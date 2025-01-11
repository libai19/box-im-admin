<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="内容" prop="content">
              <el-input v-model="queryParams.content" placeholder="内容" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="enabled">
              <el-select v-model="queryParams.enabled"  clearable>
                <el-option  key="1" label="开启" :value="true" />
                <el-option  key="2" label="关闭" :value="false" />
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
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['im:sensitiveWord:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['im:sensitiveWord:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['im:sensitiveWord:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['im:sensitiveWord:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="sensitiveWordList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="内容" align="center" prop="content" />
        <el-table-column label="状态" align="center" prop="enabled" >
          <template #default="scope">
					  <el-switch v-model="scope.row.enabled" @change="switchEnabled(scope.row)" />
				  </template>
        </el-table-column>
        <el-table-column label="创建者" align="center" prop="creatorName" />
        <el-table-column label="创建时间" align="center" prop="createTime" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
              <el-button link type="primary"  @click="handleUpdate(scope.row)" v-hasPermi="['im:sensitiveWord:edit']">修改</el-button>
              <el-button link type="danger"  @click="handleDelete(scope.row)" v-hasPermi="['im:sensitiveWord:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改敏感词对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="800px" append-to-body>
      <el-form ref="sensitiveWordFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="内容">
          <el-input v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="是否启用" prop="enabled">
          <el-switch v-model="form.enabled"/>
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

<script setup name="SensitiveWord" lang="ts">
import { listSensitiveWord, getSensitiveWord, delSensitiveWord, addSensitiveWord, updateSensitiveWord,enableSensitiveWord } from '@/api/im/sensitiveWord';
import { SensitiveWordVO, SensitiveWordQuery, SensitiveWordForm } from '@/api/im/sensitiveWord/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const sensitiveWordList = ref<SensitiveWordVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const sensitiveWordFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: SensitiveWordForm = {
  id: undefined,
  content: undefined,
  enabled: undefined,
  creator: undefined,
}
const data = reactive<PageData<SensitiveWordForm, SensitiveWordQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    content: undefined,
    enabled: undefined,
    creator: undefined,
    params: {
    }
  },
  rules: {
    content: [
      { required: true, message: "内容不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询敏感词列表 */
const getList = async () => {
  loading.value = true;
  const res = await listSensitiveWord(queryParams.value);
  sensitiveWordList.value = res.rows;
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
  sensitiveWordFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: SensitiveWordVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  form.value.enabled = true;
  dialog.visible = true;
  dialog.title = "添加敏感词";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: SensitiveWordVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getSensitiveWord(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改敏感词";
}

/** 提交按钮 */
const submitForm = () => {
  sensitiveWordFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateSensitiveWord(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addSensitiveWord(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("操作成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: SensitiveWordVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除？').finally(() => loading.value = false);
  await delSensitiveWord(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('im/sensitiveWord/export', {
    ...queryParams.value
  }, `sensitiveWord_${new Date().getTime()}.xlsx`)
}


const switchEnabled = (row: any) => {
    enableSensitiveWord(row)
}

onMounted(() => {
  getList();
});
</script>
