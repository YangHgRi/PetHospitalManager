export interface MedicalOrderVO {
  id: number;                  // 订单ID
  itemId: number;              // 新增：医疗项目ID（与MedicalOrder对齐）
  petId: number;               // 新增：宠物ID
  userId: number;              // 新增：用户ID
  itemPrice: number;
  medicalItemName: string;     // 医疗项目名称（联查）
  petName: string;             // 宠物名称（联查）
  userName: string;            // 用户名（联查）
  createdAt: string;           // 创建时间（字符串格式）
  formattedCreatedAt: string;  // 格式化后的时间
}
