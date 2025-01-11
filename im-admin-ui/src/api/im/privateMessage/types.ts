export interface PrivateMessageVO {
  /**
   * id
   */
  id: string | number;

  /**
   * 发送用户id
   */
  sendId: string | number;

  /**
   * 接收用户id
   */
  recvId: string | number;

  /**
   * 发送内容
   */
  content: string;

  /**
   * 消息类型 0:文字 1:图片 2:文件
   */
  type: number;

  /**
   * 状态 0:未读 1:已读 
   */
  status: number;

  /**
   * 发送时间
   */
  sendTime: string;

}

export interface PrivateMessageForm extends BaseEntity {
  /**
   * id
   */
  id?: string | number;

  /**
   * 发送用户id
   */
  sendId?: string | number;


  /**
   * 发送用户id
   */
  sendUserName: string;
  

  /**
   * 接收用户id
   */
  recvId?: string | number;


  /**
   * 接收用户
   */
  recvUserName: string;

  /**
   * 发送内容
   */
  content?: string;

  /**
   * 消息类型 0:文字 1:图片 2:文件
   */
  type?: number;

  /**
   * 状态 0:未读 1:已读 
   */
  status?: number;

  /**
   * 发送时间
   */
  sendTime?: string;

}

export interface PrivateMessageQuery extends PageQuery {

  /**
   * 发送用户id
   */
  sendId?: number;

  /**
   * 接收用户id
   */
  recvId?: number;

  /**
   * 发送内容
   */
  content?: string;

  /**
   * 消息类型 0:文字 1:图片 2:文件
   */
  type?: number;

  /**
   * 状态 0:未读 1:已读 
   */
  status?: number;

  /**
   * 发送时间
   */
  sendTime?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



