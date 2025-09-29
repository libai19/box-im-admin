import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { PrivateMessageVO, PrivateMessageQuery } from '@/api/im/privateMessage/types';

/**
 * 查询私聊消息列表
 * @param query
 * @returns {*}
 */

export const listPrivateMessage = (query?: PrivateMessageQuery): AxiosPromise<PrivateMessageVO[]> => {
  return request({
    url: '/im/privateMessage/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询私聊消息详细
 * @param id
 */
export const getPrivateMessage = (id: string | number): AxiosPromise<PrivateMessageVO> => {
  return request({
    url: '/im/privateMessage/' + id,
    method: 'get'
  });
};



/**
 * 按天统计私聊消息量
 * @param days 统计天数
 */
export const getDailyMessageCount = (days?: number): AxiosPromise<any[]> => {
  return request({
    url: '/im/privateMessage/dailyCount',
    method: 'get',
    params: { days }
  });
};
