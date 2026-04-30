import request from '@/utils/request';
import { AsrConfig } from './types';

export const getAsrConfig = () => {
  return request({
    url: '/im/asr-config',
    method: 'get'
  });
};

export const updateAsrConfig = (data: AsrConfig) => {
  return request({
    url: '/im/asr-config',
    method: 'put',
    data
  });
};
