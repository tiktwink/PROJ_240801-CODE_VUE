<script>
  
  import UseUserStore from "@/stores/useUserStore.js";
  import useUserStore from "@/stores/useUserStore.js";
  import {CornerUpLeft, Landmark, LibraryBig, PackageSearch, Settings2, SlidersHorizontal} from 'lucide-vue-next'
  import {NAvatar, NPopover} from "naive-ui";
  
  export default {
	methods: {UseUserStore},
	data() {
	  return {
		userX: useUserStore(),
		// appX:useAppStore()
	  }
	},
	mounted() {
	  // console.log(this.$route.path.slice(0,4));
	},
	components: {
	  Settings2,
	  PackageSearch, NPopover, LibraryBig, SlidersHorizontal, NAvatar, CornerUpLeft, Landmark
	},
  }
</script>

<template>
  <div
	  class="fixed top-0  bg-white h-[60px] border-b-2 border-b-amber-500 z-10 w-full flex justify-between items-center gap-8 px-8 cursor-pointer">
	
	<div
		class="w-fit flex-initial flex justify-end items-center gap-2 text-sm text-gray-50 bg-amber-500 px-2.5 pb-1 pt-[5px] rounded-2xl">
      <span v-show="$route.name!=='首页'" @click="$router.go(-1)" title="返回上一路由"><CornerUpLeft :size="18"
																									 :stroke-width="1.25" /></span>
	  <span class=""> {{ $route.name }}</span>
	</div>
	<div class="flex-1 flex justify-end gap-4">
	  <n-popover trigger="hover">
		<template #trigger>
        <span :class="{'bg-amber-100 hover:bg-white' : $route.path.slice(0,4)==='/ope'}"
			  class="flex px-1.5 py-1 rounded items-center gap-1  hover:text-amber-500">
          <PackageSearch :size="20" :stroke-width="1.25" />出入库</span>
		</template>
		<div class="flex flex-col gap-2">
		  <text class="hover:bg-amber-500 hover:text-white cursor-pointer px-3 py-1 rounded"
				@click.stop="$router.push('/operation/export')">
			出库
		  </text>
		  <text class="hover:bg-amber-500 hover:text-white cursor-pointer px-3 py-1 rounded"
				@click.stop="$router.push('/operation/import')">
			入库
		  </text>
		  <text class="hover:bg-amber-500 hover:text-white cursor-pointer px-3 py-1 rounded"
				@click.stop="$router.push('/operation/oplog')">
			明细
		  </text>
		</div>
	  </n-popover>
	  
	  <n-popover trigger="hover">
		<template #trigger>
        <span :class="{'bg-amber-100 hover:bg-white' : $route.path.slice(0,4)==='/pro'}"
			  class="flex px-1.5 py-1 rounded items-center gap-1  hover:text-amber-500"
			  @click.stop="$router.push('/product/input')">
          <LibraryBig :size="20" :stroke-width="1.25" />产品管理</span>
		</template>
		<div class="flex flex-col gap-2">
		  <text class="hover:bg-amber-500 hover:text-white cursor-pointer px-3 py-1 rounded" @click.stop="$router.push('/product/input')">
			录入与修改
		  </text>
		  <text class="hover:bg-amber-500 hover:text-white cursor-pointer px-3 py-1 rounded"
				@click.stop="$router.push('/product/input')">库存查询
		  </text>
		</div>
	  </n-popover>
	  
	  <span :class="{'bg-amber-100 hover:bg-white' : $route.path.slice(0,4)==='/org'}"
			class="flex px-1.5 py-1 rounded items-center gap-1 hover:text-amber-500" @click.stop="$router.push('/org')"><Landmark
		  :size="20" :stroke-width="1.25" />单位管理</span>
	  <span :class="{'bg-amber-100 hover:bg-white' : $route.path.slice(0,4)==='/set'}"
			class="flex px-1.5 py-1 rounded items-center gap-1 hover:text-amber-500" @click.stop="$router.push('/set')"><SlidersHorizontal
		  :size="20" :stroke-width="1.25" />设置</span>
	  
	  <n-tooltip trigger="hover">
		<template #trigger>
           <span :class="{'bg-amber-100 hover:bg-white' : $route.path.slice(0,4)==='/use'}"
				 class="flex px-1.5 py-1 rounded items-center gap-1 hover:text-amber-500"
				 @click.stop="$router.replace('/login'); UseUserStore().token=''"><n-avatar
			   round size="small"
			   src="http://localhost:5173/unlogin-avatar.jpg"
			   style="max-width: 20px; max-height:20px; " />{{ userX.usernick }}</span>
		</template>
		点击退出登录(本地token清空，需重新验证密码)
	  </n-tooltip>
	
	</div>
  </div>
</template>

<style lang="scss" scoped>

</style>
