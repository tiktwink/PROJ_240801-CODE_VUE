<template>
  <Toast /> <!--position:top-right-->
  <Toast group="top-center" position="top-center" />
  
  <div class="relative bg-amber-100 w-full h-full min-h-[100dvh] flex flex-col items-center" id="app-root">
	
	<AppNav v-show="$route.name!=='login'" id="app-nav" />
	<div v-show="$route.name==='login'" class="w-full z-20 h-[60px] fixed top-0 flex justify-center items-center text-2xl font-bold">
	  XXX公司进销存管理系统
	</div>
	
	
	<div class="mt-[60px]  bg-amber-50   w-full h-full  flex-1 flex flex-col" id="app-main">
	  <router-view />
	</div>
  </div>

</template>

<script>
  import useAppStore from "@/stores/useAppStore.js";
  import useLayoutStore from "@/stores/useLayoutStore.js";
  import AppNav from "@/view/layout/AppNav.vue";
  
  export default {
	components: {AppNav},
	data() {
	  return {
		appStore: useAppStore(),
		layoutX: useLayoutStore()
	  }
	},
	methods: {},
	
  }
</script>

<style lang="scss" scoped>
  
  
  /* 覆盖，左侧抽屉的动画时长 */
  .drawer-side > *:not(.drawer-overlay) {
	transition-duration: 0.4s;
  }
  
  .drawer-side {
	
	.current {
	  background-color: #fff !important;
	  color: #000 !important;
	}
	
	
	.menu:first-child {
	  li {
		border-radius: 0.5rem;
		transition: all 1s linear;
		color: #fff;
		font-weight: bold;
		font-size: 1rem;
	  }
	  
	  li:hover {
		color: #000;
		background-color: #bcf8ae;
		border-radius: 0.5rem;
		transition: all 0.4s linear;
	  }
	}
	
	.menu:last-child li {
	  border-radius: 0.5rem;
	  width: fit-content;
	  font-weight: normal;
	  font-size: 0.85rem;
	  color: #fff;
	}
	
	.menu:last-child li:hover {
	  color: #000;
	  background-color: #bcf8ae;
	}
	
	.divider {
	  width: 100%;
	  height: 2px;
	  max-height: 2px;
	  border: 1px solid #fafafa;
	  margin: 0;
	}
	
	.absolute {
	  position: absolute;
	  bottom: 0;
	  
	  .info-dot {
		border-radius: 50%;
		width: 10px;
		height: 10px;
	  }
	}
  }
</style>
