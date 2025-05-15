<template>
  <div>
    <!-- 操作查询栏（仅保留查询功能） -->
    <div class="user-search-container">
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
    </div>

    <!-- 表格展示（移除选择列和操作列） -->
    <el-table
        :data="supplyList.records"
        max-height="80vh"
        row-key="id"
        stripe
    >
      <!-- 删除type="selection"选择列 -->
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
      <!-- 删除固定右侧的操作列 -->
    </el-table>

    <!-- 分页条（保留用户端分页） -->
    <BackPage :total="supplyList.total" @changePu="changePage"/>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import BackPage from "@/components/row/BackPage.vue";
import {reqPetSupplyPage} from "@/request/PetSupplyApi"; // 仅保留查询接口
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
 * 查询参数与分页（仅保留查询逻辑）
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
</script>

<style scoped>
.avatar {
  width: 128px;
  height: 128px;
  display: block;
}

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
</style>
