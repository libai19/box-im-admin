<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="发送时间" prop="createdTime">
              <el-date-picker
                    v-model="dateRange"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    type="daterange"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
                  ></el-date-picker>
            </el-form-item>
            <el-form-item label="发送用户" prop="sendId">
              <im-user-select v-model="queryParams.sendId"></im-user-select>
            </el-form-item>
            <el-form-item label="接收用户" prop="recvId">
              <im-user-select v-model="queryParams.recvId"></im-user-select>
            </el-form-item>
            <el-form-item label="内容" prop="content">
              <el-input v-model="queryParams.content"></el-input>
            </el-form-item>
            <el-form-item label="消息类型" prop="type">
              <el-select v-model="queryParams.type"  clearable>
                <el-option v-for="dict in im_message_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="消息类型" prop="status">
              <el-select v-model="queryParams.status"  clearable>
                <el-option v-for="dict in im_message_status" :key="dict.value" :label="dict.label" :value="dict.value" />
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

    <el-card shadow="never">
      <el-table v-loading="loading" :data="privateMessageList" @selection-change="handleSelectionChange">
        <el-table-column label="发送用户" align="center" prop="sendUserName" />
        <el-table-column label="接收用户" align="center" prop="recvUserName" />
        <el-table-column label="发送内容" align="center" prop="content" >
          <template #default="scope">
            <im-message-content :message="scope.row" ></im-message-content>
          </template>
        </el-table-column>
        <el-table-column label="消息类型" align="center" prop="type" >
          <template #default="scope">
            <dict-tag :options="im_message_type" :value="scope.row.type" />
          </template>
        </el-table-column>
        <el-table-column label="状态 " align="center" prop="status" >
          <template #default="scope">
            <dict-tag :options="im_message_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="发送时间" align="center" prop="sendTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="详情" placement="top">
              <el-button link type="primary"  @click="handleUpdate(scope.row)" v-hasPermi="['im:privateMessage:query']">详情</el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改私聊消息对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="privateMessageFormRef" :model="form" :rules="rules" label-width="80px" disabled>
        <el-form-item label="发送用户" prop="sendUserName">
          <el-input v-model="form.sendUserName" />
        </el-form-item>
        <el-form-item label="接收用户" prop="sendUserName">
          <el-input v-model="form.recvUserName"  />
        </el-form-item>
        <el-form-item label="发送内容">
          <el-input v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="发送时间" prop="sendTime">
          <el-date-picker clearable
            v-model="form.sendTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择发送时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="消息类型">
          <dict-tag :options="im_message_type" :value="form.type" />
        </el-form-item>
        <el-form-item label="消息状态">
          <dict-tag :options="im_message_status" :value="form.status" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="PrivateMessage" lang="ts">
import { listPrivateMessage, getPrivateMessage } from '@/api/im/privateMessage';
import { PrivateMessageVO, PrivateMessageQuery, PrivateMessageForm } from '@/api/im/privateMessage/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const privateMessageList = ref<PrivateMessageVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const privateMessageFormRef = ref<ElFormInstance>();
const dateRange = ref<[DateModelType, DateModelType]>(['', '']);

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: PrivateMessageForm = {
  id: undefined,
  sendId: undefined,
  sendUserName: undefined,
  recvUserName: undefined,
  recvId: undefined,
  content: undefined,
  type: undefined,
  status: undefined,
  sendTime: undefined
}
const data = reactive<PageData<PrivateMessageForm, PrivateMessageQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sendId: undefined,
    recvId: undefined,
    content: undefined,
    type: undefined,
    status: undefined,
    sendTime: undefined,
    params: {
    }
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);
const { im_message_type, im_message_status } = toRefs<any>(proxy?.useDict('im_message_type', 'im_message_status'));
/** 查询私聊消息列表 */
const getList = async () => {
  loading.value = true;
  const res = await listPrivateMessage(proxy?.addDateRange(queryParams.value, dateRange.value));
  privateMessageList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

/** 确定按钮 */
const submitForm = () => {
  dialog.visible = false;
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  privateMessageFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: PrivateMessageVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 修改按钮操作 */
const handleUpdate = async (row?: PrivateMessageVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getPrivateMessage(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "消息详情";
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('im/privateMessage/export', {
    ...queryParams.value
  }, `privateMessage_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
