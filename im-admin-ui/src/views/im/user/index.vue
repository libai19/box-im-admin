<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="用户名" prop="userName">
              <el-input v-model="queryParams.userName" placeholder="请输入用户名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="queryParams.nickName" placeholder="请输入用户昵称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
              <el-button type="danger" plain icon="Lock" :disabled="multiple" @click="handleBatchBan"
                v-hasPermi="['im:user:ban']">选中批量封禁</el-button>
              <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
                v-hasPermi="['im:user:remove']">选中批量删除</el-button>
              <el-button type="warning" plain icon="Download" @click="handleExport"
                v-hasPermi="['im:user:export']">导出</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="never">
      <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="用户名" align="center" prop="userName" />
        <el-table-column label="用户昵称" align="center" prop="nickName" />
        <el-table-column label="用户头像" align="center" prop="headImageThumb" width="100">
          <template #default="scope">
            <image-preview :src="scope.row.headImageThumb" :full-src="scope.row.headImage" :width="50" :height="50" />
          </template>
        </el-table-column>
        <el-table-column label="性别" align="center" prop="sex">
          <template #default="scope">
            <dict-tag :options="sys_user_sex" :value="scope.row.sex" />
          </template>
        </el-table-column>
        <el-table-column label="是否被封禁" align="center" prop="isBanned">
          <template #default="scope">
            <el-tag :class="['ban-status-tag', scope.row.isBanned ? 'is-banned' : 'is-normal']">
              {{ scope.row.isBanned ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="注册时间" align="center" prop="createdTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="最后登录时间" align="center" prop="lastLoginTime" width="180">
          <template #default="scope">
            <span>{{ scope.row.lastLoginTime ? parseTime(scope.row.lastLoginTime, '{y}-{m}-{d} {h}:{i}:{s}') : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" v-hasPermi="['im:user:query']"
              @click="handleDetail(scope.row)">详情</el-button>
            <el-button v-if="scope.row.isBanned" link type="danger" v-hasPermi="['im:user:ban']"
              @click="unbanHandle(scope.row)">解封</el-button>
            <el-button v-else link type="danger" v-hasPermi="['im:user:ban']"
              @click="banHandle(scope.row)">封禁</el-button>
            <el-button link type="danger" v-hasPermi="['im:user:remove']"
              @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改用户对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="800px" append-to-body>
      <el-form ref="userFormRef" :model="form" :rules="rules" label-width="100px" disabled>
        <el-form-item label="用户头像" prop="headImage">
          <image-preview v-if="form.headImageThumb" :src="form.headImageThumb" :full-src="form.headImage"
            :width="100" :height="100" />
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <dict-tag :options="sys_user_sex" :value="form.sex" />
        </el-form-item>
        <el-form-item label="用户昵称" prop="nickName">
          <el-input v-model="form.nickName"  />
        </el-form-item>
        <el-form-item label="个性签名" prop="signature">
          <el-input v-model="form.signature" />
        </el-form-item>
        <el-form-item label="最后登录时间" prop="lastLoginTime">
          <el-date-picker clearable v-model="form.lastLoginTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="注册时间" prop="createdTime">
          <el-date-picker clearable v-model="form.createdTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="是否被封禁" prop="isBanned">
          <el-tag :class="['ban-status-tag', form.isBanned ? 'is-banned' : 'is-normal']">
            {{ form.isBanned ? '是' : '否' }}
          </el-tag>
        </el-form-item>
        <el-form-item v-if="form.isBanned" label="被封禁原因" prop="reason">
          <el-input v-model="form.reason"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="User" lang="ts">
import { listUser, getUser, ban, batchBan, unban, delUser } from '@/api/im/user';
import { UserVO, UserQuery, UserForm } from '@/api/im/user/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const userList = ref<UserVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const userFormRef = ref<ElFormInstance>();
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: UserForm = {
  id: undefined,
  userName: undefined,
  nickName: undefined,
  headImage: undefined,
  headImageThumb: undefined,
  password: undefined,
  sex: undefined,
  signature: undefined,
  lastLoginTime: undefined,
  createdTime: undefined,
  type: undefined,
  isBanned: undefined,
  reason: undefined
}
const data = reactive<PageData<UserForm, UserQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userName: undefined,
    nickName: undefined,
    params: {
    }
  },
  rules: {}
});
const { queryParams, form, rules } = toRefs(data);

const { im_bool } = toRefs<any>(proxy?.useDict('im_bool'));
const { sys_user_sex } = toRefs<any>(proxy?.useDict('sys_user_sex'));

/** 查询用户列表 */
const getList = async () => {
  loading.value = true;
  const res = await listUser(queryParams.value);
  userList.value = res.rows;
  total.value = res.total;
  loading.value = false;
  console.log("getList")
}
/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
  console.log("handleQuery")
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
  console.log("handleQuery")
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: UserVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
  console.log("handleSelectionChange")
}

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData };
  userFormRef.value?.resetFields();
  console.log("reset")
}

/** 修改按钮操作 */
const handleDetail = async (row?: UserVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getUser(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "用户信息";
}

/** 提交按钮 */
const submitForm = () => {
  dialog.visible = false;
}

const banHandle = (user: any) => {
  ElMessageBox.prompt('封禁原因:', '确定对该用户进行封禁？', {
    inputPattern: /\S/,
    inputErrorMessage: '请输入封禁原因',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(({ value }) => {
    const data = { id: user.id, reason: value }
    ban(data).then(() => {
      user.isBanned = true
      ElMessage.success(`用户'${user.userName}'已被封禁`);
    })
  })
}

const handleBatchBan = () => {
  ElMessageBox.prompt('封禁原因:', '确定对选中的用户进行批量封禁？', {
    inputPattern: /\S/,
    inputErrorMessage: '请输入封禁原因',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(({ value }) => {
    batchBan({ ids: ids.value, reason: value }).then(() => {
      ElMessage.success('批量封禁成功');
      getList();
    })
  })
}

const unbanHandle = (user: any) => {
  ElMessageBox.confirm('确定解除该用户的封禁状态？？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(() => {
    const data = { id: user.id }
    unban(data).then(() => {
      user.isBanned = false
      ElMessage.success(`用户'${user.userName}'解锁成功`);
    })

  })
}

/** 删除按钮操作 */
const handleDelete = (row?: UserVO) => {
  const _ids = row?.id || ids.value;
  const title = row?.userName ? `是否确认删除用户 "${row.userName}"？` : `是否确认删除选中的 ${ids.value.length} 个用户？`;
  ElMessageBox.confirm(title, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return delUser(_ids);
  }).then(() => {
    ElMessage.success('删除成功');
    getList();
  })
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('im/user/export', {
    ...queryParams.value
  }, `user_${new Date().getTime()}.xlsx`)
}


onMounted(() => {
  getList();
});
</script>

<style scoped>
.ban-status-tag.is-banned {
  color: #f56c6c;
  background-color: #fef0f0;
  border-color: #fde2e2;
}

.ban-status-tag.is-normal {
  color: #409eff;
  background-color: #ecf5ff;
  border-color: #d9ecff;
}
</style>
