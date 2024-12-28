<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="标题" prop="title">
              <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="封面" prop="coverUrl">
              <el-input v-model="queryParams.coverUrl" placeholder="请输入封面" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="简介" prop="intro">
              <el-input v-model="queryParams.intro" placeholder="请输入简介" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="富文本内容，base64编码" prop="richText">
              <el-input v-model="queryParams.richText" placeholder="请输入富文本内容，base64编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="外部链接" prop="externLink">
              <el-input v-model="queryParams.externLink" placeholder="请输入外部链接" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['im:systemMessage:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['im:systemMessage:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['im:systemMessage:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['im:systemMessage:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="systemMessageList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="id" align="center" prop="id" v-if="true" />
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column label="封面" align="center" prop="coverUrl" />
        <el-table-column label="简介" align="center" prop="intro" />
        <el-table-column label="内容类型 0:富文本  1:外部链接" align="center" prop="contentType" />
        <el-table-column label="富文本内容，base64编码" align="center" prop="richText" />
        <el-table-column label="外部链接" align="center" prop="externLink" />
        <el-table-column label="删除标识  0：正常   1：已删除" align="center" prop="deleted" />
        <el-table-column label="创建者" align="center" prop="creator" />
        <el-table-column label="更新者" align="center" prop="updater" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['im:systemMessage:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['im:systemMessage:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改系统消息对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="systemMessageFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="封面" prop="coverUrl">
          <el-input v-model="form.coverUrl" placeholder="请输入封面" />
        </el-form-item>
        <el-form-item label="简介" prop="intro">
          <el-input v-model="form.intro" placeholder="请输入简介" />
        </el-form-item>
        <el-form-item label="富文本内容，base64编码" prop="richText">
            <el-input v-model="form.richText" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="外部链接" prop="externLink">
          <el-input v-model="form.externLink" placeholder="请输入外部链接" />
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

<script setup name="SystemMessage" lang="ts">
import { listSystemMessage, getSystemMessage, delSystemMessage, addSystemMessage, updateSystemMessage } from '@/api/im/systemMessage';
import { SystemMessageVO, SystemMessageQuery, SystemMessageForm } from '@/api/im/systemMessage/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const systemMessageList = ref<SystemMessageVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const systemMessageFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: SystemMessageForm = {
  id: undefined,
  title: undefined,
  coverUrl: undefined,
  intro: undefined,
  contentType: undefined,
  richText: undefined,
  externLink: undefined,
  deleted: undefined,
  creator: undefined,
  updater: undefined,
}
const data = reactive<PageData<SystemMessageForm, SystemMessageQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: undefined,
    coverUrl: undefined,
    intro: undefined,
    contentType: undefined,
    richText: undefined,
    externLink: undefined,
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
    title: [
      { required: true, message: "标题不能为空", trigger: "blur" }
    ],
    coverUrl: [
      { required: true, message: "封面不能为空", trigger: "blur" }
    ],
    intro: [
      { required: true, message: "简介不能为空", trigger: "blur" }
    ],
    contentType: [
      { required: true, message: "内容类型 0:富文本  1:外部链接不能为空", trigger: "change" }
    ],
    richText: [
      { required: true, message: "富文本内容，base64编码不能为空", trigger: "blur" }
    ],
    externLink: [
      { required: true, message: "外部链接不能为空", trigger: "blur" }
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

/** 查询系统消息列表 */
const getList = async () => {
  loading.value = true;
  const res = await listSystemMessage(queryParams.value);
  systemMessageList.value = res.rows;
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
  systemMessageFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: SystemMessageVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加系统消息";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: SystemMessageVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getSystemMessage(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改系统消息";
}

/** 提交按钮 */
const submitForm = () => {
  systemMessageFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateSystemMessage(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addSystemMessage(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: SystemMessageVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除系统消息编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delSystemMessage(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('im/systemMessage/export', {
    ...queryParams.value
  }, `systemMessage_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
