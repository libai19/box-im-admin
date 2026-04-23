export interface SystemMessageVO {
  id: string | number;
  title: string;
  cover: string;
  summary: string;
  contentType: number;
  content: string;
  linkUrl: string;
  type: number;
  targetType: number;
  targetIds: string;
  status: number;
  creatorName: string;
  pushTime: string;
  createdTime: string;
}

export interface SystemMessageQuery extends PageQuery {
  title?: string;
  contentType?: number;
  status?: number;
  params?: any;
}

export interface SystemMessageForm {
  id?: string | number;
  title?: string;
  cover?: string;
  summary?: string;
  contentType?: number;
  content?: string;
  linkUrl?: string;
  type?: number;
  targetType?: number;
  targetIds?: string;
  status?: number;
}
