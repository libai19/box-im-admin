<template>
    <el-select v-model="userIds" :multiple="multiple" filterable remote clearable
        :placeholder="placeholder" :remote-method="handleRemote" :loading="loading">
        <el-option v-for="user in options" :key="user.id" :label="user.userName" :value="user.id" />
    </el-select>
</template>

<script setup lang="ts" name="ImUserSelect">

import { computed, ref, watch } from 'vue'
import { findUserByName, findUserByIds } from '@/api/im/user'
import { UserVO } from '@/api/im/user/types'

const props = defineProps({
	multiple: {
		type: Boolean,
		required: false,
		default: () => false
	},
	placeholder: {
		type: String,
		required: false,
		default: () => ''
	}
})

const loading = ref(false)
const options = ref<UserVO[]>([])
const model = defineModel<string | number | Array<string | number>>()
const userIds = computed({
	get() {
        if(model.value !== undefined){
            return model.value
        }else if(props.multiple){
            return []
        }
        return undefined
	},
	set(value) {
		model.value = value
	}
})

const handleRemote = (name: string)=>{
	loading.value = true
    findUserByName(name).then((res) => {
      loading.value = false;
      options.value = res.data;
	}).catch(() => {
      loading.value = false;
    });
}

watch(
  () => model.value,
  async (value) => {
    const ids = Array.isArray(value) ? value : value !== undefined && value !== '' ? [value] : [];
    if (!ids.length) return;
    const missedIds = ids.filter((id) => !options.value.some((item) => item.id === id));
    if (!missedIds.length) return;
    const { data } = await findUserByIds(missedIds);
    options.value = [...options.value, ...data.filter((item) => !options.value.some((option) => option.id === item.id))];
  },
  { immediate: true }
)

</script>
