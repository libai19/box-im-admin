export interface AsrConfig {
  enabled: boolean;
  provider: string;
  secretId?: string;
  secretKey?: string;
  region?: string;
  engineModelType?: string;
  appKey?: string;
  appId?: string;
  apiKey?: string;
  apiSecret?: string;
  accessKeyId?: string;
  accessKeySecret?: string;
  secretAccessKey?: string;
  cluster?: string;
  devPid?: string;
}
