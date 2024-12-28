export interface GroupMemberVO {
  /**
   * id
   */
  id: string | number;

  /**
   * 群id
   */
  groupId: string | number;

  /**
   * 用户id
   */
  userId: string | number;

  /**
   * 组内显示名称
   */
  remarkNickName: string;

  /**
   * 用户头像
   */
  headImage: string;

  /**
   * 用户头像Url
   */
  headImageUrl: string;
  /**
   * 群名备注
   */
  remarkGroupName: string;

  /**
   * 是否已退出
   */
  quit: number;

  /**
   * 创建时间
   */
  createdTime: string;

  /**
   * 退出时间
   */
  quitTime: string;

  /**
   * 用户昵称
   */
  userNickName: string;

}

export interface GroupMemberForm extends BaseEntity {
  /**
   * id
   */
  id?: string | number;

  /**
   * 群id
   */
  groupId?: string | number;

  /**
   * 用户id
   */
  userId?: string | number;

  /**
   * 组内显示名称
   */
  remarkNickName?: string;

  /**
   * 用户头像
   */
  headImage?: string;

  /**
   * 群名备注
   */
  remarkGroupName?: string;

  /**
   * 是否已退出
   */
  quit?: number;

  /**
   * 创建时间
   */
  createdTime?: string;

  /**
   * 退出时间
   */
  quitTime?: string;

  /**
   * 用户昵称
   */
  userNickName?: string;

}

export interface GroupMemberQuery extends PageQuery {

  /**
   * 群id
   */
  groupId?: string | number;

  /**
   * 用户id
   */
  userId?: number;

  /**
 * 用户名
 */
  userName: String,

  /**
   * 组内显示名称
   */
  remarkNickName?: string;

  /**
   * 用户头像
   */
  headImage?: string;

  /**
   * 群名备注
   */
  remarkGroupName?: string;

  /**
   * 是否已退出
   */
  quit?: number;

  /**
   * 创建时间
   */
  createdTime?: string;

  /**
   * 退出时间
   */
  quitTime?: string;

  /**
   * 用户昵称
   */
  userNickName?: string;
  /**
   * 群内显示名
   */
  showNickName?: string;
  /**
   * 日期范围参数
   */
  params?: any;
}



