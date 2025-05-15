// 宠物用品实体类型（对应Java的PetSupply）
import {PageQuery} from "@/model/VO/BackQuery.ts";

export interface PetSupply {
  id?: number;              // 主键ID（可选，新增时无ID）
  name?: string;             // 用品名称（必填）
  description?: string;     // 用品介绍（可选）
  price?: number;
  shelfTime?: string;       // 上架时间（格式：YYYY-MM-DD）
  imageFilename?: string;   // 图片文件名（可选）
}

// 宠物用品查询参数（继承PageQuery）
export interface PetSupplyQuery extends PageQuery {
  name?: string;             // 用品名称（模糊匹配）
  shelfTime?: string;       // 上架时间（精准匹配，格式：YYYY-MM-DD HH:mm:ss）
}
