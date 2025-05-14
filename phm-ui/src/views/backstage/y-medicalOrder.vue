<template>
  <div>
    <!-- 操作栏 -->
    <BackOpCol role="医疗订单" @addRole="addMedicalOrder" @delBatch="delBatch" @query="sendQuery">
      <el-input v-model="qp.medicalItemName" placeholder="医疗项目名称" size="large" @keyup.enter="sendQuery"/>
      <el-input v-model="qp.petName" placeholder="宠物名称" size="large" @keyup.enter="sendQuery"/>
      <el-input v-model="qp.userName" placeholder="用户名称" size="large" @keyup.enter="sendQuery"/>
    </BackOpCol>

    <!-- 订单列表 -->
    <el-table :data="medicalOrderList.records"
              max-height="80vh"
              row-key="id"
              stripe
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="30"/>
      <el-table-column label="医疗项目名称" prop="medicalItemName"/>
      <el-table-column label="宠物名称" prop="petName"/>
      <el-table-column label="用户名称" prop="userName"/>
      <el-table-column label="项目价格" prop="itemPrice"/>
      <el-table-column label="创建时间" prop="formattedCreatedAt" width="200">
        <template #default="scope">
          {{ scope.row.formattedCreatedAt }}
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <el-button-group>
            <!-- 新增编辑按钮 -->
            <el-button :icon="Edit" circle type="primary" @click="editMedicalOrder(scope.row)"/>
            <el-button :icon="Delete" circle type="danger" @click="delOne(scope.row)"/>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页条 -->
    <BackPage :total="medicalOrderList.total" @changePu="changePage"/>

    <!-- 新增订单模态框 -->
    <el-dialog v-model="modalVisible" :title="modalTitle" draggable width="60%">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <!-- 医疗项目选择 -->
        <el-form-item label="医疗项目" prop="itemId">
          <el-select v-model="form.itemId" placeholder="请选择医疗项目" clearable @change="handleItemChange">
            <el-option v-for="item in medicalItems" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <!-- 用户选择 -->
        <el-form-item label="用户" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择用户" clearable @change="handleUserChange">
            <el-option v-for="user in users" :key="user.clientId" :label="user.clientName" :value="user.clientId"/>
          </el-select>
        </el-form-item>
        <!-- 宠物选择（依赖前两项选择） -->
        <el-form-item label="宠物" prop="petId">
          <el-select
              v-model="form.petId"
              placeholder="请先选择医疗项目和用户"
              clearable
              :disabled="!selectedItem || !selectedUser">
            <el-option
                v-for="pet in filteredPets"
                :key="pet.petId"
                :label="`${pet.petName}（${pet.petVariety}）`"
                :value="pet.petId"/>
            <template #empty>
              无符合条件的宠物（需满足：用户名下且品种匹配医疗项目适用品种）
            </template>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="modalVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {Delete, Edit} from "@element-plus/icons-vue";
import {onMounted, reactive, ref} from "vue";
import BackOpCol from "@/components/row/BackOpCol.vue";
import BackPage from "@/components/row/BackPage.vue";
import {ElMessage, ElMessageBox, FormInstance, FormRules} from "element-plus";
import {
  reqAddMedicalOrder,
  reqDeleteMedicalOrder,
  reqDelMedicalOrderBatch,
  reqMedicalOrderPage,
  reqUpdateMedicalOrder
} from "@/request/MedicalOrderApi";
import {Page} from "@/model/DO/Page.ts";
import {PageQuery} from "@/model/VO/BackQuery.ts";
import {Pet} from "@/model/entiy/Pet.ts";
import {Client} from "@/model/entiy/Client.ts";
import {MedicalOrderVO} from "@/model/VO/MedicalOrderVO.ts";
import {MedicalOrder} from "@/model/entiy/MedicalOrder.ts";
import {MedicalItem} from "@/model/entiy/MedicalItem.ts"; // 需包含workedVariety字段
import {reqPetList} from "@/request/PetApi.ts";
import {reqClientList} from "@/request/ClientApi.ts";
import {reqMedicalItemPage} from "@/request/MedicalItemApi.ts";

// 基础数据缓存（后端已过滤无效数据）
const medicalItems = ref<MedicalItem[]>([]);  // 医疗项目列表（后端返回有效数据）
const users = ref<Client[]>([]);               // 用户列表（后端返回有效数据）
const pets = ref<Pet[]>([]);                   // 宠物列表（后端返回有效数据）

// 动态状态变量
const selectedItem = ref<MedicalItem | null>(null);   // 当前选中的医疗项目
const selectedUser = ref<Client | null>(null);        // 当前选中的用户
const filteredPets = ref<Pet[]>([]);                  // 动态过滤后的宠物列表

// 生命周期：初始化加载基础数据和订单列表
onMounted(async () => {
  await loadBaseData();  // 加载后端已过滤的基础数据
  sendQuery();
});

// 加载基础数据（后端已过滤无效记录）
const loadBaseData = async () => {
  // 医疗项目（后端返回未删除的记录）
  const medicalRes = await reqMedicalItemPage({pageSize: -1, numPage: 1});
  medicalItems.value = medicalRes.records;

  // 用户（后端返回有效用户）
  const userRes = await reqClientList({pageSize: -1, numPage: 1, clientName: ""});
  users.value = userRes.records;

  // 宠物（后端返回有效宠物）
  const petRes = await reqPetList({pageSize: -1, numPage: 1, petName: "", clientName: ""});
  pets.value = petRes.records;
};

// 监听医疗项目选择变化
const handleItemChange = (itemId: number) => {
  selectedItem.value = medicalItems.value.find(item => item.id === itemId) || null;
  form.value.petId = null;
  updateFilteredPets();  // 触发宠物过滤
};

// 监听用户选择变化
const handleUserChange = (userId: number) => {
  selectedUser.value = users.value.find(user => user.clientId === userId) || null;
  form.value.petId = null;
  updateFilteredPets();  // 触发宠物过滤
};

// 核心过滤逻辑：根据选中项目和用户计算可用宠物
const updateFilteredPets = () => {
  if (!selectedItem.value || !selectedUser.value) {
    filteredPets.value = [];
    return;
  }

  const {workedVariety} = selectedItem.value;  // 医疗项目适用品种（如"犬,猫"）
  const {clientId} = selectedUser.value;       // 用户ID

  // 过滤条件：宠物属于该用户，且品种匹配医疗项目适用品种
  if (typeof workedVariety === "string") {
    filteredPets.value = pets.value.filter(pet =>
        pet.clientId === clientId &&
        pet.petVariety.includes(workedVariety)
    );
  }
};

/**
 * 表格查询相关
 */
const qp = reactive<{
  medicalItemName: string | null;  // 医疗项目名（模糊）
  petName: string | null;          // 宠物名（模糊）
  userName: string | null;         // 用户名（模糊）
  numPage: number;          // 当前页
  pageSize: number;         // 每页大小
}>({
  medicalItemName: null,
  petName: null,
  userName: null,
  numPage: 1,
  pageSize: 10,
});

const medicalOrderList = ref<Page<MedicalOrderVO>>({records: [], total: 0});

const changePage = (val: PageQuery) => {
  qp.numPage = val.numPage;
  qp.pageSize = val.pageSize;
  sendQuery();
};

const sendQuery = () => {
  reqMedicalOrderPage(qp).then((res) => {
    medicalOrderList.value = res;
  });
};

/**
 * 数据删除相关
 */
const selectedIds = ref<number[]>([]);

const handleSelectionChange = (val: MedicalOrderVO[]) => {
  selectedIds.value = val.map((item) => item.id || 0);
};

const delBatch = () => {
  if (selectedIds.value.length === 0) return;
  ElMessageBox.confirm("确认批量删除选中的医疗订单？", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    reqDelMedicalOrderBatch(selectedIds.value).then((res) => {
      if (res.code === 1) {
        ElMessage.success("批量删除成功");
        sendQuery();
      }
    });
  }).catch(() => {
    ElMessage.info("已取消删除");
  });
};

const delOne = (row: MedicalOrderVO) => {
  ElMessageBox.confirm(`确认删除订单？`, "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    reqDeleteMedicalOrder(row.id || 0).then((res) => {
      if (res.code === 1) {
        ElMessage.success("删除成功");
        sendQuery();
      }
    });
  }).catch(() => {
    ElMessage.info("已取消删除");
  });
};

/**
 * 新增订单模态框
 */
const modalVisible = ref(false);
const modalTitle = ref("新增医疗订单");  // 默认新增标题
const form = ref<MedicalOrder>({
  id: null,
  itemId: null,
  petId: null,
  userId: null,
});
const formRef = ref<FormInstance>();

const addMedicalOrder = () => {
  modalTitle.value = "新增医疗订单";
  form.value = {id: null, itemId: null, petId: null, userId: null};
  selectedItem.value = null;   // 重置选中项目
  selectedUser.value = null;   // 重置选中用户
  filteredPets.value = [];     // 清空过滤宠物
  formRef.value?.resetFields();
  modalVisible.value = true;
};

/**
 * 表单校验规则
 */
const rules = reactive<FormRules>({
  itemId: [
    {required: true, message: "请选择医疗项目", trigger: "change"},
    // 允许值为null（初始状态），选择后必须是大于0的数字
    {type: "number", min: 1, message: "请选择有效医疗项目", trigger: "change", required: true}
  ],
  petId: [
    {required: true, message: "请选择宠物", trigger: "change"},
    {type: "number", min: 1, message: "请选择有效宠物", trigger: "change", required: true}
  ],
  userId: [
    {required: true, message: "请选择用户", trigger: "change"},
    {type: "number", min: 1, message: "请选择有效用户", trigger: "change", required: true}
  ]
});

// 提交表单（区分新增/修改）
const submitForm = async () => {
  await formRef.value?.validate(async (valid) => {
    if (!valid) return;
    modalVisible.value = false;

    // 判断是新增还是修改
    if (form.value.id === 0 || form.value.id === null || form.value.id === undefined) {
      // 新增场景
      await reqAddMedicalOrder(form.value).then((res) => {
        if (res.code === 1) {
          ElMessage.success("新增订单成功");
          sendQuery();  // 刷新列表
        }
      });
    } else {
      // 修改场景（调用更新API）
      await reqUpdateMedicalOrder(form.value).then((res) => {
        if (res.code === 1) {
          ElMessage.success("修改订单成功");
          sendQuery();  // 刷新列表
        }
      });
    }
  });
};


const editMedicalOrder = (row: MedicalOrderVO) => {
  modalTitle.value = "编辑医疗订单";
  modalVisible.value = true;

  // 直接从row中提取关联ID填充表单（无需额外查找）
  form.value = {
    id: row.id,                 // 订单ID（修改时必传）
    itemId: row.itemId,         // 直接使用row中的itemId
    petId: row.petId,           // 直接使用row中的petId
    userId: row.userId          // 直接使用row中的userId
  };

  // 同步更新关联状态（用于宠物过滤）
  selectedItem.value = medicalItems.value.find(item => item.id === row.itemId) || null;
  selectedUser.value = users.value.find(user => user.clientId === row.userId) || null;
  updateFilteredPets();
};
</script>

<style scoped>
.el-select {
  width: 100%;
}
</style>
