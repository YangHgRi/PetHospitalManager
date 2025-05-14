<template>
  <div>
    <!--操作框-->
    <BackOpCol role="医疗项目" @addRole="addMedicalItem" @delBatch="delBatch" @query="sendQuery">
      <el-input v-model="qp.name" placeholder="项目名称" size="large" @keyup.enter="sendQuery"/>
      <el-input v-model="qp.category" placeholder="项目分类" size="large" @keyup.enter="sendQuery"/>
      <el-input v-model="qp.workedVariety" placeholder="适用品种" size="large" @keyup.enter="sendQuery"/>
    </BackOpCol>
    <!--列表展示-->
    <el-table :data="medicalItemList.records"
              max-height="80vh"
              row-key="id"
              stripe
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="30"/>
      <el-table-column label="项目名称" prop="name"/>
      <el-table-column label="项目分类">
        <template #default="scope">
          <el-tag>{{ scope.row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="适用品种" prop="workedVariety"/>
      <el-table-column label="项目价格">
        <template #default="scope">
          {{ scope.row.price.toFixed(2) }} 元
        </template>
      </el-table-column>
      <el-table-column label="详细描述" prop="description" :show-overflow-tooltip="true"/>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <el-button-group>
            <el-button :icon="Edit" circle type="warning" @click="showDialog(scope.row)"/>
            <el-button :icon="Delete" circle type="danger" @click="delOne(scope.row)"/>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <!--分页条-->
    <BackPage :total="medicalItemList.total" @changePu="changePage"/>
    <!--修改、新增时的模态框-->
    <el-dialog v-model="modalVisible" :title="modalTitle" draggable width="60%">
      <!--表单-->
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="form.name" clearable placeholder="请输入项目名称"/>
        </el-form-item>
        <el-form-item label="项目分类" prop="category">
          <el-input v-model="form.category" clearable placeholder="请输入项目分类（如手术/检测）"/>
        </el-form-item>
        <el-form-item label="适用品种" prop="workedVariety">
          <el-input v-model="form.workedVariety" clearable placeholder="请输入适用品种（如犬/猫/鱼）"/>
        </el-form-item>
        <el-form-item label="项目价格" prop="price">
          <el-input-number v-model="form.price" :min="0" step="0.01" placeholder="请输入项目价格"/>
        </el-form-item>
        <el-form-item label="详细描述">
          <el-input v-model="form.description" type="textarea" rows="4" placeholder="请输入项目详细描述"/>
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
  reqAddMedicalItem,
  reqDeleteMedicalItem,
  reqDelMedicalItemBatch,
  reqMedicalItemPage,
  reqUpdateMedicalItem
} from "@/request/MedicalItemApi";
import {Page} from "@/model/DO/Page.ts";
import {PageQuery} from "@/model/VO/BackQuery.ts";

/**
 * 生命周期相关
 */
onMounted(() => {
  sendQuery();
});

/**
 * 表格查询相关
 */
const qp = reactive<{
  name: string;
  category: string;
  workedVariety: string;
  numPage: number;
  pageSize: number;
}>({
  name: "",
  category: "",
  workedVariety: "",
  numPage: 1,
  pageSize: 10,
});

const medicalItemList = ref<Page<MedicalItem>>({records: [], total: 0});

const changePage = (val: PageQuery) => {
  qp.numPage = val.numPage;
  qp.pageSize = val.pageSize;
  sendQuery();
};

const sendQuery = () => {
  reqMedicalItemPage(qp).then((res) => {
    medicalItemList.value = res;
  });
};

/**
 * 数据删除相关
 */
const selectedIds = ref<number[]>([]);

const handleSelectionChange = (val: MedicalItem[]) => {
  selectedIds.value = val.map((item) => item.id || 0);
};

const delBatch = () => {
  if (selectedIds.value.length === 0) return;
  ElMessageBox.confirm("确认批量删除选中的医疗项目？", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    reqDelMedicalItemBatch(selectedIds.value).then((res) => {
      if (res.code === 1) sendQuery();
    });
  }).catch(() => {
    ElMessage.info("已取消删除");
  });
};

const delOne = (row: MedicalItem) => {
  ElMessageBox.confirm(`确认删除医疗项目「${row.name}」？`, "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    reqDeleteMedicalItem(row.id || 0).then((res) => {
      if (res.code === 1) sendQuery();
    });
  }).catch(() => {
    ElMessage.info("已取消删除");
  });
};

/**
 * 新增/修改模态框
 */
const modalVisible = ref(false);
const modalTitle = ref<"新增医疗项目" | "修改医疗项目">("新增医疗项目");
const form = ref<MedicalItem>({
  id: 0,
  name: "",
  category: "",
  workedVariety: "",
  price: 0,
  description: "",
});
const formRef = ref<FormInstance>();

const addMedicalItem = () => {
  modalTitle.value = "新增医疗项目";
  form.value = {id: 0, name: "", category: "", workedVariety: "", price: 0, description: ""};
  formRef.value?.resetFields();
  modalVisible.value = true;
};

const showDialog = (row: MedicalItem) => {
  modalTitle.value = "修改医疗项目";
  form.value = {...row}; // 避免响应式引用问题
  modalVisible.value = true;
};

/**
 * 表单提交
 */
const rules = reactive<FormRules>({
  name: [
    {required: true, message: "请输入项目名称", trigger: "blur"},
  ],
  category: [
    {required: true, message: "请输入项目分类", trigger: "blur"},
  ],
  workedVariety: [
    {required: true, message: "请输入适用品种", trigger: "blur"},
  ],
  price: [
    {required: true, message: "请输入项目价格", trigger: "blur"},
    {type: "number", message: "价格必须为正数", min: 0.01, trigger: "blur"},
  ],
});

const submitForm = async () => {
  await formRef.value?.validate(async (valid) => {
    if (!valid) return;
    modalVisible.value = false;
    if (modalTitle.value === "新增医疗项目") {
      await reqAddMedicalItem(form.value).then((res) => {
        if (res.code === 1) {
          ElMessage.success("新增成功");
          sendQuery();
        }
      });
    } else {
      await reqUpdateMedicalItem(form.value).then((res) => {
        if (res.code === 1) {
          ElMessage.success("修改成功");
          sendQuery();
        }
      });
    }
  });
};
</script>

<style scoped>
</style>
