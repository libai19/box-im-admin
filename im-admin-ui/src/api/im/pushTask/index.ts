import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { PushTaskQuery, PushTaskVO } from './types';

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
