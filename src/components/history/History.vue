<script>
  import useAppStore from "@/stores/useAppStore.js";
  import {axiosJson} from "@/apis/axios/axiosJson.js";
  
  export default {
    name: 'History',
    data() {
      return {
        appStore: useAppStore(),
        checks: [],
      }
    },
    mounted() {
      this.appStore.sideOn = false
      
      axiosJson.get('/api/check/vl-all')
      .then(res => {
        if (res.data.code === 0) {
          console.log(res.data.data)
        }
      })
      
      
    },
  }
</script>

<template>
  <div class="flex h-full w-full px-4 py-4 bg-accent rounded-lg">
    <div class="w-[25%] h-full min-h-[300px] border-box " style="border-right:1px #ccc solid;">
    
    </div>
    <div class="w-[75%] h-full min-h-[300px] border-box "></div>
  </div>
</template>

<style lang="scss" scoped>

</style>
