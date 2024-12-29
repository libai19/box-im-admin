export interface SystemMessageVO {
  /**
   * id
   */
  id: string | number;

  /**
   * 标题
   */
  title: string;

  /**
   * 封面
   */
  coverUrl: string;

  /**
   * 简介
   */
  intro: string;

  /**
   * 内容类型 0:富文本  1:外部链接
   */
  contentType: number;

  /**
   * 富文本内容，base64编码
   */
  richText: string;

  /**
   * 外部链接
   */
  externLink: string;

  /**
   * 创建者
   */
  creator: number;
  
  /**
   * 创建者
   */
  creatorName: string

}

export interface SystemMessageForm extends BaseEntity {
  /**
   * id
   */
  id?: string | number;

  /**
   * 标题
   */
  title?: string;

  /**
   * 封面
   */
  coverUrl?: string;

  /**
   * 简介
   */
  intro?: string;

  /**
   * 内容类型 0:富文本  1:外部链接
   */
  contentType?: number;

  /**
   * 富文本内容，base64编码
   */
  richText?: string;

  /**
   * 外部链接
   */
  externLink?: string;

  /**
   * 删除标识  0：正常   1：已删除
   */
  deleted?: number;

  /**
   * 创建者
   */
  creator?: number;

  /**
   * 更新者
   */
  updater?: number;

}

export interface SystemMessageQuery extends PageQuery {

  /**
   * 标题
   */
  title?: string;

  /**
   * 封面
   */
  coverUrl?: string;

  /**
   * 简介
   */
  intro?: string;

  /**
   * 内容类型 0:富文本  1:外部链接
   */
  contentType?: number;

  /**
   * 富文本内容，base64编码
   */
  richText?: string;

  /**
   * 外部链接
   */
  externLink?: string;

  /**
   * 删除标识  0：正常   1：已删除
   */
  deleted?: number;

  /**
   * 创建者
   */
  creator?: number;

  /**
   * 更新者
   */
  updater?: number;

    /**
     * 日期范围参数
     */
    params?: any;
}



