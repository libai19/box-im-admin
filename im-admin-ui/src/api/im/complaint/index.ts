import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ComplaintHandleForm, ComplaintQuery, ComplaintVO } from './types';

export const listComplaint = (query?: ComplaintQuery): AxiosPromise<ComplaintVO[]> => {
  return request({
    url: '/im/complaint/list',
    method: 'get',
    params: query
  });
};

export const getComplaint = (id: string | number): AxiosPromise<ComplaintVO> => {
  return request({
    url: '/im/complaint/' + id,
    method: 'get'
  });
};

export const handleComplaint = (data: ComplaintHandleForm) => {
  return request({
    url: '/im/complaint/handle',
    method: 'put',
    data
  });
};

export const getComplaintNoticeTemplate = () => {
  return request({
    url: '/im/complaint/notice-template',
    method: 'get'
  });
};

export const updateComplaintNoticeTemplate = (value: string) => {
  return request({
    url: '/im/complaint/notice-template',
    method: 'put',
    data: { value }
  });
};

export const delComplaint = (id: string | number | Array<string | number>) => {
  return request({
    url: '/im/complaint/' + id,
    method: 'delete'
  });
};
