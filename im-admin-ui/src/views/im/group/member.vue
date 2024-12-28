<template>
	<div class="p-2">
		<transition :enter-active-class="proxy?.animate.searchAnimate.enter"
			:leave-active-class="proxy?.animate.searchAnimate.leave">
			<div v-show="showSearch" class="mb-[10px]">
				<el-card shadow="hover">
					<el-form ref="queryFormRef" :model="queryParams" :inline="true">
						<el-form-item label="用户" prop="userId">
							<im-user-select v-model="queryParams.userId"></im-user-select>
						</el-form-item>
						<el-form-item label="昵称" prop="userNickName">
							<el-input v-model="queryParams.userNickName" placeholder="请输入用户昵称" clearable />
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
			<el-table v-loading="loading" :data="groupMemberList">
				<el-table-column label="头像" align="center" prop="headImage" width="100">
					<template #default="scope">
						<image-preview :src="scope.row.headImage" :width="50" :height="50" />
					</template>
				</el-table-column>
				<el-table-column label="用户" align="center" prop="userName" />
				<el-table-column label="群内昵称" align="center" prop="showNickName" />
				<el-table-column label="进群时间" align="center" prop="createdTime" width="180">
					<template #default="scope">
						<span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d}') }}</span>
					</template>
				</el-table-column>
			</el-table>
			<pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
				v-model:limit="queryParams.pageSize" @pagination="getList" />
		</el-card>
	</div>
</template>

<script setup name="member" lang="ts">
import { listGroupMember } from '@/api/im/groupMember';
import { GroupMemberVO, GroupMemberQuery } from '@/api/im/groupMember/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const groupMemberList = ref<GroupMemberVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const queryFormRef = ref<ElFormInstance>();

const queryParams = ref<GroupMemberQuery>({
	pageNum: 1,
		pageSize: 10,
		groupId: undefined,
		userId: undefined,
		userName: undefined,
		remarkNickName: undefined,
		headImage: undefined,
		remarkGroupName: undefined,
		quit: undefined,
		createdTime: undefined,
		quitTime: undefined,
		userNickName: undefined,
		showNickName: undefined
})

/** 查询群成员列表 */
const getList = async () => {
	loading.value = true;
	const res = await listGroupMember(queryParams.value);
	groupMemberList.value = res.rows;
	total.value = res.total;
	loading.value = false;
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

const init = (id: number) => {
	queryParams.value.groupId = id;
	getList();
}

defineExpose({
	init
})
</script>