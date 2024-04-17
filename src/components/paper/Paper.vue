<script>
  import PaperBase from "@/components/paper/PaperBase.vue";
  import PaperFilled from "@/components/paper/PaperFilled.vue";
  
  export default {
    name: 'Paper',
    data() {
      return {
        compName: 'PaperBase'
      }
    },
    components: {
      PaperBase, PaperFilled
    },
    // computed: {
    //   compName() {
    //     return 'PaperBase'
    //   }
    // },
  }
</script>

<template>
  <div class="w-full h-full flex bg-accent rounded-lg">
    <div class="p-4 h-full w-[20%] min-w-[300px] border-box" style="border-right:1px #ccc solid;">
      边栏
    </div>
    
    <div class="p-4 h-full w-[80%] min-w-[300px]">
      主区
    </div>
  </div>
</template>

<style lang="scss" scoped>

</style>
