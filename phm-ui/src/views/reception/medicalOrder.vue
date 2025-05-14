<template>
  <div>
    <!-- 操作栏 -->
    <BackOpCol role="医疗订单" @addRole="addMedicalOrder" @delBatch="delBatch" @query="sendQuery">
      <el-input v-model="qp.medicalItemName" placeholder="医疗项目名称" size="large" @keyup.enter="sendQuery"/>
      <el-input v-model="qp.petName" placeholder="宠物名称" size="large" @keyup.enter="sendQuery"/>
      <!-- 删除用户名称输入框 -->
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
      <!-- 删除用户名称列 -->
      <el-table-column label="项目价格" prop="itemPrice"/>
      <el-table-column label="创建时间" prop="formattedCreatedAt" width="200">
        <template #default="scope">
          {{ scope.row.formattedCreatedAt }}
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <el-button-group>
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
        <!-- 用户锁定为当前登录用户 -->
        <el-form-item label="用户">
          <el-input :value="client.clientName" disabled placeholder="当前登录用户"/>

        </el-form-item>
        <!-- 宠物选择（依赖前两项选择） -->
        <el-form-item label="宠物" prop="petId">
          <el-select
              v-model="form.petId"
              placeholder="请先选择医疗项目"
              clearable
              :disabled="!selectedItem">
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
import {Client, exampleClient} from "@/model/entiy/Client.ts";
import {MedicalOrderVO} from "@/model/VO/MedicalOrderVO.ts";
import {MedicalOrder} from "@/model/entiy/MedicalOrder.ts";
import {MedicalItem} from "@/model/entiy/MedicalItem.ts";
import {reqPetList} from "@/request/PetApi.ts";
import {reqMedicalItemPage} from "@/request/MedicalItemApi.ts";
import {reqLoginUser} from "@/request/PowerApi";
import {reqClientList} from "@/request/ClientApi.ts"; // 新增：假设存在获取登录用户的接口

// 新增：当前登录用户信息
const client = ref<Client>(exampleClient())

// 基础数据缓存（后端已过滤无效数据）
const medicalItems = ref<MedicalItem[]>([]);
const users = ref<Client[]>([]);
const pets = ref<Pet[]>([]);

// 动态状态变量
const selectedItem = ref<MedicalItem | null>(null);
// const selectedUser = ref<Client | null>(null);
const filteredPets = ref<Pet[]>([]);

// 生命周期：初始化加载基础数据和订单列表
onMounted(async () => {
  try {
    // 获取当前登录用户信息（新增）
    client.value = await reqLoginUser();
    if (!client.value) {
      ElMessage.error("未获取到登录用户信息，请重新登录");
      return;
    }

    await loadBaseData();
    // 初始化查询参数（新增：自动填充当前用户）
    qp.userName = client.value.clientName;  // 假设后端通过userId过滤，需根据接口调整
    sendQuery();
  } catch (error) {
    ElMessage.error("初始化失败，请刷新页面");
  }
});

// 加载基础数据（后端已过滤无效记录）
const loadBaseData = async () => {
  // 医疗项目（后端返回未删除的记录）
  const medicalRes = await reqMedicalItemPage({pageSize: -1, numPage: 1});
  medicalItems.value = medicalRes.records;

  // 用户（后端返回有效用户）
  const userRes = await reqClientList({pageSize: -1, numPage: 1, clientName: client.value.clientName});
  users.value = userRes.records;

  // 宠物（后端返回有效宠物）
  const petRes = await reqPetList({pageSize: -1, numPage: 1, petName: "", clientName: client.value.clientName});
  pets.value = petRes.records;
};

// 监听医疗项目选择变化（调整：无需监听用户选择）
const handleItemChange = (itemId: number) => {
  selectedItem.value = medicalItems.value.find(item => item.id === itemId) || null;
  form.value.petId = null;
  updateFilteredPets();
};

// 核心过滤逻辑（调整：固定使用当前用户）
const updateFilteredPets = () => {
  if (!selectedItem.value || !client.value) {
    filteredPets.value = [];
    return;
  }

  const {workedVariety} = selectedItem.value;
  const {clientId} = client.value;  // 固定使用当前用户ID

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
  userName: client.value.clientName,
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
  // 确保userId已设置（当前用户）
  if (!qp.userName) return;
  reqMedicalOrderPage(qp).then((res) => {
    medicalOrderList.value = res;
  });
};

/**
 * 数据删除相关（无变化）
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
 * 新增订单模态框（调整：自动填充当前用户）
 */
const modalVisible = ref(false);
const modalTitle = ref("新增医疗订单");
const form = ref<MedicalOrder>({
  id: null,
  itemId: null,
  petId: null,
  userId: null,  // 将由当前用户自动填充
});
const formRef = ref<FormInstance>();

const addMedicalOrder = () => {
  modalTitle.value = "新增医疗订单";
  form.value = {
    id: null,
    itemId: null,
    petId: null,
    userId: client.value?.clientId  // 自动填充当前用户ID
  };
  selectedItem.value = null;
  filteredPets.value = [];
  formRef.value?.resetFields();
  modalVisible.value = true;
};

/**
 * 表单校验规则（调整：移除userId校验，由系统自动填充）
 */
const rules = reactive<FormRules>({
  itemId: [
    {required: true, message: "请选择医疗项目", trigger: "change"},
    {type: "number", min: 1, message: "请选择有效医疗项目", trigger: "change", required: true}
  ],
  petId: [
    {required: true, message: "请选择宠物", trigger: "change"},
    {type: "number", min: 1, message: "请选择有效宠物", trigger: "change", required: true}
  ],
  // 移除userId校验（系统自动填充）
});

// 提交表单（无变化）
const submitForm = async () => {
  await formRef.value?.validate(async (valid) => {
    if (!valid) return;
    modalVisible.value = false;

    if (form.value.id === null) {
      await reqAddMedicalOrder(form.value).then((res) => {
        if (res.code === 1) {
          ElMessage.success("新增订单成功");
          sendQuery();
        }
      });
    } else {
      await reqUpdateMedicalOrder(form.value).then((res) => {
        if (res.code === 1) {
          ElMessage.success("修改订单成功");
          sendQuery();
        }
      });
    }
  });
};

// 编辑订单（调整：强制使用当前用户）
const editMedicalOrder = (row: MedicalOrderVO) => {
  modalTitle.value = "编辑医疗订单";
  modalVisible.value = true;

  form.value = {
    id: row.id,
    itemId: row.itemId,
    petId: row.petId,
    userId: client.value?.clientId  // 强制使用当前用户ID
  };

  selectedItem.value = medicalItems.value.find(item => item.id === row.itemId) || null;
  updateFilteredPets();  // 无需更新selectedUser（已固定）
};
</script>

<style scoped>
.el-select {
  width: 100%;
}
</style>
