import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { SystemMessageVO, SystemMessageForm, SystemMessageQuery } from '@/api/im/systemMessage/types';

/**
 * 查询系统消息列表
 * @param query
 * @returns {*}
 */

export const listSystemMessage = (query?: SystemMessageQuery): AxiosPromise<SystemMessageVO[]> => {
  return request({
    url: '/im/systemMessage/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询系统消息详细
 * @param id
 */
export const getSystemMessage = (id: string | number): AxiosPromise<SystemMessageVO> => {
  return request({
    url: '/im/systemMessage/' + id,
    method: 'get'
  });
};

/**
 * 新增系统消息
 * @param data
 */
export const addSystemMessage = (data: SystemMessageForm) => {
  return request({
    url: '/im/systemMessage',
    method: 'post',
    data: data
  });
};

/**
 * 修改系统消息
 * @param data
 */
export const updateSystemMessage = (data: SystemMessageForm) => {
  return request({
    url: '/im/systemMessage',
    method: 'put',
    data: data
  });
};

/**
 * 删除系统消息
 * @param id
 */
export const delSystemMessage = (id: string | number | Array<string | number>) => {
  return request({
    url: '/im/systemMessage/' + id,
    method: 'delete'
  });
};

/**
 * 根据标题模糊查询系统消息
 * @param title
 */
export const findSystemMessageByTitle = (title?: String): AxiosPromise<SystemMessageVO[]> => {
  return request({
    url: '/im/systemMessage/findByTitle?title=' + title,
    method: 'get'
  });
};

