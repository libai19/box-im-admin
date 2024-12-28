import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { GroupMemberVO, GroupMemberQuery } from '@/api/im/groupMember/types';

/**
 * 查询群成员列表
 * @param query
 * @returns {*}
 */

export const listGroupMember = (query?: GroupMemberQuery): AxiosPromise<GroupMemberVO[]> => {
  return request({
    url: '/im/group/member/list',
    method: 'get',
    params: query
  });
};

