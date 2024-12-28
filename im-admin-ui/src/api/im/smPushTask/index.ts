import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { SmPushTaskVO, SmPushTaskForm, SmPushTaskQuery } from '@/api/im/smPushTask/types';

/**
 * 查询系统消息推送任务列表
 * @param query
 * @returns {*}
 */

export const listSmPushTask = (query?: SmPushTaskQuery): AxiosPromise<SmPushTaskVO[]> => {
  return request({
    url: '/im/smPushTask/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询系统消息推送任务详细
 * @param id
 */
export const getSmPushTask = (id: string | number): AxiosPromise<SmPushTaskVO> => {
  return request({
    url: '/im/smPushTask/' + id,
    method: 'get'
  });
};

/**
 * 新增系统消息推送任务
 * @param data
 */
export const addSmPushTask = (data: SmPushTaskForm) => {
  return request({
    url: '/im/smPushTask',
    method: 'post',
    data: data
  });
};

/**
 * 修改系统消息推送任务
 * @param data
 */
export const updateSmPushTask = (data: SmPushTaskForm) => {
  return request({
    url: '/im/smPushTask',
    method: 'put',
    data: data
  });
};

/**
 * 删除系统消息推送任务
 * @param id
 */
export const delSmPushTask = (id: string | number | Array<string | number>) => {
  return request({
    url: '/im/smPushTask/' + id,
    method: 'delete'
  });
};
