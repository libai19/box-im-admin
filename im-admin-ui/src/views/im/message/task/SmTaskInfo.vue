<template>
    <!-- 添加或修改系统消息推送任务对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
        <el-form ref="dataFormRef" :model="form" :rules="dataRules" label-width="80px">
            <el-form-item label="系统消息" prop="messageId">
                <im-sm-select v-model="form.messageId"></im-sm-select>
            </el-form-item>
            <el-form-item label="推送时间" prop="sendTime">
                <el-date-picker clearable v-model="form.sendTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="请选择推送时间">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="接收用户" prop="sendToAll">
                <el-radio-group v-model="form.sendToAll">
                    <el-radio :value="true">全体用户</el-radio>
                    <el-radio :value="false">指定用户</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item v-if="!form.sendToAll">
                <im-user-select v-model="recvIds" :multiple="true" placeholder="请选择最多20个用户"></im-user-select>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="SmTaskInfo" lang="ts">
import { getSmPushTask, addSmPushTask, updateSmPushTask } from '@/api/im/smPushTask';
import { SmPushTaskForm, SmPushTaskVO } from '@/api/im/smPushTask/types';
import { parseTime } from '@/utils/ruoyi'; 

const emit = defineEmits(['refreshDataList'])

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const recvIds = ref<Array<number>>([]);
const dataFormRef = ref<ElFormInstance>();
const dataRules = ref()
const buttonLoading = ref(false);
const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
});

const form: SmPushTaskForm = reactive({
    id: undefined,
    messageId: null,
    sendTime: null,
    sendToAll: true,
    recvIds: ''
})

/** 提交按钮 */
const submitForm = () => {
    buttonLoading.value = true;
    dataFormRef.value?.validate(async (valid: boolean) => {
        if (valid) {
            form.recvIds = recvIds.value.join(",");
            if (form.id) {
                updateSmPushTask(form).finally(() => buttonLoading.value = false);
            } else {
                await addSmPushTask(form).finally(() => buttonLoading.value = false);
            }
            proxy?.$modal.msgSuccess("操作成功");
            dialog.visible = false;
            emit('refreshDataList')
        }
    });
}

const init = async (id?: number) => {
    dialog.visible = true;
    form.id = id;
    recvIds.value = [];
    // 重置表单数据
    dataFormRef.value?.resetFields()
    if (id) {
        const res = await getSmPushTask(id);
        Object.assign(form, res.data);
        if (res.data.recvIds) {
            recvIds.value = res.data.recvIds.split(",").map(Number);
        }
        dialog.title = "修改推送任务";
    } else {
        dialog.title = "添加推送任务";
    }
}

const initByTask = (task?: SmPushTaskVO) => {
    dialog.visible = true;
    dataFormRef.value?.resetFields()
    form.id = undefined;
    form.messageId = task.messageId;
    form.sendToAll = task.sendToAll;
    form.recvIds = task.recvIds;
    form.sendTime = parseTime(new Date(),'{y}-{m}-{d} {h}:{i}:{s}');
    recvIds.value = [];
    if (task.recvIds) {
        recvIds.value = task.recvIds.split(",").map(Number);
    }
    dialog.title = "添加推送任务";
}


const initByMessage = (messageId: number) => {
    dialog.visible = true;
    dialog.title = "添加推送任务";
    dataFormRef.value?.resetFields()
    form.id = undefined;
    form.messageId = messageId;
    form.sendToAll = true;
    form.recvIds = '';
    form.sendTime = parseTime(new Date(),'{y}-{m}-{d} {h}:{i}:{s}');
    console.log(form.sendTime)
}

const cancel = () => {

    dialog.visible = false;
}
defineExpose({
    init, initByTask, initByMessage
})
</script>
