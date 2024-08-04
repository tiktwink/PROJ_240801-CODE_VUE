<script setup>
  import {axiosUtil} from "@/apis/axios/axiosUtil.js";
  import useAppStore from "@/stores/useAppStore.js";
  import useUserStore from "@/stores/useUserStore.js";
  import {Check, RotateCw} from "lucide-vue-next";
  import {NButton, NDataTable, NDatePicker, NModal, NModalProvider} from "naive-ui";
  import {useToast} from "primevue/usetoast";
  import {computed, h, onMounted, reactive, ref} from "vue";
  
  const toast = useToast()
  const imList = ref([]) //存储查询到的入库日志列表
  const exList = ref([]) //存储查询到的出库日志列表
  const id = ref('')
  const orgId = ref('')
  const timestamp = ref(null)
  const createTimestamp = ref(null)
  const operatorId = ref(useUserStore().username)
  const type = ref('')
  
  onMounted(() => {
	getExportLogs()
	getImportLogs()
  })
  
  const handleClickToQuery = () => {
	console.log('点击了查询按钮')
	imList.value = []
	exList.value = []
	getImportLogs()
	getExportLogs()
	
	setTimeout(() => {
	  if (imList.value.length === 0 && exList.value.length === 0) {
		console.log('未查询到任何日志')
		toast.add({
		  severity: 'error',
		  detail: '未查询到任何日志',
		  life: 3000
		})
	  }
	}, 2000)
  }
  
  // ❗组装查询到的日志列表
  const data = computed(() => {
	return [...exList.value, ...imList.value]
  })
  // ❗获取日志（出库）
  const getExportLogs = () => {
	axiosUtil.post('/api/export/log', {
	  exportId: id.value,
	  timestamp: timestamp.value,
	  createTimestamp: createTimestamp.value,
	  operatorId: operatorId.value,
	  orgId: orgId.value,
	})
		.then(res => {
		  if (res.data.code === 0) {
			exList.value = res.data.data.map(item => {
			  return {
				...item,
				type: '出库',
				id: item.exportId,
				productPrice: item.totalCost / item.totalCount,
				totalCost: item.totalCost.toFixed(4),
				totalCostAfterOff: item.totalCostAfterOff.toFixed(4),
				timetext: new Date(item.timestamp).toLocaleString(),
				createTimetext: new Date(item.createTimestamp).toLocaleString()
			  }
			})
		  } else {
			exList.value = []
		  }
		})
		.catch(err => {
		  exList.value = []
		})
  }
  // ❗获取日志（入库）
  const getImportLogs = () => {
	axiosUtil.post('/api/import/log', {
	  importId: id.value,
	  timestamp: timestamp.value,
	  createTimestamp: createTimestamp.value,
	  operatorId: operatorId.value,
	  orgId: orgId.value,
	})
		.then(res => {
		  if (res.data.code === 0) {
			imList.value = res.data.data.map(item => {
			  return {
				...item,
				type: '入库',
				id: item.importId,
				productPrice: item.totalCost / item.totalCount,
				totalCost: item.totalCost.toFixed(4),
				totalCostAfterOff: item.totalCostAfterOff.toFixed(4),
				timetext: new Date(item.timestamp).toLocaleString(),
				createTimetext: new Date(item.createTimestamp).toLocaleString()
			  }
			})
		  } else {
			imList.value = []
		  }
		})
		.catch(err => {
		  imList.value = []
		})
  }
  
  
  //❗创建表格标题栏的核心函数
  const createCols = ({play}) => {
	return [
	  {
		title: "类型",
		key: "type"
	  },
	  {
		title: "明细ID",
		key: "id"
	  },
	  {
		title: "明细时间",
		key: "timetext",
		defaultSortOrder: 'descend',
		sorter: (row1, row2) => row2.timestamp - row1.timestamp
	  },
	  {
		title: "创建时间",
		key: "createTimetext",
		sorter: (row1, row2) => row2.createTimestamp - row1.createTimestamp
	  },
	  {
		title: "单位ID",
		key: "orgId"
	  },
	  {
		title: "单位名称",
		key: "orgName"
	  },
	  {
		title: "支付方式",
		key: "payWay",
		//以下3行：列筛选
		defaultFilterOptionValues: [], //默认开启的筛选，如果为空，表示显示全部。这应当是一个包含❗字符串❗的数组，如 useAppStore().defaultData.payWayList.map(item => item.label)
		filterOptions: useAppStore().defaultData.payWayList.slice(),
		filter(value, row) {
		  return ~row.payWay.indexOf(value)
		}
	  },
	  {
		title: "创建人ID",
		key: "operatorId"
	  },
	  /* {
		 title: "产品总数",
		 key: "totalCount"
	   },
	  {
		title: "产品总价",
		key: "totalCost",
		sorter: (row1, row2) => row1.totalCost - row2.totalCost
	  },
	  {
		title: "折扣率(%)",
		key: "offRate",
		sorter: (row1, row2) => row1.offRate - row2.offRate
	  },*/
	  {
		title: "折后总价",
		key: "totalCostAfterOff",
		sorter: (row1, row2) => row1.totalCostAfterOff - row2.totalCostAfterOff
	  },
	  {
		title: "详情",
		render(row) {
		  return h(
			  NButton,
			  {
				strong: true,
				tertiary: true,
				size: "small",
				onClick: () => play(row)
			  },
			  {default: () => "查看"}
		  );
		}
	  }
	]
  }
  //❗传入自定义play参数，创建表格标题栏
  const cols = createCols({
	play(row) {
	  //ℹ️调用自定义方法，显示记录详情的函数，唤醒模态框
	  showModalDetail(row)
	}
  })
  
  //❗分页参数
  const pagination = reactive({
	page: 1,
	pageSize: 8,
	showSizePicker: true,
	pageSizes: [5, 8, 10, 15],
	onChange: (page) => {
	  pagination.page = page
	},
	onUpdatePageSize: (pageSize) => {
	  pagination.pageSize = pageSize
	  pagination.page = 1
	}
  })
  
  
  const showModalQueryLog = ref(false)
  let rowToShow = ref(null)
  let rowListToShow = ref([])
  //显示记录详情的函数，唤醒模态框
  const showModalDetail = (row) => {
	showModalQueryLog.value = true
	rowToShow.value = {...row} //最好是克隆一下
	// console.log(row)
	
	//查询当前明细下的产品列表
	let a
	if (row.type === '出库') a = 'export'
	else if (row.type === '入库') a = 'import'
	
	axiosUtil.get(`/api/${a}/detail/${row.id}`)
		.then(res => {
		  if (res.data.code === 0) {
			rowListToShow.value = res.data.data
		  }
		})
	
  }


</script>

<template>
  <n-modal-provider>
	<div class="w-full flex flex-col gap-4" style="max-height:calc(100dvh - 60px);">
	  
	  <div class="w-full flex flex-col items-center gap-2">
		<div class=" w-full h-full px-4 pt-4 pb-2 flex flex-wrap items-center gap-2">
          <span class=" sort flex-1 max-w-[24.5%] min-w-fit flex items-center gap-0.5"><text
			  class="label font-bold min-w-fit">明细单号：</text><input class="value" placeholder="ID优先查询"
																	   v-model="id" /></span>
		  <span class=" sort flex-1 max-w-[24.5%] min-w-fit flex items-center gap-0.5"><text
			  class="label font-bold min-w-fit">明细类型：</text>
            <n-select :bordered="false"
					  :options="[{label: '全部', value: ''},{label: '出库', value: 'ex'}, {label: '入库', value: 'im'}]"
					  class="value"
					  size="small" v-model:value="type" /></span>
		  <span class=" sort flex-1 max-w-[24.5%] min-w-fit flex items-center gap-0.5"><text
			  class="label font-bold min-w-fit">明细日期：</text>
            <n-date-picker :bordered="false"
						   class="value" clearable size="small"
						   type="datetime"
						   v-model:value="timestamp" /></span>
		  <span class=" sort flex-1 max-w-[24.5%] min-w-fit flex items-center gap-0.5"><text
			  class="label font-bold min-w-fit">创建日期：</text>
            <n-date-picker :bordered="false"
						   class="value" clearable size="small"
						   type="datetime"
						   v-model:value="createTimestamp" /></span>
		  <span class=" sort flex-1 max-w-[24.5%] min-w-fit flex items-center gap-0.5"><text
			  class="label font-bold min-w-fit">创建人ID：</text><input class="value" v-model="operatorId" /></span>
		  <span class=" sort flex-1 max-w-[24.5%] min-w-fit flex items-center gap-0.5"><text
			  class="label font-bold min-w-fit">交易单位代码：</text><input class="value" v-model="orgId" /></span>
		</div>
		
		<div class="flex items-center gap-4 mb-2">
        <span
			class="search flex text-base  justify-center items-center select-none px-3 pt-1 pb-0.5 bg-amber-200 hover:bg-amber-400 rounded-2xl cursor-pointer text-gray-600 hover:text-white"
			@click.stop="handleClickToQuery">
          查询&刷新
        </span>
		  <span class="text-gray-500">共有 <text class="text-black font-bold">{{ data.length }}</text> 条记录</span>
		</div>
		<div class="w-full px-4 text-xs text-center text-gray-500">
		  <text>【明细日期、创建日期】为空时，不限相应的时间范围，否则查询的时间范围为
			<text class="text-black font-bold">选定的时间到其后30天内</text>
			，如【明细日期】选定为【2024-8-1
			00:00:00】，则查询的【明细日期】范围为【2024-6-1 00:00:00】到【2024-6-30 23:59:59】
		  </text>
		</div>
	  </div>
	  
	  <div class="flex-1 px-4 pb-8 flex flex-col">
		<n-data-table :bordered="true" :columns="cols" :data="data"
					  :pagination="pagination"
					  :single-column="false"
					  :single-line="true"
					  size="small"
					  striped
					  style="cursor:pointer; flex:1 1 auto;" />
	  </div>
	</div>
	
	<!--模态框-->
	<n-modal :mask-closable="true" v-model:show="showModalQueryLog">
	  <div class="modal-log min-w-[400px] max-w-[80vw] bg-white p-4 rounded flex flex-col  gap-2">
		<div class="font-bold text-base border-b mb-1 pb-2 flex items-center justify-between">
		  查看明细详情
		  <Check :size="28" :stroke-width="1.25" class="cursor-pointer hover:bg-gray-200 rounded-full p-1"
				 @click.stop="showModalQueryLog=false" />
		</div>
		
		<div class="relative flex-wrap  flex gap-1 border-b pl-0 pb-3 mb-1">
          <span class="property"><text class="label">明细ID：</text><text
			  class="value font-bold">{{ rowToShow.id }}</text></span>
		  <span class="property"><text class="label">类型：</text><text class="value text-red-400">{{
			  rowToShow.type
			}}</text></span>
		  <span class="property"><text class="label p-0">创建人：</text><text class="value">{{ rowToShow.operatorName
			}}</text></span>
		  <span class="property"><text class="label">仓库：</text><text
			  class="value">{{
			  rowToShow.cangkuName
			}}</text></span>
		  <span class="property"><text class="label">产品总数：</text><text class="value">{{
			  rowToShow.totalCount
			}}</text></span>
		  <span class="property"><text class="label">总价：</text><text class="value">{{
			  rowToShow.totalCost
			}}</text></span>
		  <span class="property"><text class="label">折扣率：</text><text class="value">{{
			  rowToShow.offRate
			}}</text></span>
		  <span class="property"><text class="label">折后总价：</text><text class="value">{{
			  rowToShow.totalCostAfterOff
			}}</text></span>
		</div>
		
		<n-scrollbar v-if="rowListToShow.length>0" style="max-height:40vh;">
		  <div
			  v-for="(item,index) in rowListToShow"
			  class="prod  max-w-fit flex items-center gap-2 mb-1 last:mb-0 cursor-pointer hover:bg-amber-200 px-1.5 py-1 rounded-lg ">
            <span
				style="padding-top:0.5px; font-size:0.7rem; width:18px; height:18px; border-radius:50%; display:flex; justify-content:center; align-items:center; background-color:#fbbf24;color:white; ">{{ index + 1
			  }}</span>
			<span class="prod-property"><text class="label">产品ID：</text><text
				class="value">{{ item.productId }}</text></span>
			<span class="prod-property"><text class="label">合同ID：</text><text
				class="value">{{ item.contactId }}</text></span>
			<span class="prod-property"><text class="label">名称：</text><text
				class="value">{{ item.productName }}</text></span>
			<span class="prod-property"><text class="label">规格：</text><text
				class="value">{{ item.productSpec }}</text></span>
			<span class="prod-property"><text class="label">计量单位：</text><text class="value">{{ item.productUnit
			  }}</text></span>
			<span class="prod-property"><text class="label">总数：</text><text class="value">{{ item.count
			  }}</text></span>
			<span class="prod-property"><text class="label">单价：</text><text class="value">{{ item.price
			  }}</text></span>
			<span class="prod-property"><text class="label">总价：</text><text
				class="value">{{ item.count * item.price }}</text></span>
		  </div>
		</n-scrollbar>
		<div v-else>
          <span class="text-red-400 flex items-center gap-1">查询详情失败，请重试！
            <RotateCw :size="20" :stroke-width="1.25" />
          </span>
		</div>
	  </div>
	</n-modal>
  </n-modal-provider>

</template>

<style lang="scss" scoped>
  
  .value::-webkit-input-placeholder {
	font-size: 0.75rem;
	color: #aaa;
	border: none;
  }
  
  .sort {
	.value {
	  border: 1px #ccc solid;
	  border-radius: 0.25rem;
	}
	
	input {
	  padding: 0.2rem 0.5rem;
	  outline: none;
	  cursor: default;
	  //background-color: red;
	}
  }
  
  .modal-log {
	.relative {
	  max-width: 100%;
	  
	  .property {
		flex: none;
		min-width: fit-content;
		font-size: 0.8rem;
		display: flex;
		align-items: center;
		background-color: var(--gray-100);
		padding: 0.125rem 0.375rem;
		border-radius: 0.25rem;
		//border: 1px solid #ccc;
		
		cursor: pointer;
		
		.label {
		  padding: 0;
		}
	  }
	}
	
	
	.prod {
	  &:hover > .prod-property {
		background-color: transparent !important;
	  }
	  
	  > .prod-property {
		//flex: 1 0 auto;
		min-width: fit-content;
		display: flex;
		align-items: center;
		gap: 0;
		padding: 0.1rem 0.5rem !important;
		background-color: var(--gray-100);
		border-radius: 0.25rem;
		font-size: 0.7rem;
		
		.label {
		  padding: 0;
		}
	  }
	}
	
  }
</style>
