<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="推送消息" prop="messageId">
              <im-sm-select v-model="queryParams.messageId" placeholder="请输入系统消息" clearable ></im-sm-select>
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd"
              v-hasPermi="['im:smPushTask:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['im:smPushTask:remove']">删除</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="smPushTaskList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="推送消息" align="center" prop="messageTitle" />
        <el-table-column label="推送时间" align="center" prop="sendTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="推送状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="im_sm_push_status" :value="scope.row.status" />
          </template>
        </el-table-column>>
        <el-table-column label="接收用户" align="center" prop="recvIds">
          <template #default="scope">
            <span>{{ scope.row.sendToAll ? '全体用户' : `共${scope.row.recvIds.split(',').length}个用户` }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建者" align="center" prop="creatorName" />
        <el-table-column label="创建时间" align="center" prop="createTime" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button v-if="scope.row.status == 1" v-hasPermi="['im:smPushTask:edit']" type="primary" link
              @click="handleUpdate(scope.row)">修改</el-button>
            <el-button v-hasPermi="['im:smPushTask:remove']" type="danger" link
              @click="handleDelete(scope.row)">删除</el-button>
            <el-button v-if="scope.row.status == 1" v-hasPermi="['im:smPushTask:edit']" type="danger" link
              @click="handleCancel(scope.row.id)">取消</el-button>
            <el-button v-if="scope.row.status == 4" v-hasPermi="['im:smPushTask:edit']" type="primary" link
              @click="handleOpen(scope.row.id)">开启</el-button>
            <el-button v-if="scope.row.status == 3" v-hasPermi="['im:smPushTask:add']" type="primary" link
              @click="handleResend(scope.row.id)">再次推送</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <sm-task-info ref="taskInfoRef" @refreshDataList="getList"></sm-task-info>
  </div>
</template>

<script setup name="SmPushTask" lang="ts">
import { listSmPushTask, getSmPushTask, delSmPushTask, cancelSmPushTask, openSmPushTask } from '@/api/im/smPushTask';
import { SmPushTaskVO, SmPushTaskQuery } from '@/api/im/smPushTask/types';
import  SmTaskInfo  from './SmTaskInfo.vue'

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const smPushTaskList = ref<SmPushTaskVO[]>([]);

const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const taskInfoRef = ref();

const queryParams = reactive<SmPushTaskQuery>({
    pageNum: 1,
    pageSize: 10,
    messageId: undefined,
})
const { im_sm_push_status } = toRefs<any>(proxy?.useDict('im_sm_push_status'));


/** 查询系统消息推送任务列表 */
const getList = async () => {
  loading.value = true;
  const res = await listSmPushTask(queryParams);
  smPushTaskList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1;
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
  console.log(taskInfoRef.value)
  taskInfoRef.value.init()
}

/** 修改按钮操作 */
const handleUpdate = async (row: SmPushTaskVO) => {
  taskInfoRef.value.init(row.id);
}


/** 删除按钮操作 */
const handleDelete = async (row?: SmPushTaskVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除？').finally(() => loading.value = false);
  await delSmPushTask(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

const handleCancel = (id: number) => {
  cancelSmPushTask(id).then(() => {
    getList()
    ElMessage.success('任务取消成功')
  })
}

const handleOpen = (id: number) => {
  openSmPushTask(id).then(() => {
    getList();
    ElMessage.success('任务开启成功')
  })
}

const handleResend = async(id: number) => {
  const res = await getSmPushTask(id);
  taskInfoRef.value.initByTask(res.data);
}

onMounted(() => {
  getList();
});
</script>
