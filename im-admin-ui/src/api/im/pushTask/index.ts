import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { PushTaskForm, PushTaskQuery, PushTaskVO } from './types';

export const listPushTask = (query?: PushTaskQuery): AxiosPromise<PushTaskVO[]> => {
  return request({
    url: '/im/pushTask/list',
    method: 'get',
    params: query
  });
};

export const getPushTask = (id: string | number): AxiosPromise<PushTaskVO> => {
  return request({
    url: '/im/pushTask/' + id,
    method: 'get'
  });
};

export const addPushTask = (data: PushTaskForm) => {
  return request({
    url: '/im/pushTask',
    method: 'post',
    data
  });
};

export const resendPushTask = (id: string | number) => {
  return request({
    url: '/im/pushTask/resend/' + id,
    method: 'post'
  });
};

export const delPushTask = (id: string | number | Array<string | number>) => {
  return request({
    url: '/im/pushTask/' + id,
    method: 'delete'
  });
};
