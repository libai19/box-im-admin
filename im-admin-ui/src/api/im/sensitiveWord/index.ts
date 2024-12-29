import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { SensitiveWordVO, SensitiveWordForm, SensitiveWordQuery } from '@/api/im/sensitiveWord/types';

/**
 * 查询敏感词列表
 * @param query
 * @returns {*}
 */

export const listSensitiveWord = (query?: SensitiveWordQuery): AxiosPromise<SensitiveWordVO[]> => {
  return request({
    url: '/im/sensitiveWord/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询敏感词详细
 * @param id
 */
export const getSensitiveWord = (id: string | number): AxiosPromise<SensitiveWordVO> => {
  return request({
    url: '/im/sensitiveWord/' + id,
    method: 'get'
  });
};

/**
 * 新增敏感词
 * @param data
 */
export const addSensitiveWord = (data: SensitiveWordForm) => {
  return request({
    url: '/im/sensitiveWord',
    method: 'post',
    data: data
  });
};

/**
 * 修改敏感词
 * @param data
 */
export const updateSensitiveWord = (data: SensitiveWordForm) => {
  return request({
    url: '/im/sensitiveWord',
    method: 'put',
    data: data
  });
};

/**
 * 删除敏感词
 * @param id
 */
export const delSensitiveWord = (id: string | number | Array<string | number>) => {
  return request({
    url: '/im/sensitiveWord/' + id,
    method: 'delete'
  });
};


/**
 * 开启/关闭敏感词
 * @param id
 */
export const enableSensitiveWord = (data: SensitiveWordForm) => {
  return request({
    url: '/im/sensitiveWord/enable',
    method: 'put',
    data: data
  });
};
