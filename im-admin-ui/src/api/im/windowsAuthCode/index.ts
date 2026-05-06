import request from '@/utils/request';
import { WindowsAuthCodeConfig } from './types';

export const getWindowsAuthCode = () => {
  return request({
    url: '/im/windows-auth-code',
    method: 'get'
  });
};

export const refreshWindowsAuthCode = () => {
  return request<WindowsAuthCodeConfig>({
    url: '/im/windows-auth-code/refresh',
    method: 'post'
  });
};
