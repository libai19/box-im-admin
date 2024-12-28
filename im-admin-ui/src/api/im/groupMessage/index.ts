import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { GroupMessageVO, GroupMessageQuery } from '@/api/im/groupMessage/types';

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

