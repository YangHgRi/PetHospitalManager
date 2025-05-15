<template>
  <div>
    <!-- 操作查询栏 -->
    <BackOpCol role="宠物用品" @addRole="addSupply" @delBatch="delBatch" @query="sendQuery">
      <el-input v-model="qp.name" placeholder="用品名称" size="large" @keyup.enter="sendQuery"/>
      <el-date-picker
          v-model="qp.shelfTime"
          type="date"
          placeholder="选择上架时间"
          size="large"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="sendQuery"
      />
    </BackOpCol>

    <!-- 表格展示 -->
    <el-table
        :data="supplyList.records"
        max-height="80vh"
        row-key="id"
        stripe
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="30"/>
      <el-table-column label="图片" width="240">
        <template #default="scope">
          <img v-if="scope.row.imageFilename" :src="'/api/common/download?name='+scope.row.imageFilename" alt="头像"
               class="avatar"/>
          <span v-else>无图片</span>
        </template>
      </el-table-column>
      <el-table-column label="用品名称" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column label="用品介绍" prop="description" :show-overflow-tooltip="true"/>
      <el-table-column label="用品价格">
        <template #default="scope">
          {{ scope.row.price.toFixed(2) }} 元
        </template>
      </el-table-column>
      <el-table-column label="上架时间" prop="shelfTime" width="180"/>
      <el-table-column fixed="right" label="操作" width="120">
        <template #default="scope">
          <el-button-group>
            <el-button :icon="Edit" circle type="warning" @click="showDialog(scope.row)"/>
            <el-button :icon="Delete" circle type="danger" @click="delOne(scope.row)"/>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页条 -->
    <BackPage :total="supplyList.total" @changePu="changePage"/>

    <!-- 新增/修改模态框 -->
    <el-dialog v-model="modalVisible" :title="modalTitle" draggable width="60%">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="用品名称" prop="name">
          <el-input v-model="form.name" clearable placeholder="请输入用品名称"/>
        </el-form-item>
        <el-form-item label="用品介绍">
          <el-input v-model="form.description" type="textarea" rows="3" placeholder="请输入用品介绍"/>
        </el-form-item>
        <el-form-item label="用品价格" prop="price">
          <el-input-number v-model="form.price" :min="0" step="0.01" placeholder="请输入项目价格"/>
        </el-form-item>
        <el-form-item label="上架时间">
          <el-date-picker
              v-model="form.shelfTime"
              type="date"
              placeholder="选择上架时间"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="图片上传">
          <el-upload
              class="upload-demo"
              action="/api/common/upload"
              :on-success="handleUploadSuccess"
              :show-file-list="false"
              :before-upload="beforeUpload"
              name="myFile"
          >
            <el-button type="primary">上传图片</el-button>
            <img v-if="form.imageFilename" :src="'/api/common/download?name='+form.imageFilename" alt="图片"
                 class="avatar"/>
          </el-upload>
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
  reqAddPetSupply,
  reqDeletePetSupply,
  reqDelPetSupplyBatch,
  reqPetSupplyPage,
  reqUpdatePetSupply
} from "@/request/PetSupplyApi";
import {Page} from "@/model/DO/Page.ts";
import {PageQuery} from "@/model/VO/BackQuery.ts";
import {PetSupply} from "@/model/entiy/PetSupply.ts";

/**
 * 生命周期初始化
 */
onMounted(() => {
  sendQuery();
});

/**
 * 查询参数与分页
 */
const qp = reactive<{
  name?: string;
  shelfTime?: string;
  numPage: number;
  pageSize: number;
}>({
  name: undefined,
  shelfTime: undefined,
  numPage: 1,
  pageSize: 10,
});

const supplyList = ref<Page<PetSupply>>({records: [], total: 0});

const changePage = (val: PageQuery) => {
  qp.numPage = val.numPage;
  qp.pageSize = val.pageSize;
  sendQuery();
};

const sendQuery = () => {
  reqPetSupplyPage(qp).then((res) => {
    supplyList.value = res;
  });
};

/**
 * 批量删除相关
 */
const selectedIds = ref<number[]>([]);

const handleSelectionChange = (val: PetSupply[]) => {
  selectedIds.value = val.map((item) => item.id || 0);
};

const delBatch = () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning("请选择需要删除的用品");
    return;
  }
  ElMessageBox.confirm("确认批量删除选中的宠物用品？", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    reqDelPetSupplyBatch(selectedIds.value).then((res) => {
      if (res.code === 1) {
        ElMessage.success("批量删除成功");
        sendQuery();
      }
    });
  }).catch(() => ElMessage.info("已取消删除"));
};

/**
 * 单个删除
 */
const delOne = (row: PetSupply) => {
  ElMessageBox.confirm(`确认删除宠物用品「${row.name}」？`, "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    reqDeletePetSupply(row.id || 0).then((res) => {
      if (res.code === 1) {
        ElMessage.success("删除成功");
        sendQuery();
      }
    });
  }).catch(() => ElMessage.info("已取消删除"));
};

/**
 * 新增/修改模态框
 */
const modalVisible = ref(false);
const modalTitle = ref<"新增宠物用品" | "修改宠物用品">("新增宠物用品");
const form = ref<PetSupply>({
  name: "",
  description: "",
  price: 0.00,
  shelfTime: undefined,
  imageFilename: undefined
});
const formRef = ref<FormInstance>();

const addSupply = () => {
  modalTitle.value = "新增宠物用品";
  form.value = {name: "", description: "", price: 0.00, shelfTime: undefined, imageFilename: undefined};
  formRef.value?.resetFields();
  modalVisible.value = true;
};

const showDialog = (row: PetSupply) => {
  modalTitle.value = "修改宠物用品";
  form.value = {...row}; // 深拷贝避免响应式问题
  modalVisible.value = true;
};

/**
 * 图片上传处理
 */
const handleUploadSuccess = (res: any) => {
  if (res) {
    form.value.imageFilename = res;
    ElMessage.success("图片上传成功");
  } else {
    ElMessage.error("图片上传失败");
  }
};

const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith("image/");
  if (!isImage) {
    ElMessage.error("只能上传图片文件");
    return false;
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    ElMessage.error("图片大小不能超过2MB");
    return false;
  }
  return true;
};
/**
 * 表单验证与提交
 */
const rules = reactive<FormRules>({
  name: [{required: true, message: "请输入用品名称", trigger: "blur"}],
});

const submitForm = async () => {
  await formRef.value?.validate(async (valid) => {
    if (!valid) return;
    modalVisible.value = false;

    const api = modalTitle.value === "新增宠物用品" ? reqAddPetSupply : reqUpdatePetSupply;
    await api(form.value).then((res) => {
      if (res.code === 1) {
        ElMessage.success(`${modalTitle.value}成功`);
        sendQuery();
      }
    });
  });
};
</script>

<style scoped>
.upload-tip {
  margin-top: 8px;
  color: #606266;
  font-size: 14px;
}

.avatar {
  width: 128px;
  height: 128px;
  display: block;
}
</style>
