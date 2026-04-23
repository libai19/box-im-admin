export interface ComplaintVO {
  id: string | number;
  userId: string | number;
  userName: string;
  targetId: string | number;
  targetType: number;
  targetName: string;
  type: number;
  content: string;
  images: string;
  status: number;
  result: string;
  handleByName: string;
  handleTime: string;
  createdTime: string;
}

export interface ComplaintQuery extends PageQuery {
  userId?: string | number;
  targetId?: string | number;
  targetType?: number;
  type?: number;
  status?: number;
  content?: string;
  params?: any;
}

export interface ComplaintHandleForm {
  id?: string | number;
  status?: number;
  result?: string;
}
