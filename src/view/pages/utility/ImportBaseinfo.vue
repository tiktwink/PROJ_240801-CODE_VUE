<script setup>
  import {NDatePicker, NSelect} from 'naive-ui'
  
  const baseinfo = defineModel("baseinfo", {
	type: Object,
	default: {}
  })
  
  let payWayList = [{
	label: '款到发货',
	value: '款到发货'
  }, {
	label: '货到付款',
	value: '货到付款'
  }, {
	label: '月结',
	value: '月结'
  }, {
	label: '周结',
	value: '周结'
  }, {
	label: '季结',
	value: '季结'
  }]
  
  const handleTimestamp = (value) => {
	console.log('123')
  }

</script>

<template>
  <div v-if="baseinfo" class="w-full h-full">
	
	<div class="grid w-full h-full p-4" style="grid-template-columns:  repeat(4, minmax(200px, 1fr))">
	  <span><text class="label">入库单号：</text><input :value="baseinfo.importId" class="value" disabled placeholder="后台自动生成" /></span>
	  <span><text class="label">入库日期：</text><n-date-picker class="value" clearable type="datetime" v-model:value="baseinfo.timestamp" /></span>
	  <span><text class="label">创建日期：</text><n-date-picker :value="baseinfo.createTimestamp" class="value" clearable disabled type="datetime" /></span>
	  <span><text class="label">创建人：</text><input :value="baseinfo.operatorName+'('+baseinfo.operatorId+')'" class="value" disabled /></span>
	  <span><text class="label">仓库名称：</text><input class="value" v-model="baseinfo.cangkuName" /></span>
	  <span><text class="label min-w-fit">结算方式：</text><NSelect :options="payWayList" class="value ma0x-w-[200px]" v-model:value="baseinfo.payWay" /></span>
	  <span><text class="label">供应商代码：</text><input class="value" v-model="baseinfo.orgId" /></span>
	  <span><text class="label">供应商名称：</text><input class="value" disabled v-model="baseinfo.orgName" /></span>
	  <!--      <span><text class="label">联系人：</text><input class="value" /></span>-->
	  <!--      <span><text class="label">联系电话：</text><input class="value"/></span>-->
	  <!--      <span><text class="label">地址：</text><input class="value"/></span>-->
	  <span style="grid-column: span 1;"><text class="label">摘要：</text><input class="value" /></span>
	  <span style="grid-column: span 3;"><text class="label">备注：</text><input class="value" /></span>
	</div>
  </div>
</template>

<style lang="scss" scoped>
  .grid {
	column-gap: 1rem;
	row-gap: 0.5rem;
	
	> span {
	  display: flex;
	  justify-content: space-between;
	  align-items: center;
	  
	  > .value {
		flex: 1 1 auto;
		outline: none;
	  }
	  
	  input {
		padding: 0.25rem 0.5rem;
		border-radius: 0.25rem;
		border: 1px #ccc solid;
	  }
	}
  }
</style>
