export interface SmPushTaskVO {
  /**
   * id
   */
  id: number;

  /**
   * 系统消息id
   */
  messageId:  number;

  /**
   * 发送序列号
   */
  seqNo: number;

  /**
   * 推送时间
   */
  sendTime: string;

  /**
   * 状态 1:待发送 2:发送中 3:已发送 4:已取消
   */
  status: number;

  /**
   * 是否发送给全体用户
   */
  sendToAll: boolean;

  /**
   * 接收用户id,逗号分隔,send_to_all为false时有效
   */
  recvIds: string;

  /**
   * 删除标识  0：正常   1：已删除
   */
  deleted: number;

  /**
   * 创建者
   */
  creator: number;

  /**
   * 更新者
   */
  updater: number;

}

export interface SmPushTaskForm extends BaseEntity {
  /**
   * id
   */
  id?:  number;

  /**
   * 系统消息id
   */
  messageId?: number;

  /**
   * 发送序列号
   */
  seqNo?: number;

  /**
   * 推送时间
   */
  sendTime?: string;

  /**
   * 状态 1:待发送 2:发送中 3:已发送 4:已取消
   */
  status?: number;

  /**
   * 是否发送给全体用户
   */
  sendToAll?: boolean;

  /**
   * 接收用户id,逗号分隔,send_to_all为false时有效
   */
  recvIds?: string;

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

export interface SmPushTaskQuery extends PageQuery {

  /**
   * 系统消息id
   */
  messageId?: number;

  /**
   * 发送序列号
   */
  seqNo?: number;

  /**
   * 推送时间
   */
  sendTime?: string;

  /**
   * 状态 1:待发送 2:发送中 3:已发送 4:已取消
   */
  status?: number;

  /**
   * 是否发送给全体用户
   */
  sendToAll?: boolean;

  /**
   * 接收用户id,逗号分隔,send_to_all为false时有效
   */
  recvIds?: string;

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



