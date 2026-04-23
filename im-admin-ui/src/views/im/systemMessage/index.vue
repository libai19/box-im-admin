<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
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
          <el-col :span="1.5"><el-button v-hasPermi="['im:systemMessage:add']" type="primary" plain icon="Plus" @click="handleAdd">新增</el-button></el-col>
          <el-col :span="1.5"><el-button v-hasPermi="['im:systemMessage:remove']" type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()">删除</el-button></el-col>
          <right-toolbar v-model:showSearch="showSearch" @query-table="getList"></right-toolbar>
        </el-row>
      </template>
      <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="标题" align="center" prop="title" min-width="160" />
        <el-table-column label="封面" align="center" width="140">
          <template #default="scope"><image-preview v-if="scope.row.cover" :src="scope.row.cover" :width="90" :height="52" /></template>
        </el-table-column>
        <el-table-column label="内容类型" align="center" width="120">
          <template #default="scope"><el-tag>{{ scope.row.contentType === 2 ? '外部链接' : '富文本' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope"><el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{ scope.row.status === 1 ? '已推送' : '草稿' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="创建者" align="center" prop="creatorName" width="120" />
        <el-table-column label="操作" align="center" fixed="right" width="180">
          <template #default="scope">
            <el-button v-hasPermi="['im:systemMessage:push']" link type="primary" @click="handlePush(scope.row)">推送</el-button>
            <el-button v-hasPermi="['im:systemMessage:edit']" link type="primary" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button v-hasPermi="['im:systemMessage:remove']" link type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>

    <el-dialog v-model="dialog.visible" :title="dialog.title" width="980px" append-to-body>
      <el-form ref="messageFormRef" :model="form" :rules="rules" label-width="92px">
        <el-form-item label="标题" prop="title"><el-input v-model="form.title" placeholder="请输入标题" /></el-form-item>
        <el-form-item label="封面" prop="cover"><image-upload v-model="form.cover" :limit="1" /></el-form-item>
        <el-form-item label="简介" prop="summary"><el-input v-model="form.summary" placeholder="请输入简介" /></el-form-item>
        <el-form-item label="类型" prop="contentType">
          <el-radio-group v-model="form.contentType">
            <el-radio :value="1">富文本</el-radio>
            <el-radio :value="2">外部链接</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.contentType === 2" label="链接地址" prop="linkUrl"><el-input v-model="form.linkUrl" placeholder="请输入外部链接" /></el-form-item>
        <el-form-item v-else label="富文本内容" prop="content"><editor v-model="form.content" :min-height="260" /></el-form-item>
        <el-form-item label="接收用户" prop="targetType">
          <el-radio-group v-model="form.targetType">
            <el-radio :value="0">全体用户</el-radio>
            <el-radio :value="1">指定用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.targetType === 1" label="用户ID" prop="targetIds">
          <el-input v-model="form.targetIds" placeholder="多个用户ID用英文逗号分隔" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确定</el-button>
        <el-button @click="dialog.visible = false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ImSystemMessage" lang="ts">
import { addSystemMessage, delSystemMessage, getSystemMessage, listSystemMessage, pushSystemMessage, updateSystemMessage } from '@/api/im/systemMessage';
import { SystemMessageForm, SystemMessageQuery, SystemMessageVO } from '@/api/im/systemMessage/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const messageList = ref<SystemMessageVO[]>([]);
const loading = ref(true);
const buttonLoading = ref(false);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const multiple = ref(true);
const total = ref(0);
const queryFormRef = ref<ElFormInstance>();
const messageFormRef = ref<ElFormInstance>();
const dialog = reactive({ visible: false, title: '' });
const queryParams = ref<SystemMessageQuery>({ pageNum: 1, pageSize: 10, title: '' });
const initForm: SystemMessageForm = { title: '', cover: '', summary: '', contentType: 1, content: '', linkUrl: '', type: 1, targetType: 0, targetIds: '', status: 0 };
const form = ref<SystemMessageForm>({ ...initForm });
const rules = {
  title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
  contentType: [{ required: true, message: '内容类型不能为空', trigger: 'change' }]
};

const getList = async () => {
  loading.value = true;
  const res = await listSystemMessage(queryParams.value);
  messageList.value = res.rows;
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

const handleSelectionChange = (selection: SystemMessageVO[]) => {
  ids.value = selection.map((item) => item.id);
  multiple.value = !selection.length;
};

const reset = () => {
  form.value = { ...initForm };
  messageFormRef.value?.resetFields();
};

const handleAdd = () => {
  reset();
  dialog.title = '新增系统消息';
  dialog.visible = true;
};

const handleUpdate = async (row: SystemMessageVO) => {
  reset();
  const { data } = await getSystemMessage(row.id);
  form.value = data;
  dialog.title = '修改系统消息';
  dialog.visible = true;
};

const submitForm = () => {
  messageFormRef.value?.validate(async (valid: boolean) => {
    if (!valid) return;
    buttonLoading.value = true;
    form.value.id ? await updateSystemMessage(form.value).finally(() => (buttonLoading.value = false)) : await addSystemMessage(form.value).finally(() => (buttonLoading.value = false));
    proxy?.$modal.msgSuccess('保存成功');
    dialog.visible = false;
    getList();
  });
};

const handlePush = async (row: SystemMessageVO) => {
  await proxy?.$modal.confirm('确认推送系统消息 "' + row.title + '" 吗？');
  await pushSystemMessage(row.id);
  proxy?.$modal.msgSuccess('推送成功');
  getList();
};

const handleDelete = async (row?: SystemMessageVO) => {
  const messageIds = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除系统消息编号为 "' + messageIds + '" 的数据项？');
  await delSystemMessage(messageIds);
  proxy?.$modal.msgSuccess('删除成功');
  getList();
};

onMounted(getList);
</script>
