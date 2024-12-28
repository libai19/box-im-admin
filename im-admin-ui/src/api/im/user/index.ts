import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { UserVO, UserBanDTO, UserUnbanDTO, UserQuery } from '@/api/im/user/types';

/**
 * 查询用户列表
 * @param query
 * @returns {*}
 */

export const listUser = (query?: UserQuery): AxiosPromise<UserVO[]> => {
  return request({
    url: '/im/user/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询用户详细
 * @param id
 */
export const getUser = (id: string | number): AxiosPromise<UserVO> => {
  return request({
    url: '/im/user/' + id,
    method: 'get'
  });
};


/**
 * 封禁用户
 * @param data
 */
export const ban = (data: UserBanDTO) => {
  return request({
    url: '/im/user/ban',
    method: 'put',
    data: data
  });
};


/**
 * 解封用户
 * @param data
 */
export const unban = (data: UserUnbanDTO) => {
  return request({
    url: '/im/user/unban',
    method: 'put',
    data: data
  });
};


export const findUserByName = (name?: String): AxiosPromise<UserVO[]> => {
  return request({
    url: '/im/user/findByName?name='+name,
    method: 'get'
  });
};

