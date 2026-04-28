<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="发起用户" prop="userId">
              <im-user-select v-model="queryParams.userId" />
            </el-form-item>
            <el-form-item label="处理状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 180px">
                <el-option label="未处理" :value="0" />
                <el-option label="处理中" :value="1" />
                <el-option label="已处理" :value="2" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
            <el-form-item label="投诉提示" class="notice-template-item">
              <el-input v-model="noticeTemplate" maxlength="20" show-word-limit clearable style="width: 300px" />
              <el-button v-hasPermi="['im:complaint:handle']" class="ml-2" type="primary" :loading="noticeSaving" @click="submitNoticeTemplate">
                确认
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="hover">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <right-toolbar v-model:showSearch="showSearch" @query-table="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table v-loading="loading" :data="complaintList">
        <el-table-column label="发起用户" align="center" prop="userName" min-width="120" />
        <el-table-column label="投诉对象" align="center" prop="targetName" min-width="140" />
        <el-table-column label="投诉对象类型" align="center" prop="targetType" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.targetType === 2 ? 'primary' : 'danger'">{{ scope.row.targetType === 2 ? '群' : '用户' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="投诉原因" align="center" prop="type" width="130">
          <template #default="scope">{{ reasonLabel(scope.row.type) }}</template>
        </el-table-column>
        <el-table-column label="投诉时间" align="center" prop="createdTime" width="180">
          <template #default="scope">{{ parseTime(scope.row.createdTime) }}</template>
        </el-table-column>
        <el-table-column label="处理状态" align="center" prop="status" width="110">
          <template #default="scope">
            <el-tag :type="scope.row.status === 2 ? 'success' : 'danger'">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="150">
          <template #default="scope">
            <el-button v-hasPermi="['im:complaint:query']" link type="primary" @click="handleDetail(scope.row)">详情</el-button>
            <el-button v-hasPermi="['im:complaint:handle']" link type="primary" @click="handleProcess(scope.row)">处理</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="640px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="发起用户">{{ current?.userName }}</el-descriptions-item>
        <el-descriptions-item label="投诉对象">{{ current?.targetName }}</el-descriptions-item>
        <el-descriptions-item label="对象类型">{{ current?.targetType === 2 ? '群' : '用户' }}</el-descriptions-item>
        <el-descriptions-item label="投诉原因">{{ reasonLabel(current?.type) }}</el-descriptions-item>
        <el-descriptions-item label="投诉时间">{{ parseTime(current?.createdTime) }}</el-descriptions-item>
        <el-descriptions-item label="处理状态">{{ statusLabel(current?.status) }}</el-descriptions-item>
        <el-descriptions-item label="投诉内容" :span="2">{{ current?.content }}</el-descriptions-item>
        <el-descriptions-item v-if="current?.result" label="处理结果" :span="2">{{ current?.result }}</el-descriptions-item>
      </el-descriptions>
      <el-form v-if="dialog.mode === 'process'" ref="handleFormRef" :model="handleForm" :rules="rules" label-width="80px" class="mt-4">
        <el-form-item label="处理状态" prop="status">
          <el-radio-group v-model="handleForm.status">
            <el-radio :value="1">处理中</el-radio>
            <el-radio :value="2">已处理</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理结果" prop="result">
          <el-input v-model="handleForm.result" type="textarea" :rows="4" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button v-if="dialog.mode === 'process'" :loading="buttonLoading" type="primary" @click="submitHandle">确定</el-button>
        <el-button @click="dialog.visible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ImComplaint" lang="ts">
import { getComplaint, getComplaintNoticeTemplate, handleComplaint, listComplaint, updateComplaintNoticeTemplate } from '@/api/im/complaint';
import { ComplaintHandleForm, ComplaintQuery, ComplaintVO } from '@/api/im/complaint/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const complaintList = ref<ComplaintVO[]>([]);
const loading = ref(true);
const buttonLoading = ref(false);
const noticeSaving = ref(false);
const showSearch = ref(true);
const total = ref(0);
const current = ref<ComplaintVO>();
const noticeTemplate = ref('');
const queryFormRef = ref<ElFormInstance>();
const handleFormRef = ref<ElFormInstance>();
const dialog = reactive({ visible: false, title: '', mode: 'detail' });

const queryParams = ref<ComplaintQuery>({ pageNum: 1, pageSize: 10 });
const handleForm = ref<ComplaintHandleForm>({ id: undefined, status: 2, result: '' });
const rules = { result: [{ required: true, message: '处理结果不能为空', trigger: 'blur' }] };

const reasonLabel = (type?: number) => ({ 1: '垃圾信息', 2: '骚扰', 3: '诈骗', 4: '其他' }[type || 0] || '其他');
const statusLabel = (status?: number) => ({ 0: '未处理', 1: '处理中', 2: '已处理' }[status || 0] || '未处理');

const getList = async () => {
  loading.value = true;
  const res = await listComplaint(queryParams.value);
  complaintList.value = res.rows;
  total.value = res.total;
  loading.value = false;
};

const getNoticeTemplate = async () => {
  const { data } = await getComplaintNoticeTemplate();
  noticeTemplate.value = data || '';
};

const submitNoticeTemplate = async () => {
  const value = noticeTemplate.value.trim();
  if (!value) {
    proxy?.$modal.msgWarning('投诉提示不能为空');
    return;
  }
  if (value.length > 20) {
    proxy?.$modal.msgWarning('投诉提示不能超过20字');
    return;
  }
  noticeSaving.value = true;
  await updateComplaintNoticeTemplate(value).finally(() => (noticeSaving.value = false));
  proxy?.$modal.msgSuccess('修改成功');
  noticeTemplate.value = value;
};

const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};

const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
};

const handleDetail = async (row: ComplaintVO) => {
  const { data } = await getComplaint(row.id);
  current.value = data;
  dialog.mode = 'detail';
  dialog.title = '投诉详情';
  dialog.visible = true;
};

const handleProcess = async (row: ComplaintVO) => {
  const { data } = await getComplaint(row.id);
  current.value = data;
  handleForm.value = { id: data.id, status: data.status === 2 ? 2 : 1, result: data.result || '' };
  dialog.mode = 'process';
  dialog.title = '处理投诉';
  dialog.visible = true;
};

const submitHandle = () => {
  handleFormRef.value?.validate(async (valid: boolean) => {
    if (!valid) return;
    buttonLoading.value = true;
    await handleComplaint(handleForm.value).finally(() => (buttonLoading.value = false));
    proxy?.$modal.msgSuccess('处理成功');
    dialog.visible = false;
    getList();
  });
};

onMounted(() => {
  getList();
  getNoticeTemplate();
});
</script>

<style scoped>
.notice-template-item {
  margin-left: 12px;
}
</style>
