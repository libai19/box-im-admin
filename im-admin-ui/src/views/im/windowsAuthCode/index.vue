<template>
  <div class="p-2">
    <el-card shadow="hover">
      <template #header>
        <div class="auth-header">
          <div>
            <div class="auth-title">今日授权码</div>
            <div class="auth-subtitle">Windows 桌面端登录二次校验</div>
          </div>
          <el-button v-hasPermi="['im:windowsAuthCode:edit']" type="primary" :loading="refreshing" @click="handleRefresh">
            立即刷新
          </el-button>
        </div>
      </template>

      <el-skeleton :loading="loading" animated>
        <div class="auth-layout">
          <div class="code-panel">
            <div class="code-label">今日可用授权码</div>
            <div class="code-value">{{ form.code || '------' }}</div>
            <div class="code-actions">
              <el-button plain @click="copyCode">复制授权码</el-button>
              <el-tag :type="form.enabled ? 'success' : 'info'">{{ form.enabled ? '已启用' : '已停用' }}</el-tag>
            </div>
          </div>

          <el-descriptions :column="1" border class="auth-meta">
            <el-descriptions-item label="有效日期">{{ form.codeDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="生成时间">{{ form.generatedAt || '-' }}</el-descriptions-item>
            <el-descriptions-item label="更新规则">每天 00:00 自动生成新码</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-skeleton>
    </el-card>
  </div>
</template>

<script setup name="ImWindowsAuthCode" lang="ts">
import { getWindowsAuthCode, refreshWindowsAuthCode } from '@/api/im/windowsAuthCode';
import { WindowsAuthCodeConfig } from '@/api/im/windowsAuthCode/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const loading = ref(false);
const refreshing = ref(false);
const form = ref<WindowsAuthCodeConfig>({
  enabled: true,
  code: '',
  codeDate: '',
  generatedAt: ''
});

const loadConfig = async () => {
  loading.value = true;
  const { data } = await getWindowsAuthCode().finally(() => (loading.value = false));
  form.value = {
    enabled: true,
    code: '',
    codeDate: '',
    generatedAt: '',
    ...(data || {})
  };
};

const handleRefresh = async () => {
  refreshing.value = true;
  const { data } = await refreshWindowsAuthCode().finally(() => (refreshing.value = false));
  form.value = {
    ...form.value,
    ...(data || {})
  };
  proxy?.$modal.msgSuccess('今日授权码已刷新');
};

const copyCode = async () => {
  if (!form.value.code) return;
  await navigator.clipboard.writeText(form.value.code);
  proxy?.$modal.msgSuccess('已复制');
};

onMounted(loadConfig);
</script>

<style scoped>
.auth-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.auth-title {
  font-size: 16px;
  font-weight: 600;
}

.auth-subtitle {
  margin-top: 4px;
  color: #7a8aa0;
  font-size: 13px;
}

.auth-layout {
  display: grid;
  grid-template-columns: 300px minmax(360px, 1fr);
  gap: 18px;
  max-width: 880px;
}

.code-panel {
  padding: 22px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  background: #f8fbff;
}

.code-label {
  color: #606266;
  font-size: 13px;
}

.code-value {
  margin-top: 10px;
  color: #1f2e40;
  font-family: Consolas, Monaco, monospace;
  font-size: 44px;
  font-weight: 700;
  letter-spacing: 0;
  line-height: 1.1;
}

.code-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 18px;
}

.auth-meta {
  min-width: 0;
}

@media (max-width: 760px) {
  .auth-layout {
    grid-template-columns: 1fr;
  }
}
</style>
