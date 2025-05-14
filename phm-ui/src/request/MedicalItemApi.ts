// 分页查询医疗项目（支持项目名称、分类、适用品种模糊搜索）
import {Page} from "@/model/DO/Page.ts";
import reqApi from "@/request/reqAPI.ts";
import {Res} from "@/request/Res.ts";
import {MedicalItem} from "@/model/entiy/MedicalItem.ts";

export const reqMedicalItemPage = (params: {
  numPage: number;        // 当前页码（必填）
  pageSize: number;       // 每页大小（必填）
  name?: string;          // 项目名称（模糊搜索，可选）
  category?: string;      // 项目分类（模糊搜索，可选）
  workedVariety?: string; // 适用品种（模糊搜索，可选）
}): Promise<Page<MedicalItem>> => {
  return reqApi("medical-item/page", {params});
};

// 根据ID查询单个医疗项目
export const reqMedicalItemById = (id: number): Promise<MedicalItem> => {
  return reqApi(`medical-item/${id}`);
};

// 添加医疗项目（需经理权限）
export const reqAddMedicalItem = (data: Omit<MedicalItem, 'id'>): Promise<Res> => {
  return reqApi.post("medical-item", data);
};

// 更新医疗项目（需经理权限）
export const reqUpdateMedicalItem = (data: MedicalItem): Promise<Res> => {
  return reqApi.put("medical-item", data);
};

// 删除医疗项目（需经理权限）
export const reqDeleteMedicalItem = (id: number): Promise<Res> => {
  return reqApi.delete(`medical-item/${id}`);
};

// 删除医疗项目（需经理权限）
export const reqDelMedicalItemBatch = (ids: number[]): Promise<Res> => {
  return reqApi.delete(`medical-item/batch`, {data: ids});
};