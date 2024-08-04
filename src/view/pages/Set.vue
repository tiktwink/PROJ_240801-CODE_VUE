<script setup>
  import useAppStore from "@/stores/useAppStore.js";
  import {Check, Settings2} from 'lucide-vue-next';
  import {NModal, NModalProvider, NSelect} from "naive-ui";
  import {ref} from "vue";
  
  const defaultData = {
	org: {
	  orgId: '', //只有id是可定义的，其余属性由id自查
	  orgName: '',
	  orgType: ''
	},
	cangkuName: '杭州1号仓',
	payWay: '款到发货',
	offRate: 5
  }
  const showModalPayway = ref(false)
  const dd = ref(useAppStore().defaultData)
  let addPayway = ref('')
  
  //todo 应该把这些默认设置保存到服务器而非浏览器本地，这里简化了

</script>

<template>
  <n-modal-provider>
	<div class="w-full h-full min-h-[30dvh] flex flex-col  justify-center  items-center">
	  <div class="flex flex-col min-w-[300px] gap-2">
		<span class="item flex items-center gap-1"><text class="label">默认仓库：</text><input class="value" v-model.trim="dd.cangkuName" /></span>
		<span class="item flex items-center gap-1 pr-2">
          <text class="label min-w-fit">默认支付方式：</text>
          <n-select :options="dd.payWayList" class="value mr-2" v-model:value="dd.payWay" />
          <n-tooltip trigger="hover">
          <template #trigger>
            <settings2 :size="32" :stroke-width="1.25" class="cursor-pointer hover:text-amber-500 h-fit  " @click.stop="showModalPayway=true" />
          </template>
            管理支付选项
         </n-tooltip>
      </span>
		<span class="item flex items-center gap-1"><text class="label">默认折扣率(省略%)：</text><input class="value" v-model.trim.number="dd.offRate" /></span>
	  </div>
	</div>
	
	<n-modal :mask-closable="true" v-model:show="showModalPayway">
	  <div class="modal-payway bg-white p-4 rounded flex flex-col  gap-2">
		<div class="font-bold text-base border-b mb-1 pb-2 flex items-center justify-between">
		  管理支付选项
		  <Check :size="28" :stroke-width="1.25" class="cursor-pointer hover:bg-gray-200 rounded-full p-1" @click.stop="showModalPayway=false" />
		</div>
		
		<div class="relative flex flex-col gap-2 border-b pb-3 mb-1">
		  <span class="text-gray-400 text-[0.75rem] h-[20px]">以下是现有支付方式，选中后可以删除</span>
		  <span v-for=" item in dd.payWayList">
          <NCheckbox :label="item.label" v-model:checked="item.checked" />
        </span>
		  <span class=" absolute top-[28px] right-0 text-red-300 hover:text-red-500 cursor-pointer" @click.stop="dd.payWayList = dd.payWayList.filter(item => !item.checked)">删除所选</span>
		</div>
		
		<div class="flex items-center justify-between gap-1 w-full">
		  <input class="max-w-[140px] flex-auto bg-gray-100 px-1 py-0.5 rounded indent-1 cursor-default" type="text" v-model="addPayway">
		  <text class="text-end text-gray-500 hover:text-amber-500 cursor-pointer" @click.stop="dd.payWayList.push({label:addPayway,checked:false,value:addPayway})">
			新增所填
		  </text>
		</div>
	  </div>
	</n-modal>
  </n-modal-provider>
</template>

<style lang="scss" scoped>
  * {
	outline: none !important;
	
	input {
	  padding: 0.3rem 0.5rem;
	  border: 1px #ccc solid;
	  cursor: default;
	  border-radius: 0.25rem;
	}
	
	.item {
	  .label {
		font-weight: bold;
	  }
	  
	  .value {
		flex: 1 1 auto;
	  }
	}
	
	.modal-payway {
	  cursor: default;
	  
	}
  }
</style>
