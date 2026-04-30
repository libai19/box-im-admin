<template>
  <div class="p-2">
    <el-card shadow="hover">
      <template #header>
        <div class="asr-title">语音识别接口</div>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="150px" class="asr-form">
        <el-form-item label="启用状态" prop="enabled">
          <el-switch v-model="form.enabled" active-text="启用" inactive-text="停用" />
        </el-form-item>
        <el-form-item label="识别平台" prop="provider">
          <el-select v-model="form.provider" placeholder="请选择识别平台" style="width: 360px" @change="onProviderChange">
            <el-option label="腾讯云 ASR" value="tencent" />
            <el-option label="阿里云智能语音交互" value="aliyun" />
            <el-option label="百度智能云语音识别" value="baidu" />
            <el-option label="讯飞开放平台" value="iflytek" />
            <el-option label="火山引擎语音识别" value="volcengine" />
          </el-select>
        </el-form-item>

        <template v-if="form.provider === 'tencent'">
          <el-form-item label="SecretId" prop="secretId">
            <el-input v-model="form.secretId" clearable placeholder="请输入腾讯云 SecretId" />
          </el-form-item>
          <el-form-item label="SecretKey" prop="secretKey">
            <el-input v-model="form.secretKey" clearable show-password placeholder="请输入腾讯云 SecretKey" />
          </el-form-item>
          <el-form-item label="地域" prop="region">
            <el-input v-model="form.region" clearable placeholder="默认 ap-guangzhou" />
          </el-form-item>
          <el-form-item label="识别模型" prop="engineModelType">
            <el-input v-model="form.engineModelType" clearable placeholder="默认 16k_zh" />
          </el-form-item>
        </template>

        <template v-if="form.provider === 'aliyun'">
          <el-form-item label="AccessKeyId" prop="accessKeyId">
            <el-input v-model="form.accessKeyId" clearable />
          </el-form-item>
          <el-form-item label="AccessKeySecret" prop="accessKeySecret">
            <el-input v-model="form.accessKeySecret" clearable show-password />
          </el-form-item>
          <el-form-item label="AppKey" prop="appKey">
            <el-input v-model="form.appKey" clearable />
          </el-form-item>
          <el-form-item label="地域" prop="region">
            <el-input v-model="form.region" clearable placeholder="如 cn-shanghai" />
          </el-form-item>
        </template>

        <template v-if="form.provider === 'baidu'">
          <el-form-item label="API Key" prop="apiKey">
            <el-input v-model="form.apiKey" clearable />
          </el-form-item>
          <el-form-item label="Secret Key" prop="secretKey">
            <el-input v-model="form.secretKey" clearable show-password />
          </el-form-item>
          <el-form-item label="语言模型 DevPid" prop="devPid">
            <el-input v-model="form.devPid" clearable placeholder="默认可留空" />
          </el-form-item>
        </template>

        <template v-if="form.provider === 'iflytek'">
          <el-form-item label="AppId" prop="appId">
            <el-input v-model="form.appId" clearable />
          </el-form-item>
          <el-form-item label="ApiKey" prop="apiKey">
            <el-input v-model="form.apiKey" clearable />
          </el-form-item>
          <el-form-item label="ApiSecret" prop="apiSecret">
            <el-input v-model="form.apiSecret" clearable show-password />
          </el-form-item>
        </template>

        <template v-if="form.provider === 'volcengine'">
          <el-form-item label="AccessKeyId" prop="accessKeyId">
            <el-input v-model="form.accessKeyId" clearable />
          </el-form-item>
          <el-form-item label="SecretAccessKey" prop="secretAccessKey">
            <el-input v-model="form.secretAccessKey" clearable show-password />
          </el-form-item>
          <el-form-item label="AppId" prop="appId">
            <el-input v-model="form.appId" clearable />
          </el-form-item>
          <el-form-item label="Cluster" prop="cluster">
            <el-input v-model="form.cluster" clearable />
          </el-form-item>
          <el-form-item label="地域" prop="region">
            <el-input v-model="form.region" clearable placeholder="如 cn-beijing" />
          </el-form-item>
        </template>

        <el-form-item>
          <el-button @click="loadConfig">取消</el-button>
          <el-button v-hasPermi="['im:asrConfig:edit']" type="primary" :loading="saving" @click="submitForm">确认</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup name="ImAsrConfig" lang="ts">
import { getAsrConfig, updateAsrConfig } from '@/api/im/asrConfig';
import { AsrConfig } from '@/api/im/asrConfig/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const formRef = ref<ElFormInstance>();
const saving = ref(false);
const form = ref<AsrConfig>({
  enabled: false,
  provider: 'tencent',
  region: 'ap-guangzhou',
  engineModelType: '16k_zh'
});

const rules = {
  provider: [{ required: true, message: '请选择识别平台', trigger: 'change' }]
};

const defaults: Record<string, Partial<AsrConfig>> = {
  tencent: { region: 'ap-guangzhou', engineModelType: '16k_zh' },
  aliyun: { region: 'cn-shanghai' },
  baidu: {},
  iflytek: {},
  volcengine: { region: 'cn-beijing' }
};

const loadConfig = async () => {
  const { data } = await getAsrConfig();
  form.value = {
    enabled: false,
    provider: 'tencent',
    region: 'ap-guangzhou',
    engineModelType: '16k_zh',
    ...(data || {})
  };
};

const onProviderChange = () => {
  form.value = {
    ...form.value,
    ...(defaults[form.value.provider] || {})
  };
};

const submitForm = () => {
  formRef.value?.validate(async (valid: boolean) => {
    if (!valid) return;
    saving.value = true;
    await updateAsrConfig(form.value).finally(() => (saving.value = false));
    proxy?.$modal.msgSuccess('修改成功');
    loadConfig();
  });
};

onMounted(loadConfig);
</script>

<style scoped>
.asr-title {
  font-size: 16px;
  font-weight: 600;
}

.asr-form {
  max-width: 760px;
}
</style>
