<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="系统消息id" prop="messageId">
              <el-input v-model="queryParams.messageId" placeholder="请输入系统消息id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="发送序列号" prop="seqNo">
              <el-input v-model="queryParams.seqNo" placeholder="请输入发送序列号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="推送时间" prop="sendTime">
              <el-date-picker clearable
                v-model="queryParams.sendTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择推送时间"
              />
            </el-form-item>
            <el-form-item label="是否发送给全体用户" prop="sendToAll">
              <el-input v-model="queryParams.sendToAll" placeholder="请输入是否发送给全体用户" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="接收用户id,逗号分隔,send_to_all为false时有效" prop="recvIds">
              <el-input v-model="queryParams.recvIds" placeholder="请输入接收用户id,逗号分隔,send_to_all为false时有效" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="删除标识  0：正常   1：已删除" prop="deleted">
              <el-input v-model="queryParams.deleted" placeholder="请输入删除标识  0：正常   1：已删除" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="创建者" prop="creator">
              <el-input v-model="queryParams.creator" placeholder="请输入创建者" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="更新者" prop="updater">
              <el-input v-model="queryParams.updater" placeholder="请输入更新者" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['im:smPushTask:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['im:smPushTask:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['im:smPushTask:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['im:smPushTask:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="smPushTaskList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="id" align="center" prop="id" v-if="true" />
        <el-table-column label="系统消息id" align="center" prop="messageId" />
        <el-table-column label="发送序列号" align="center" prop="seqNo" />
        <el-table-column label="推送时间" align="center" prop="sendTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态 1:待发送 2:发送中 3:已发送 4:已取消" align="center" prop="status" />
        <el-table-column label="是否发送给全体用户" align="center" prop="sendToAll" />
        <el-table-column label="接收用户id,逗号分隔,send_to_all为false时有效" align="center" prop="recvIds" />
        <el-table-column label="删除标识  0：正常   1：已删除" align="center" prop="deleted" />
        <el-table-column label="创建者" align="center" prop="creator" />
        <el-table-column label="更新者" align="center" prop="updater" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['im:smPushTask:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['im:smPushTask:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改系统消息推送任务对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="smPushTaskFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="系统消息id" prop="messageId">
          <el-input v-model="form.messageId" placeholder="请输入系统消息id" />
        </el-form-item>
        <el-form-item label="发送序列号" prop="seqNo">
          <el-input v-model="form.seqNo" placeholder="请输入发送序列号" />
        </el-form-item>
        <el-form-item label="推送时间" prop="sendTime">
          <el-date-picker clearable
            v-model="form.sendTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择推送时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="是否发送给全体用户" prop="sendToAll">
          <el-input v-model="form.sendToAll" placeholder="请输入是否发送给全体用户" />
        </el-form-item>
        <el-form-item label="接收用户id,逗号分隔,send_to_all为false时有效" prop="recvIds">
          <el-input v-model="form.recvIds" placeholder="请输入接收用户id,逗号分隔,send_to_all为false时有效" />
        </el-form-item>
        <el-form-item label="删除标识  0：正常   1：已删除" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入删除标识  0：正常   1：已删除" />
        </el-form-item>
        <el-form-item label="创建者" prop="creator">
          <el-input v-model="form.creator" placeholder="请输入创建者" />
        </el-form-item>
        <el-form-item label="更新者" prop="updater">
          <el-input v-model="form.updater" placeholder="请输入更新者" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SmPushTask" lang="ts">
import { listSmPushTask, getSmPushTask, delSmPushTask, addSmPushTask, updateSmPushTask } from '@/api/im/smPushTask';
import { SmPushTaskVO, SmPushTaskQuery, SmPushTaskForm } from '@/api/im/smPushTask/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const smPushTaskList = ref<SmPushTaskVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const smPushTaskFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: SmPushTaskForm = {
  id: undefined,
  messageId: undefined,
  seqNo: undefined,
  sendTime: undefined,
  status: undefined,
  sendToAll: undefined,
  recvIds: undefined,
  deleted: undefined,
  creator: undefined,
  updater: undefined,
}
const data = reactive<PageData<SmPushTaskForm, SmPushTaskQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    messageId: undefined,
    seqNo: undefined,
    sendTime: undefined,
    status: undefined,
    sendToAll: undefined,
    recvIds: undefined,
    deleted: undefined,
    creator: undefined,
    updater: undefined,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "id不能为空", trigger: "blur" }
    ],
    messageId: [
      { required: true, message: "系统消息id不能为空", trigger: "blur" }
    ],
    seqNo: [
      { required: true, message: "发送序列号不能为空", trigger: "blur" }
    ],
    sendTime: [
      { required: true, message: "推送时间不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态 1:待发送 2:发送中 3:已发送 4:已取消不能为空", trigger: "change" }
    ],
    sendToAll: [
      { required: true, message: "是否发送给全体用户不能为空", trigger: "blur" }
    ],
    recvIds: [
      { required: true, message: "接收用户id,逗号分隔,send_to_all为false时有效不能为空", trigger: "blur" }
    ],
    deleted: [
      { required: true, message: "删除标识  0：正常   1：已删除不能为空", trigger: "blur" }
    ],
    creator: [
      { required: true, message: "创建者不能为空", trigger: "blur" }
    ],
    updater: [
      { required: true, message: "更新者不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询系统消息推送任务列表 */
const getList = async () => {
  loading.value = true;
  const res = await listSmPushTask(queryParams.value);
  smPushTaskList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  smPushTaskFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: SmPushTaskVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加系统消息推送任务";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: SmPushTaskVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getSmPushTask(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改系统消息推送任务";
}

/** 提交按钮 */
const submitForm = () => {
  smPushTaskFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateSmPushTask(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addSmPushTask(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: SmPushTaskVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除系统消息推送任务编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delSmPushTask(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('im/smPushTask/export', {
    ...queryParams.value
  }, `smPushTask_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
