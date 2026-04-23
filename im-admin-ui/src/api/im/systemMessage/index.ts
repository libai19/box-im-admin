import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { SystemMessageForm, SystemMessageQuery, SystemMessageVO } from './types';

export const listSystemMessage = (query?: SystemMessageQuery): AxiosPromise<SystemMessageVO[]> => {
  return request({
    url: '/im/systemMessage/list',
    method: 'get',
    params: query
  });
};

export const getSystemMessage = (id: string | number): AxiosPromise<SystemMessageVO> => {
  return request({
    url: '/im/systemMessage/' + id,
    method: 'get'
  });
};

export const addSystemMessage = (data: SystemMessageForm) => {
  return request({
    url: '/im/systemMessage',
    method: 'post',
    data
  });
};

export const updateSystemMessage = (data: SystemMessageForm) => {
  return request({
    url: '/im/systemMessage',
    method: 'put',
    data
  });
};

export const pushSystemMessage = (id: string | number) => {
  return request({
    url: '/im/systemMessage/push/' + id,
    method: 'post'
  });
};

export const delSystemMessage = (id: string | number | Array<string | number>) => {
  return request({
    url: '/im/systemMessage/' + id,
    method: 'delete'
  });
};
