<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="群id" prop="groupId">
              <el-input v-model="queryParams.groupId" placeholder="请输入群id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="发送用户id" prop="sendId">
              <el-input v-model="queryParams.sendId" placeholder="请输入发送用户id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="发送用户昵称" prop="sendNickName">
              <el-input v-model="queryParams.sendNickName" placeholder="请输入发送用户昵称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="被@用户id列表，逗号分隔" prop="atUserIds">
              <el-input v-model="queryParams.atUserIds" placeholder="请输入被@用户id列表，逗号分隔" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="发送时间" prop="sendTime">
              <el-date-picker clearable
                v-model="queryParams.sendTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择发送时间"
              />
            </el-form-item>
            <el-form-item label="回执消息是否完成" prop="receiptOk">
              <el-input v-model="queryParams.receiptOk" placeholder="请输入回执消息是否完成" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="是否回执消息" prop="receipt">
              <el-input v-model="queryParams.receipt" placeholder="请输入是否回执消息" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="接收用户id,逗号分隔，为空表示发给所有成员" prop="recvIds">
              <el-input v-model="queryParams.recvIds" placeholder="请输入接收用户id,逗号分隔，为空表示发给所有成员" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['im:groupMessage:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['im:groupMessage:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['im:groupMessage:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['im:groupMessage:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="groupMessageList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="id" align="center" prop="id" v-if="true" />
        <el-table-column label="群id" align="center" prop="groupId" />
        <el-table-column label="发送用户id" align="center" prop="sendId" />
        <el-table-column label="发送用户昵称" align="center" prop="sendNickName" />
        <el-table-column label="被@用户id列表，逗号分隔" align="center" prop="atUserIds" />
        <el-table-column label="发送内容" align="center" prop="content" />
        <el-table-column label="" align="center" prop="status" />
        <el-table-column label="消息类型" align="center" prop="type" />
        <el-table-column label="发送时间" align="center" prop="sendTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="回执消息是否完成" align="center" prop="receiptOk" />
        <el-table-column label="是否回执消息" align="center" prop="receipt" />
        <el-table-column label="接收用户id,逗号分隔，为空表示发给所有成员" align="center" prop="recvIds" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['im:groupMessage:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['im:groupMessage:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改群消息对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="groupMessageFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="群id" prop="groupId">
          <el-input v-model="form.groupId" placeholder="请输入群id" />
        </el-form-item>
        <el-form-item label="发送用户id" prop="sendId">
          <el-input v-model="form.sendId" placeholder="请输入发送用户id" />
        </el-form-item>
        <el-form-item label="发送用户昵称" prop="sendNickName">
          <el-input v-model="form.sendNickName" placeholder="请输入发送用户昵称" />
        </el-form-item>
        <el-form-item label="被@用户id列表，逗号分隔" prop="atUserIds">
          <el-input v-model="form.atUserIds" placeholder="请输入被@用户id列表，逗号分隔" />
        </el-form-item>
        <el-form-item label="发送内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="发送时间" prop="sendTime">
          <el-date-picker clearable
            v-model="form.sendTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择发送时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="回执消息是否完成" prop="receiptOk">
          <el-input v-model="form.receiptOk" placeholder="请输入回执消息是否完成" />
        </el-form-item>
        <el-form-item label="是否回执消息" prop="receipt">
          <el-input v-model="form.receipt" placeholder="请输入是否回执消息" />
        </el-form-item>
        <el-form-item label="接收用户id,逗号分隔，为空表示发给所有成员" prop="recvIds">
          <el-input v-model="form.recvIds" placeholder="请输入接收用户id,逗号分隔，为空表示发给所有成员" />
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

<script setup name="GroupMessage" lang="ts">
import { listGroupMessage, getGroupMessage, delGroupMessage, addGroupMessage, updateGroupMessage } from '@/api/im/groupMessage';
import { GroupMessageVO, GroupMessageQuery, GroupMessageForm } from '@/api/im/groupMessage/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const groupMessageList = ref<GroupMessageVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const groupMessageFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: GroupMessageForm = {
  id: undefined,
  groupId: undefined,
  sendId: undefined,
  sendNickName: undefined,
  atUserIds: undefined,
  content: undefined,
  status: undefined,
  type: undefined,
  sendTime: undefined,
  receiptOk: undefined,
  receipt: undefined,
  recvIds: undefined
}
const data = reactive<PageData<GroupMessageForm, GroupMessageQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    groupId: undefined,
    sendId: undefined,
    sendNickName: undefined,
    atUserIds: undefined,
    content: undefined,
    status: undefined,
    type: undefined,
    sendTime: undefined,
    receiptOk: undefined,
    receipt: undefined,
    recvIds: undefined,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "id不能为空", trigger: "blur" }
    ],
    groupId: [
      { required: true, message: "群id不能为空", trigger: "blur" }
    ],
    sendId: [
      { required: true, message: "发送用户id不能为空", trigger: "blur" }
    ],
    sendNickName: [
      { required: true, message: "发送用户昵称不能为空", trigger: "blur" }
    ],
    atUserIds: [
      { required: true, message: "被@用户id列表，逗号分隔不能为空", trigger: "blur" }
    ],
    content: [
      { required: true, message: "发送内容不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "不能为空", trigger: "change" }
    ],
    type: [
      { required: true, message: "消息类型 0:文字 1:图片 2:文件不能为空", trigger: "change" }
    ],
    sendTime: [
      { required: true, message: "发送时间不能为空", trigger: "blur" }
    ],
    receiptOk: [
      { required: true, message: "回执消息是否完成不能为空", trigger: "blur" }
    ],
    receipt: [
      { required: true, message: "是否回执消息不能为空", trigger: "blur" }
    ],
    recvIds: [
      { required: true, message: "接收用户id,逗号分隔，为空表示发给所有成员不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询群消息列表 */
const getList = async () => {
  loading.value = true;
  const res = await listGroupMessage(queryParams.value);
  groupMessageList.value = res.rows;
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
  groupMessageFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: GroupMessageVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加群消息";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: GroupMessageVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getGroupMessage(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改群消息";
}

/** 提交按钮 */
const submitForm = () => {
  groupMessageFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateGroupMessage(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addGroupMessage(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: GroupMessageVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除群消息编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delGroupMessage(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('im/groupMessage/export', {
    ...queryParams.value
  }, `groupMessage_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
