import request from '@/utils/request';
import { WindowsVersionConfig } from './types';

export const getWindowsVersion = () => {
  return request({
    url: '/im/windows-version',
    method: 'get'
  });
};

export const generateWindowsVersion = () => {
  return request({
    url: '/im/windows-version/generate',
    method: 'get'
  });
};

export const updateWindowsVersion = (data: WindowsVersionConfig) => {
  return request({
    url: '/im/windows-version',
    method: 'put',
    data
  });
};
