// PetSupplyApi.ts
import reqApi from "@/request/reqAPI.ts";
import {Page} from "@/model/DO/Page.ts";
import {Res} from "@/request/Res.ts";
import {PetSupply} from "@/model/entiy/PetSupply.ts";


/**
 * 分页查询宠物用品（支持名称模糊+上架时间精准）
 * @param params 查询参数（包含分页信息、名称、上架时间）
 * @returns 分页结果
 */
export const reqPetSupplyPage = (params: {
  numPage: number;        // 当前页码（必填）
  pageSize: number;       // 每页大小（必填）
  name?: string;          // 用品名称（模糊搜索，可选）
  shelfTime?: string;     // 上架时间（精准匹配，ISO格式字符串，可选）
}): Promise<Page<PetSupply>> => {
  return reqApi("/pet-supply/page", {params});
};

/**
 * 新增宠物用品（需经理权限）
 * @param data 宠物用品信息（不含ID）
 * @returns 操作结果
 */
export const reqAddPetSupply = (data: Omit<PetSupply, 'id'>): Promise<Res> => {
  return reqApi.post("/pet-supply", data);
};

/**
 * 修改宠物用品（需经理权限）
 * @param data 宠物用品信息（包含ID）
 * @returns 操作结果
 */
export const reqUpdatePetSupply = (data: PetSupply): Promise<Res> => {
  return reqApi.put("/pet-supply", data);
};

/**
 * 删除单个宠物用品（需经理权限）
 * @param id 用品ID
 * @returns 操作结果
 */
export const reqDeletePetSupply = (id: number): Promise<Res> => {
  return reqApi.delete(`/pet-supply/${id}`);
};

/**
 * 批量删除宠物用品（需经理权限）
 * @param ids 用品ID列表
 * @returns 操作结果
 */
export const reqDelPetSupplyBatch = (ids: number[]): Promise<Res> => {
  return reqApi.delete(`/pet-supply/batch/${ids.join(',')}`);
};
