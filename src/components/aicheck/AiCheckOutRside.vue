<script>
  import useCheckoutStore from "@/stores/useCheckoutStore.js";
  import useAppStore from "@/stores/useAppStore.js";
  
  export default {
    data() {
      return {
        checkoutStore: useCheckoutStore(),
      }
    },
    mounted() {
      useAppStore().sideOn = true
      document.getElementById('rside').style.visibility = 'visible'
    },
  }
</script>

<template>
  <div>这里是评阅结果页的边栏
    <div>
      <NScrollbar style="max-height:400px;">
        <div>
        </div>
      </NScrollbar>
    </div>
  </div>
</template>

<style lang="scss" scoped>

</style>
