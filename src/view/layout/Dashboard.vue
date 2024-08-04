<script>
  import useAppStore from "@/stores/useAppStore.js";
  import useLayoutStore from "@/stores/useLayoutStore.js";
  
  export default {
	data() {
	  return {
		appStore: useAppStore(),
		layoutX: useLayoutStore()
	  }
	},
	mounted() {
	
	},
	
  }
</script>

<template>
  <div class="flex-1 bg-amber-50  w-full h-full flex justify-center p-4">这里是首页面板</div>
</template>

<style lang="scss" scoped>

</style>
