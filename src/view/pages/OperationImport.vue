<script setup>
  import {axiosUtil} from "@/apis/axios/axiosUtil.js";
  import useAppStore from "@/stores/useAppStore.js";
  import useUserStore from "@/stores/useUserStore.js";
  import ImportBaseinfo from "@/view/pages/utility/ImportBaseinfo.vue";
  import {useDebounceFn} from "@vueuse/core";
  import {NCheckbox} from "naive-ui";
  import {useToast} from "primevue/usetoast";
  import {computed, reactive, ref, watch} from "vue";
  
  
  //非响应式数据
  let errorFlag = false
  
  
  //响应式数据
  const importInfo = reactive({
	importId: '',
	timestamp: Date.now(),
	createTimestamp: Date.now(),
	operatorId: useUserStore().username,
	operatorName: useUserStore().usernick,
	cangkuName: useAppStore().defaultData.cangkuName,
	payWay: useAppStore().defaultData.payWay,
	offRate: useAppStore().defaultData.offRate,
	orgId: '',
	orgName: '',
	orgConnector: '',
	orgTel: '',
	notes: '',
	backupLink: ''
  })
  let imList = ref([])
  const toast = useToast()
  
  watch([() => importInfo.orgId], ([newOrgId], [oldOrgId]) => {
	//orgId
	if (newOrgId !== '') queryOrg(newOrgId)
  })
  
  
  const queryOrg = useDebounceFn((id) => {
	axiosUtil.post('/api/org/search', {orgId: id})
		.then(res => {
		  console.log(res.data.code)
		  if (res.data.code === 0) {
			errorFlag = false
			importInfo.orgName = res.data.data[0].orgName; //注意接口返回的是列表
		  } else {
			toast.add({
			  severity: 'error',
			  detail: `未查询到ID为${id}的机构`,
			  life: 5000
			})
			importInfo.orgName = ''
			importInfo.orgTel = ''
			importInfo.orgConnector = ''
			errorFlag = true
		  }
		})
		.catch(err => {
		  toast.add({
			severity: 'error',
			detail: `未查询到ID为${id}的机构`,
			life: 5000
		  })
		  importInfo.orgName = ''
		  importInfo.orgTel = ''
		  importInfo.orgConnector = ''
		  errorFlag = true
		})
	
  }, 800)
  
  const queryProduct = useDebounceFn((id, idx) => {
	id = id.toString().trim()
	if (id !== "") {
	  axiosUtil.post('/api/product/search', {productId: id})
		  .then(res => {
			if (res.data.code === 0) {
			  imList.value[idx].productName = res.data.data[0].productName
			  imList.value[idx].productSpec = res.data.data[0].productSpec
			  imList.value[idx].productUnit = res.data.data[0].productUnit
			} else {
			  toast.add({
				severity: 'error',
				detail: `未查询到ID为${id}的产品`,
				life: 2000
			  })
			  imList.value[idx].productName = ''
			  imList.value[idx].productSpec = ''
			  imList.value[idx].productUnit = ''
			  errorFlag = true
			}
		  })
		  .catch(err => {
			toast.add({
			  severity: 'error',
			  detail: `未查询到ID为${id}的产品`,
			  life: 2000
			})
			imList.value[idx].productName = ''
			imList.value[idx].productSpec = ''
			imList.value[idx].productUnit = ''
			errorFlag = true
		  })
	}
	
  }, 800)
  
  const handleAddIm = () => {
	imList.value.push({
	  selected: false,
	  contactId: '',
	  productId: '',
	  productName: '',
	  productSpec: '',
	  productUnit: '',
	  count: 1,
	  price: 1.0,
	  cost: 1, //上传数据之前，这个数据始终为''，因为从未变过，可以上传前处理一下，也可以不处理，由后台自动计算，即 count*price
	  notes: ''
	})
	// console.log(imList.length)
  }
  
  const handleRemoveIm = () => {
	imList.value = imList.value.filter(im => !im.selected)
	// console.log(imList.value.length)
  }
  
  const handleSubmit = () => {
	if (errorFlag || imList.value.length === 0 || importInfo.orgId === '' || importInfo.cangkuName === '') {
	  toast.add({
		severity: 'warn',
		detail: '请检查是否遗漏必要信息',
		life: 2000
	  })
	  return
	}
	
	axiosUtil.post("/api/import/add", {
	  ...importInfo,
	  importContent: imList.value.filter(im => im.count > 0)
	})
		.then(res => {
		  if (res.data.code === 0) {
			toast.add({
			  severity: 'success',
			  detail: '入库单上传成功',
			  life: 1500
			})
			importInfo.importId = res.data.data
		  } else {
			toast.add({
			  severity: 'error',
			  detail: '入库单上传失败',
			  life: 2000
			})
			importInfo.importId = ''
		  }
		}).catch(err => {
	  toast.add({
		severity: 'error',
		detail: '入库单上传失败',
		life: 2000
	  })
	  importInfo.importId = ''
	})
  }
  
  const computeTotalCount = computed(() => imList.value.reduce((sum, item) => sum + item.count, 0))
  const computeTotalCost = computed(() => imList.value.reduce((sum, item) => sum + (item.count * item.price), 0))
  const computeTotalCostAfterOff = computed(() => computeTotalCost.value * (1 - importInfo.offRate / 100))
</script>

<template>
  <div class="w-full h-full">
	<ImportBaseinfo v-model:baseinfo="importInfo" />
	
	<div>
	  
	  <div class="titles flex items-center w-full px-8 py-2" style="height:fit-content;">
		<span style="flex:none; margin-right:0.5rem; width:40px;"><text class="label">选中</text></span>
		<span style="flex:none; margin-right:0.5rem; width:50px;"><text class="label">编号</text></span>
		<span><text class="label">采购合同号</text></span>
		<span><text class="label">产品ID</text></span>
		<span><text class="label">产品名称</text></span>
		<span><text class="label">规格</text></span>
		<span><text class="label">计量单位</text></span>
		<span><text class="label">入库数量</text></span>
		<span><text class="label">入库单价(元)</text></span>
		<span><text class="label">入库金额(元)</text></span>
		<span><text class="label">备注</text></span>
	  </div>
	  
	  <div class="w-full px-8 mb-4">
		<div v-for="(im,index) in imList" class="item flex items-center w-full first:border-t border-b border-gray-300">
		  <span style="width:40px; flex:none; margin-right:0.5rem;"> <n-checkbox v-model:checked="im.selected" /></span>
		  <span style="width:50px; flex:none; margin-right:0.5rem; "><text style="font-size:0.7rem;font-weight:bold;width:24px;height:23px; border-radius:50%;background-color:#f59e0c;color:white; display:flex;justify-content:center;align-items:center;">{{
			  index + 1
			}}</text></span>
		  <span><input class="value" v-model="im.contactId" /></span>
		  <span><input class="value" v-model="im.productId" @input.stop="queryProduct(im.productId,index)" /></span>
		  <span><input class="value" v-model="im.productName" /></span>
		  <span><input class="value" v-model="im.productSpec" /></span>
		  <span><input class="value" v-model="im.productUnit" /></span>
		  <span><input class="value" v-model.number="im.count" /></span>
		  <span><input class="value" v-model.number="im.price" /></span>
		  <span><text class="value">{{ (parseInt(im.count) * im.price).toFixed(4) }}</text></span>
		  <span><input class="value" v-model="im.notes" /></span>
		</div>
	  
	  </div>
	  <div class="w-full flex justify-center items-center gap-2">
		<span class="px-3 py-1 rounded-2xl bg-amber-200 text-gray-500 hover:text-white hover:bg-amber-400 cursor-pointer " @click.stop="handleAddIm">新增入库条目</span>
		<span v-if="imList.some(im => im.selected)" class="px-3 py-1 rounded-2xl bg-red-300 hover:bg-red-500 text-white  cursor-pointer " @click.stop="handleRemoveIm">移除所选条目</span>
		<span v-if="imList.length>0" class="px-3 py-1 rounded-2xl bg-amber-200 text-gray-500 hover:text-white hover:bg-amber-400 cursor-pointer flex items-center" @click.stop="handleSubmit"><text>提交入库单</text><text class="text-[0.7rem] font-bold">(所有条目)</text></span>
	  </div>
	  
	  <div v-show="imList.length>0" class=" mx-8 my-2 flex w-fit items-center justify-start gap-3 text-[0.8rem] bg-gray-200 px-2.5 py-1 rounded-lg">
		<span class="flex items-center gap-1"><text>总数量:</text><text>{{ computeTotalCount }}</text></span>
		<span class="flex items-center gap-1"><text>总金额:</text><text>{{ computeTotalCost.toFixed(4) }}</text></span>
		<span class="flex items-center gap-1"><text>折扣比例(省略%):</text><input class=" max-w-[32px] pt-[1px] outline-none border cursor-default text-center" v-model.number="importInfo.offRate" /></span>
		<span class="flex items-center gap-1"><text>折后金额:</text><text>{{
			computeTotalCostAfterOff.toFixed(4)
		  }}</text></span>
	  </div>
	</div>
  </div>
</template>

<style lang="scss" scoped>
  .titles, .item {
	
	> span {
	  display: flex;
	  align-items: center;
	  justify-content: center;
	  gap: 0.25rem;
	  
	  &:not(:last-child,:first-child,:nth-child(2)) {
		border-right: 1px #aaa solid;
	  }
	  
	  &:nth-child(3) {
		border-left: 1px #aaa solid;
	  }
	  
	  
	  .label {
		font-size: 0.8rem;
	  }
	  
	  .value {
		font-size: 0.75rem;
		flex: 1 1 auto;
		padding: 0.25rem 0.25rem;
		border-radius: 0.25rem;
		outline: none;
		text-align: center;
	  }
	}
	
	> span:nth-child(1) {
	  flex: 0 1 10%;
	}
	
	> span:nth-child(2) {
	  flex: 0 1 10%;
	}
	
	> span:nth-child(3) {
	  flex: 0 1 20%;
	}
	
	> span:nth-child(4) {
	  flex: 0 1 20%;
	}
	
	> span:nth-child(5) {
	  flex: 0 1 20%;
	}
	
	> span:nth-child(6) {
	  flex: 0 1 20%;
	}
	
	> span:nth-child(7) {
	  flex: 0 1 20%;
	}
	
	> span:nth-child(8) {
	  flex: 0 1 20%;
	}
	
	> span:nth-child(9) {
	  flex: 0 1 20%;
	}
	
	> span:nth-child(10) {
	  flex: 0 1 20%;
	}
	
	> span:nth-child(11) {
	  flex: 0 1 20%;
	}
  }
</style>
