export interface SensitiveWordVO {
  /**
   * id
   */
  id: string | number;

  /**
   * 敏感词内容
   */
  content: string;

  /**
   * 是否启用 0:未启用 1:启用
   */
  enabled: boolean;

  /**
   * 创建者
   */
  creator: number;

}

export interface SensitiveWordForm extends BaseEntity {
  /**
   * id
   */
  id?: string | number;

  /**
   * 敏感词内容
   */
  content?: string;

  /**
   * 是否启用
   */
  enabled?: boolean;

  /**
   * 创建者
   */
  creator?: number;

}

export interface SensitiveWordQuery extends PageQuery {

  /**
   * 敏感词内容
   */
  content?: string;

  /**
   * 是否启用 0:未启用 1:启用
   */
  enabled?: boolean;

  /**
   * 创建者
   */
  creator?: number;

    /**
     * 日期范围参数
     */
    params?: any;
}



