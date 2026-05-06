<template>
  <div class="p-2">
    <el-card shadow="hover">
      <template #header>
        <div class="version-header">
          <div class="version-title">Windows 客户端版本</div>
          <el-button v-hasPermi="['im:windowsVersion:edit']" type="primary" plain :loading="generating" @click="generateConfig">
            自动生成
          </el-button>
        </div>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="150px" class="version-form">
        <el-form-item label="启用更新检查" prop="enabled">
          <el-switch v-model="form.enabled" active-text="启用" inactive-text="停用" />
        </el-form-item>
        <el-form-item label="最新版本" prop="latestVersion">
          <el-input v-model="form.latestVersion" clearable placeholder="例如 1.0.1" />
        </el-form-item>
        <el-form-item label="最低可用版本" prop="minVersion">
          <el-input v-model="form.minVersion" clearable placeholder="低于该版本将强制更新" />
        </el-form-item>
        <el-form-item label="强制更新" prop="forceUpdate">
          <el-switch v-model="form.forceUpdate" active-text="强制" inactive-text="可跳过" />
        </el-form-item>
        <el-form-item label="安装包地址" prop="setupUrl">
          <el-input v-model="form.setupUrl" clearable placeholder="https://im.zhixiaochaxun.com/..." />
        </el-form-item>
        <el-form-item label="EXE地址" prop="exeUrl">
          <el-input v-model="form.exeUrl" clearable placeholder="可选，直接下载 exe 时使用" />
        </el-form-item>
        <el-form-item label="SHA256" prop="sha256">
          <el-input v-model="form.sha256" clearable placeholder="可选，用于客户端校验" />
        </el-form-item>
        <el-form-item label="更新说明" prop="releaseNotes">
          <el-input v-model="form.releaseNotes" type="textarea" :rows="5" maxlength="1000" show-word-limit />
        </el-form-item>
        <el-form-item>
          <el-button @click="loadConfig">取消</el-button>
          <el-button v-hasPermi="['im:windowsVersion:edit']" type="primary" :loading="saving" @click="submitForm">确认</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup name="ImWindowsVersion" lang="ts">
import { generateWindowsVersion, getWindowsVersion, updateWindowsVersion } from '@/api/im/windowsVersion';
import { WindowsVersionConfig } from '@/api/im/windowsVersion/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const formRef = ref<ElFormInstance>();
const saving = ref(false);
const generating = ref(false);
const form = ref<WindowsVersionConfig>({
  enabled: false,
  latestVersion: '1.0.0',
  minVersion: '1.0.0',
  forceUpdate: false
});

const rules = {
  latestVersion: [{ required: true, message: '最新版本不能为空', trigger: 'blur' }],
  minVersion: [{ required: true, message: '最低可用版本不能为空', trigger: 'blur' }]
};

const loadConfig = async () => {
  const { data } = await getWindowsVersion();
  form.value = {
    enabled: false,
    latestVersion: '1.0.0',
    minVersion: '1.0.0',
    forceUpdate: false,
    ...(data || {})
  };
};

const generateConfig = async () => {
  generating.value = true;
  const { data } = await generateWindowsVersion().finally(() => (generating.value = false));
  form.value = {
    ...form.value,
    ...(data || {})
  };
  proxy?.$modal.msgSuccess('已自动生成发布信息');
};

const submitForm = () => {
  formRef.value?.validate(async (valid: boolean) => {
    if (!valid) return;
    saving.value = true;
    await updateWindowsVersion(form.value).finally(() => (saving.value = false));
    proxy?.$modal.msgSuccess('修改成功');
    loadConfig();
  });
};

onMounted(async () => {
  await loadConfig();
  if (!form.value.setupUrl && !form.value.exeUrl) {
    await generateConfig();
  }
});
</script>

<style scoped>
.version-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.version-title {
  font-size: 16px;
  font-weight: 600;
}

.version-form {
  max-width: 820px;
}
</style>
