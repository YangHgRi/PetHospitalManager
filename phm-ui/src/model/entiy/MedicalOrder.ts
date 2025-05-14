// 类型定义
export interface MedicalOrder {
  id?: number | null;       // 订单ID（新增时可选）
  itemId: number | null;    // 医疗项目ID（必填）
  petId: number | null;     // 宠物ID（必填）
  userId: number | null;    // 用户ID（必填）
}