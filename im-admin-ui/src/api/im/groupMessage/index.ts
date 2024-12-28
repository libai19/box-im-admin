import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { GroupMessageVO, GroupMessageForm, GroupMessageQuery } from '@/api/im/groupMessage/types';

/**
 * 查询群消息列表
 * @param query
 * @returns {*}
 */

export const listGroupMessage = (query?: GroupMessageQuery): AxiosPromise<GroupMessageVO[]> => {
  return request({
    url: '/im/groupMessage/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询群消息详细
 * @param id
 */
export const getGroupMessage = (id: string | number): AxiosPromise<GroupMessageVO> => {
  return request({
    url: '/im/groupMessage/' + id,
    method: 'get'
  });
};

/**
 * 新增群消息
 * @param data
 */
export const addGroupMessage = (data: GroupMessageForm) => {
  return request({
    url: '/im/groupMessage',
    method: 'post',
    data: data
  });
};

/**
 * 修改群消息
 * @param data
 */
export const updateGroupMessage = (data: GroupMessageForm) => {
  return request({
    url: '/im/groupMessage',
    method: 'put',
    data: data
  });
};

/**
 * 删除群消息
 * @param id
 */
export const delGroupMessage = (id: string | number | Array<string | number>) => {
  return request({
    url: '/im/groupMessage/' + id,
    method: 'delete'
  });
};
