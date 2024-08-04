<script>
  import {axiosUtil} from "@/apis/axios/axiosUtil.js";
  import {NotebookPen} from 'lucide-vue-next'
  import {NSelect} from 'naive-ui'
  
  export default {
	data() {
	  return {
		hadSearch: false,
		
		org: {
		  orgId: '',
		  orgName: '',
		  orgType: '客户',
		},
		searchList: [],
		selectedOrg: {
		  orgId: '',
		  orgName: '',
		  orgType: '客户'
		},
		units: [
		  {
			label: '客户',
			value: '客户'
		  },
		  {
			label: '供应商',
			value: '供应商'
		  },
		]
	  }
	},
	mounted() {
	  this.trySearch();
	},
	watch: {
	  org: {
		deep: true,
		handler(newVal, oldVal) {
		  this.hadSearch = false
		}
	  }
	},
	computed: {
	  onUpdateMode() {
		// 处于更新模式，可能发生了修改也可能没有
		return (this.org.orgId !== '') && (this.selectedOrg?.orgId === this.org.orgId)
	  },
	  onUpdating() {
		// 处于更新模式，且已经发生了修改
		return (this.org.orgId !== '') && (this.org.orgName !== this.selectedOrg.orgName || this.org.orgType !== this.selectedOrg.orgType)
	  }
	},
	components: {
	  NSelect, NotebookPen
	},
	methods: {
	  trySearch() {
		this.datasetWhenSearch()
		
		axiosUtil.post('/api/org/search', this.org)
			.then(res => {
			  if (res.data.code === 0) {
				let list = res.data.data
				this.searchList = list.map(item => {
				  return {
					...item,
					orgId: this.calId(item.orgId)
				  }
				})
			  }
			})
		
	  },
	  handleSelect(item) {
		
		if (this.selectedOrg.orgId !== item.orgId) {
		  this.selectedOrg = {...item} //❗复制而非引用
		  this.org = {...item} //❗复制而非引用
		} else {
		  this.selectedOrg = {
			orgId: '',
			orgName: '',
			orgType: '客户'
		  }
		  this.org = {
			orgId: '',
			orgName: '',
			orgType: '客户'
		  }
		}
	  },
	  //搜索前对相关参数进行设置/重置
	  datasetWhenSearch() {
		this.hadSearch = true
		this.selectedOrg = {
		  orgId: '',
		  orgName: '',
		  orgType: '客户',
		}
	  },
	  //产品id补零函数
	  calId: (id) => {
		let res = id.toString()
		let len = 6 - res.length
		
		if (len > 0) {
		  for (let i = 0; i < len; i++) {
			res = '0' + res
		  }
		}
		return res
	  },
	  handleRightButtonClick() {
		// 直接尝试录入（未处于更新模式，且要求输入的有效值均不为空）
		if (!this.onUpdateMode && this.org.orgName !== '' && this.org.orgType !== '') {
		  //尝试录入
		  axiosUtil.post('/api/org/add', this.org)
			  .then(res => {
				if (res.data.code === 0) {
				  this.org.orgId = this.calId(res.data.data)
				  this.selectedOrg = {...this.org}
				  this.searchList = [this.org]
				  
				  this.$toast.add({
					severity: 'success',
					detail: '录入成功',
					life: 1500
				  })
				  
				} else {
				  this.$toast.add({
					severity: 'error',
					detail: '录入失败',
					life: 2000
				  })
				}
			  })
			  .catch(err => {
				this.$toast.add({
				  severity: 'error',
				  detail: '录入失败',
				  life: 2000
				})
			  })
		}
		// 处于更新模式，且发生了修改
		else if (this.onUpdating) {
		  axiosUtil.post('/api/org/update', this.org)
			  .then(res => {
				if (res.data.code === 0) {
				  this.org = {...res.data.data, orgId: this.calId(res.data.data.orgId)}
				  
				  this.$toast.add({
					severity: 'success',
					detail: '更新成功',
					life: 1500
				  })
				} else {
				  this.$toast.add({
					severity: 'error',
					detail: '更新失败',
					life: 2000
				  })
				}
			  })
			  .catch(err => {
				this.$toast.add({
				  severity: 'error',
				  detail: '更新失败',
				  life: 2000
				})
			  })
		}
	  }
	  
	},
  }
</script>

<template>
  <div class="w-full h-full flex flex-col flex-wrap justify-center items-center px-8 py-6 gap-4">
	
	<div class="flex justify-center items-center gap-x-8 border-b border-b-gray-400 pb-6">
	  <div class="sorts relative  flex-auto flex justify-end items-center gap-x-4 pr-4 gap-y-3 flex-wrap">
		<span class="sort">代码：<input :disabled="onUpdateMode" placeholder="ID优先查询" v-model.trim="org.orgId" /></span>
		<span class="sort">名称：<input v-model.trim="org.orgName" /></span>
		<span class="sort">类型：<n-select :options="units" default-value="客户" size="small" style="outline:none!important;  width:90px;" v-model:value="org.orgType" /></span>
		<!--      <span class="absolute top-[50px] w-full justify-center flex items-center text-gray-400 text-[0.7rem]">TIP:不输入条件，直接查询，可以浏览所有产品</span>-->
	  
	  </div>
	  
	  <div class=" flex-auto  flex justify-start items-center gap-3">
		
		<div @click="trySearch" class="search flex text-base justify-center items-center select-none  bg-amber-200 hover:bg-amber-400 rounded-2xl cursor-pointer text-gray-600 hover:text-white">
		  查询&刷新
		</div>
		
		<div @click="handleRightButtonClick" :class="{
          ' hover:bg-amber-500 hover:text-white' : !onUpdateMode&&org.orgName!==''&&org.orgType!=='' , ' !bg-red-400 !text-white':onUpdating
        }" class="w-fit h-fit select-none text-[0.75rem] px-2.5 pt-1 pb-[3px] rounded-lg flex justify-center items-center bg-gray-300 text-white cursor-pointer">
		  {{ !onUpdateMode ? '直接尝试录入' : onUpdating ? '提交修改' : '< 请修改' }}
		</div>
	  </div>
	
	</div>
	
	
	<div class="w-full flex flex-col px-4 pt-2 items-center ">
	  
	  <div v-if="searchList.length===0" class="mb-6 text-red-300 font-bold flex flex-col items-center gap-4">
		什么都没有查到..o_O
		<span class="register select-none px-3 py-1.5 flex items-center gap-1 bg-amber-200 hover:bg-amber-500 rounded-3xl cursor-pointer text-gray-600 hover:text-white" @click.stop="handleRightButtonClick"><NotebookPen :size="20" :stroke-width="1.25" />
          录入</span>
	  </div>
	  
	  
	  <div v-if="searchList.length>=0" class="w-full flex items-center flex-col gap-2">
		<div class="mb-2 text-amber-500 text-[0.8rem]">
		  共查询到
		  <text class="text-black">{{ searchList.length }}</text>
		  条有效记录，点击记录后可以在上方输入域
		  <text class="text-black ">修改</text>
		  产品信息(单位代码除外)
		</div>
		<div class="scroll-title font-bold text-amber-500 pl-2  w-full  " style="max-height:40px;min-height:40px; max-width:calc(100dvw - 40rem); min-width:320px; flex:1 1 auto;">
		  <span class="id">单位代码</span>
		  <span class="name">单位名称</span>
		  <span class="code">单位类型</span>
		</div>
	  </div>
	  
	  <NScrollbar v-if="searchList.length>=0" class="bg-white border-amber-500 border rounded-lg " style="max-height:calc(100dvh - 220px - 4rem);min-height:50px; max-width:calc(100dvw - 40rem); min-width:320px; flex:1 1 auto;">
		<div v-for="(item,index) in searchList" :class="{'!text-white !bg-amber-400':searchList.length>1&&org.orgId===item.orgId}" :key="index" class="scroll-item hover:bg-gray-200 cursor-pointer w-full px-4 py-2 border-b border-b-gray-400 last:border-none " @click.stop="handleSelect(item)">
		  <span class="id">{{ item.orgId }}</span>
		  <span class="name">{{ item.orgName }}</span>
		  <span class="type">{{ item.orgType }}</span>
		</div>
	  </NScrollbar>
	</div>
  </div>
</template>

<style lang="scss" scoped>
  .sorts {
	.sort {
	  flex: 0 1 auto;
	  display: flex;
	  align-items: center;
	  user-select: none;
	  
	  > input {
		flex: 1 1 auto;
		outline: none;
		max-width: 110px;
		border-radius: 0.25rem;
		padding: 0.2rem 0.5rem;
		display: flex;
		align-items: center;
		cursor: default;
		border: 0.5px #ddd solid;
	  }
	  
	  &:first-child {
		input::-webkit-input-placeholder {
		  font-size: 0.75rem;
		}
		
		input {
		  flex: 1 1 auto;
		  max-width: 125px;
		  text-indent: 0.5ch;
		}
	  }
	  
	  &:nth-child(3) {
		> input {
		  min-width: 200px;
		  //border:1px red solid;
		}
	  }
	}
  }
  
  .search {
	flex: 0 1 auto;
	padding: 0.4rem 0.75rem 0.25rem;
	
  }
  
  .scroll-title, .scroll-item {
	padding-inline: 0;
	width: 100%;
	display: flex;
	align-items: center;
	justify-content: space-between;
	
	> span {
	  //border:1px solid rgba(0,0,0,0.5);
	  displaY: flex;
	  justify-content: center;
	  
	  &:not(:last-child) {
		border-right: 1px solid rgba(0, 0, 0, 0.5);
	  }
	}
	
	> span:nth-child(1) {
	  flex: 1 1 30%;
	}
	
	> span:nth-child(2) {
	  flex: 1 1 50%;
	}
	
	> span:nth-child(3) {
	  flex: 1 1 20%;
	}
  }


</style>
