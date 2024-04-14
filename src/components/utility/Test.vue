<template>
  <div class="flex flex-col items-center ">
    <DvTest1/>
    <DividerHorizontal class="mt-2 mb-6 "/>
    <DvTest2/>
  </div>
</template>

<script>
  
  import DvTest1 from "@/components/datavisual/DvTest1.vue";
  import DvTest2 from "@/components/datavisual/DvTest2.vue";
  import DividerHorizontal from "@/components/utility/DividerHorizontal.vue";
  
  export default {
    components: {
      DividerHorizontal,
      DvTest1,
      DvTest2
    },
  }
</script>

<style lang="scss" scoped>

</style>
