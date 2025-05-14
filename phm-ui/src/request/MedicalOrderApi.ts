import reqApi from "@/request/reqAPI.ts";
import {Res} from "@/request/Res.ts";
import {Page} from "@/model/DO/Page.ts";
import {MedicalOrder} from "@/model/entiy/MedicalOrder.ts";
import {MedicalOrderVO} from "@/model/VO/MedicalOrderVO.ts";

// 分页查询医疗订单（支持医疗项目名、宠物名、用户名模糊搜索）
export const reqMedicalOrderPage = (params: {
  numPage: number;          // 当前页码（必填）
  pageSize: number;         // 每页大小（必填）
  medicalItemName: string | null; // 医疗项目名（模糊，可选）
  petName: string | null;         // 宠物名（模糊，可选）
  userName: string | null;        // 用户名（模糊，可选）
}): Promise<Page<MedicalOrderVO>> => {
  return reqApi("medical-order/page", {params});
};

// 添加医疗订单（需经理权限）
export const reqAddMedicalOrder = (data: Omit<MedicalOrder, 'id'>): Promise<Res> => {
  return reqApi.post("medical-order", data);
};

// 更新医疗订单（需经理权限）
export const reqUpdateMedicalOrder = (data: MedicalOrder): Promise<Res> => {
  return reqApi.put("medical-order", data);
};

// 单条删除医疗订单（需经理权限）
export const reqDeleteMedicalOrder = (id: number): Promise<Res> => {
  return reqApi.delete(`medical-order/${id}`);
};

// 批量删除医疗订单（需经理权限）
export const reqDelMedicalOrderBatch = (ids: number[]): Promise<Res> => {
  return reqApi.delete("medical-order/batch", {data: ids});
};