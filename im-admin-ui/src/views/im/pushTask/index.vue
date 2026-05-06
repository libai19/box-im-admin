<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="推送消息" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入系统消息" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="推送状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 160px">
                <el-option label="待发送" :value="0" />
                <el-option label="已发送" :value="1" />
                <el-option label="失败" :value="2" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="hover">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5"><el-button v-hasPermi="['im:pushTask:add']" type="primary" plain icon="Plus" @click="handleAdd">新增推送</el-button></el-col>
          <el-col :span="1.5"><el-button v-hasPermi="['im:pushTask:remove']" type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()">删除</el-button></el-col>
          <right-toolbar v-model:showSearch="showSearch" @query-table="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table v-loading="loading" :data="taskList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="推送消息" align="center" prop="title" min-width="180" />
        <el-table-column label="推送时间" align="center" prop="pushTime" width="180"><template #default="scope">{{ parseTime(scope.row.pushTime) }}</template></el-table-column>
        <el-table-column label="推送状态" align="center" width="110">
          <template #default="scope"><el-tag :type="scope.row.status === 1 ? 'success' : scope.row.status === 2 ? 'danger' : 'info'">{{ statusLabel(scope.row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="接收用户" align="center" min-width="120">
          <template #default="scope">{{ scope.row.targetType === 1 ? `共${targetCount(scope.row.targetIds)}个用户` : '全体用户' }}</template>
        </el-table-column>
        <el-table-column label="创建者" align="center" prop="creatorName" width="120" />
        <el-table-column label="创建时间" align="center" prop="createdTime" width="180"><template #default="scope">{{ parseTime(scope.row.createdTime) }}</template></el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="150">
          <template #default="scope">
            <el-button v-hasPermi="['im:pushTask:remove']" link type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-button v-hasPermi="['im:pushTask:resend']" link type="primary" @click="handleResend(scope.row)">再次推送</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>

    <el-dialog v-model="dialog.visible" title="新增推送任务" width="720px" append-to-body>
      <el-form ref="taskFormRef" :model="form" :rules="rules" label-width="96px">
        <el-form-item label="系统消息" prop="messageId">
          <el-select v-model="form.messageId" filterable remote clearable placeholder="请输入标题搜索系统消息" :remote-method="loadMessageOptions" :loading="messageLoading" style="width: 100%">
            <el-option v-for="item in messageOptions" :key="item.id" :label="item.title" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="接收用户" prop="targetType">
          <el-radio-group v-model="form.targetType">
            <el-radio :value="0">全体用户</el-radio>
            <el-radio :value="1">指定用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.targetType === 1" label="指定用户" prop="targetIds">
          <im-user-select v-model="selectedTargetIds" multiple placeholder="请选择接收用户" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">立即推送</el-button>
        <el-button @click="dialog.visible = false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ImPushTask" lang="ts">
import { addPushTask, delPushTask, listPushTask, resendPushTask } from '@/api/im/pushTask';
import { PushTaskForm, PushTaskQuery, PushTaskVO } from '@/api/im/pushTask/types';
import { listSystemMessage } from '@/api/im/systemMessage';
import { SystemMessageVO } from '@/api/im/systemMessage/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const taskList = ref<PushTaskVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const buttonLoading = ref(false);
const messageLoading = ref(false);
const ids = ref<Array<string | number>>([]);
const multiple = ref(true);
const total = ref(0);
const queryFormRef = ref<ElFormInstance>();
const taskFormRef = ref<ElFormInstance>();
const queryParams = ref<PushTaskQuery>({ pageNum: 1, pageSize: 10, title: '', status: undefined });
const dialog = reactive({ visible: false });
const form = ref<PushTaskForm>({ messageId: undefined, targetType: 0, targetIds: '' });
const selectedTargetIds = ref<Array<string | number>>([]);
const messageOptions = ref<SystemMessageVO[]>([]);

const statusLabel = (status?: number) => ({ 0: '待发送', 1: '已发送', 2: '失败' }[status || 0] || '待发送');
const targetCount = (targetIds?: string) => (targetIds ? targetIds.split(',').filter(Boolean).length : 0);
const rules = {
  messageId: [{ required: true, message: '请选择系统消息', trigger: 'change' }],
  targetIds: [
    {
      validator: (_rule: any, _value: string, callback: (error?: Error) => void) => {
        if (form.value.targetType === 1 && selectedTargetIds.value.length === 0) {
          callback(new Error('请选择接收用户'));
          return;
        }
        callback();
      },
      trigger: 'change'
    }
  ]
};

const getList = async () => {
  loading.value = true;
  const res = await listPushTask(queryParams.value);
  taskList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};

const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};

const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
};

const handleSelectionChange = (selection: PushTaskVO[]) => {
  ids.value = selection.map((item) => item.id);
  multiple.value = !selection.length;
};

const loadMessageOptions = async (title?: string) => {
  messageLoading.value = true;
  try {
    const res = await listSystemMessage({ pageNum: 1, pageSize: 20, title });
    messageOptions.value = res.rows;
  } finally {
    messageLoading.value = false;
  }
};

const handleAdd = () => {
  form.value = { messageId: undefined, targetType: 0, targetIds: '' };
  selectedTargetIds.value = [];
  taskFormRef.value?.resetFields();
  loadMessageOptions();
  dialog.visible = true;
};

const submitForm = () => {
  form.value.targetIds = form.value.targetType === 1 ? selectedTargetIds.value.join(',') : '';
  taskFormRef.value?.validate(async (valid: boolean) => {
    if (!valid) return;
    buttonLoading.value = true;
    await addPushTask(form.value).finally(() => (buttonLoading.value = false));
    proxy?.$modal.msgSuccess('推送成功');
    dialog.visible = false;
    getList();
  });
};

const handleResend = async (row: PushTaskVO) => {
  await proxy?.$modal.confirm('确认再次推送 "' + row.title + '" 吗？');
  await resendPushTask(row.id);
  proxy?.$modal.msgSuccess('推送成功');
  getList();
};

const handleDelete = async (row?: PushTaskVO) => {
  const taskIds = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除推送任务编号为 "' + taskIds + '" 的数据项？');
  await delPushTask(taskIds);
  proxy?.$modal.msgSuccess('删除成功');
  getList();
};

onMounted(getList);
</script>
