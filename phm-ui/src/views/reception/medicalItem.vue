<template>
  <div>
    <!-- 查询栏（仅保留搜索功能） -->
    <div class="user-search-container">
      <el-input v-model="qp.name" placeholder="搜索项目名称" size="large" @keyup.enter="sendQuery"/>
      <el-input v-model="qp.category" placeholder="搜索项目分类" size="large" @keyup.enter="sendQuery"/>
      <el-input v-model="qp.workedVariety" placeholder="搜索适用品种" size="large" @keyup.enter="sendQuery"/>
    </div>

    <!-- 医疗项目列表（仅展示） -->
    <el-table :data="medicalItemList.records"
              max-height="80vh"
              row-key="id"
              stripe
              border>
      <el-table-column label="项目名称" prop="name"/>
      <el-table-column label="项目分类">
        <template #default="scope">
          <el-tag type="info">{{ scope.row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="适用品种" prop="workedVariety"/>
      <el-table-column label="项目价格">
        <template #default="scope">
          {{ scope.row.price.toFixed(2) }} 元
        </template>
      </el-table-column>
      <el-table-column label="详细描述" prop="description" :show-overflow-tooltip="true"/>
    </el-table>

    <!-- 分页条 -->
    <BackPage :total="medicalItemList.total" @changePu="changePage"/>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import BackPage from "@/components/row/BackPage.vue";
import {reqMedicalItemPage} from "@/request/MedicalItemApi";
import {Page} from "@/model/DO/Page.ts";
import {PageQuery} from "@/model/VO/BackQuery.ts";
import {MedicalItem} from "@/model/entiy/MedicalItem.ts";

/**
 * 生命周期初始化
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

// 分页切换处理
const changePage = (val: PageQuery) => {
  qp.numPage = val.numPage;
  qp.pageSize = val.pageSize;
  sendQuery();
};

// 执行查询
const sendQuery = () => {
  reqMedicalItemPage(qp).then((res) => {
    medicalItemList.value = res;
  });
};
</script>

<style scoped>
.user-search-container {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  padding: 0 10px;
}

.user-search-container .el-input {
  flex: 1;
  max-width: 300px;
}

.el-table {
  margin: 0 10px;
}

.el-table .el-table__empty-block {
  min-height: 300px;
}
</style>
