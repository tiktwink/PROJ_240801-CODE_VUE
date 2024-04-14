<template>
  <div :id="`${path}#default`" class="anchor-demo"
       style="position:fixed; top:100px; right:10px; display: flex">
    <div style="flex: 1"/>
    <t-anchor :affix-props="{ offsetTop: 150 }">
      <t-anchor-item href="#基础锚点" title="基础锚点"/>
      <t-anchor-item href="#多级锚点" title="多级锚点"/>
      <t-anchor-item href="#指定容器锚点" title="指定容器锚点"/>
      <t-anchor-item href="#特定交互锚点" title="特定交互锚点"/>
      <t-anchor-item href="#不同尺寸的锚点" title="不同尺寸的锚点"/>
    </t-anchor>
  </div>
  
  <div class="wrapper">
    <div id="基础锚点">1</div>
    <div id="多级锚点">2</div>
    <div id="指定容器锚点">3</div>
    <div id="特定交互锚点">4</div>
    <div id="不同尺寸的锚点">5</div>
  </div>
</template>
<script setup>
  import {computed, getCurrentInstance} from 'vue';
  
  import get from 'lodash/get';
  
  const {ctx} = getCurrentInstance();
  const path = computed(() => get(ctx, '$route.path', ''));
</script>
<style lang="scss" scoped>
  .anchor-demo {
    border: 1px solid transparent;
    padding: 20px;
    margin: -20px;
  }
  
  .anchor-demo:target {
    border-color: #1890ff;
  }
  
  
  .wrapper {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    padding-left: 100px;
    
    div {
      width: 600px;
      height: 400px;
      margin: 10px;
      border: 1px deepskyblue dashed;
      border-radius: 15px;
      padding: 1rem;
    }
    
    div:last-child {
      margin: 10px 10px 500px 10px;
    }
  }
</style>

