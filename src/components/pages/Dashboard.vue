<script>
  import useAppStore from "@/stores/useAppStore.js";
  
  export default {
    data() {
      return {
        appStore: useAppStore(),
      }
    },
    mounted() {
      //隐藏右侧栏
      this.appStore.sideOn = false
      document.getElementById('rside').style.visibility = 'hidden'
    },
    unmounted() {
      //隐藏右侧栏
      this.appStore.sideOn = false
      document.getElementById('rside').style.visibility = 'visible'
    }
  }
</script>

<template>
  <div>这里是首页面板</div>
</template>

<style lang="scss" scoped>

</style>
