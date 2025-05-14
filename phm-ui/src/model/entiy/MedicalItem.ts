// 医疗项目实体类型（对应Java的MedicalItem）
interface MedicalItem {
  id?: number;            // 主键ID（可选，新增时无ID）
  name: string;           // 项目名称（必填）
  category?: string;      // 项目分类（如手术/检测等，可选）
  workedVariety?: string; // 适用品种（犬、猫等，可选）
  price: number;          // 项目价格（必填，对应Java的BigDecimal）
  description?: string;   // 详细描述（可选）
}