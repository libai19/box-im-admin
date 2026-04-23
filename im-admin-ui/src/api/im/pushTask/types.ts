export interface PushTaskVO {
  id: string | number;
  messageId: string | number;
  title: string;
  content: string;
  targetType: number;
  targetIds: string;
  status: number;
  failReason: string;
  creatorName: string;
  pushTime: string;
  createdTime: string;
}

export interface PushTaskQuery extends PageQuery {
  title?: string;
  status?: number;
  params?: any;
}
