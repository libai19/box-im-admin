<template>
    <el-select v-model="groupIds" :multiple="multiple"  filterable remote clearable
        :placeholder="placeholder" :remote-method="handleRemote" :loading="loading"
        style="width: 240px">
        <el-option v-for="group in options" :key="group.id" :label="group.name" :value="group.id" />
    </el-select>
</template>

<script setup lang="ts" name="ImGroupSelect">

import { computed } from 'vue'
import { ref } from 'vue'
import { findGroupByName } from '@/api/im/group'
import { GroupVO } from '@/api/im/group/types'

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
const options = ref<GroupVO[]>([])
const model = defineModel<string | number | Array<string | number>>()
const groupIds = computed({
	get() {
        if(model.value != undefined){
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
    findGroupByName(name).then((res) => {
      loading.value = false;
      options.value = res.data;
	}).catch(() => {
      loading.value = false;
    });
}

</script>
