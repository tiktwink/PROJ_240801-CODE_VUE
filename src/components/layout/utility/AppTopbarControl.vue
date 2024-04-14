<script>
  import useAppStore from "@/stores/useAppStore.js";
  
  export default {
    data() {
      return {
        appStore: useAppStore()
      }
    },
    methods: {
      handleMouseEnter() {
        let apptopbar = document.getElementById('apptopbar')
        
        setTimeout(() => {
          apptopbar.style.top = '0'
          apptopbar.style.transition = 'top 0.65s'
        }, 1)
      }
    },
  }
</script>

<template>
  <div id="apptopbarcontrol" class="dark:bg-[#121212]"
       style="z-index:9999; position:fixed; top:0;width:100vw;" @mouseenter="handleMouseEnter">
    <div v-if="appStore.apptopbarControl===2"
         class="flex justify-center items-center px-12 text-black/50 mx-0.5"
         style="height: 10px; font-size:10px; background-image:linear-gradient(to left,#ff5678,#4090ff,#bbea44,#6666ff,#00a9ff,#ffffff,#ff2d2d,#53df5f,#f18f6b,#e41eec,#ffc34b); border-radius: 10px; ">
    </div>
    <div v-else-if="appStore.apptopbarControl===1"
         class="flex justify-center items-center px-12 text-black dark:text-white mx-0.5"
         style="height: 10px; font-size:10px; font-weight: bold;border-radius: 10px; ">
      ···
    </div>
    <div v-else-if="appStore.apptopbarControl===0"
         class="flex justify-center  items-center px-12 text-black/ mx-0.5"
         style="height: 10px; font-size:10px; border-radius: 10px;  background-color:transparent;">
    
    </div>
  </div>
</template>

<style lang="scss" scoped>

</style>
