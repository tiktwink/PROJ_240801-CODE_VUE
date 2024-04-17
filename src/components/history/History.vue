<script>
  import useAppStore from "@/stores/useAppStore.js";
  import {axiosJson} from "@/apis/axios/axiosJson.js";
  import {NCheckbox, NCheckboxGroup, NSpace} from "naive-ui";
  import CheckLog from "@/components/history/CheckLog.vue";
  import QueryLog from "@/components/history/QueryLog.vue";
  import OtherLog from "@/components/history/OtherLog.vue";
  import CudLog from "@/components/history/CudLog.vue";
  
  export default {
    name: 'History',
    data() {
      return {
        appStore: useAppStore(),
        checks: [],
        entities: ['试卷'],
        type: 1,
        checkLog: [],
      }
    },
    computed: {
      compName() {
        if (this.type === 1) return 'CheckLog'
        else if (this.type === 2) return 'QueryLog'
        else if (this.type === 3) return 'CudLog'
        else if (this.type === 4) return 'OtherLog'
      }
    },
    mounted() {
      this.appStore.sideOn = false
      
      this.queryCheckLog()
    },
    methods: {
      queryCheckLog() {
        axiosJson.get('/api/check/vl-all')
        .then(res => {
          if (res.data.code === 0) {
            console.log(res.data.data)
            this.checkLog = res.data.data
            this.checkLog.reverse() //时间最近
          }
        })
      },
      refresh() {
        this.queryCheckLog()
      }
    },
    components: {
      CheckLog, QueryLog, OtherLog, CudLog,
      NCheckbox, NCheckboxGroup, NSpace
    },
  }
</script>

<template>
  <div class="flex h-full w-full px-4 py-4 bg-accent rounded-lg">
    <div class="w-[24%] h-full min-w-[330px] border-box p-1 relative" style="border-right:1px #ccc solid;">
      
      <p class="text-xl font-bold mb-4">筛选</p>
      <span class="absolute right-5 top-1.5 btn btn-sm btn-ghost bg-white border-none hover:bg-secondary"
            @click="refresh">
        <svg class="icon" height="16" p-id="3785" t="1713191560562" version="1.1"
             viewBox="0 0 1024 1024" width="16" xmlns="http://www.w3.org/2000/svg"><path
            d="M822.496 473.152a32 32 0 0 0-31.392 55.776l97.450667 54.848c20.32 11.434667 45.653333-2.005333 47.594666-25.248 1.674667-20.16 2.517333-35.573333 2.517334-46.528C938.666667 276.362667 747.637333 85.333333 512 85.333333S85.333333 276.362667 85.333333 512s191.029333 426.666667 426.666667 426.666667c144.106667 0 276.053333-72.032 354.752-189.536a32 32 0 1 0-53.173333-35.616C746.645333 813.461333 634.538667 874.666667 512 874.666667c-200.298667 0-362.666667-162.368-362.666667-362.666667s162.368-362.666667 362.666667-362.666667c197.098667 0 357.472 157.226667 362.538667 353.109334l-52.042667-29.290667z"
            fill="#515151" p-id="3786"></path></svg>
        刷新
      </span>
      <div class="relative w-[100%] flex justify-between pl-0 pr-4">
        <span :class="{'item-choosen':type===1}" class="btn btn-sm px-3 bg-white border-none hover:bg-secondary"
              @click="type=1">阅卷任务</span>
        <span :class="{'item-choosen':type===2}"
              class="btn btn-sm px-3 bg-white border-none hover:bg-secondary relative"
              @click="type=2">查询</span>
        <span :class="{'item-choosen':type===3}"
              class="btn btn-sm px-3 bg-white border-none hover:bg-secondary relative"
              @click="type=3">增删改</span>
        <span :class="{'item-choosen':type===4}" class="btn btn-sm px-3 bg-white border-none hover:bg-secondary"
              @click="type=4">其它</span>
      </div>
      <div v-show="type===3||type===2" class=" mt-2 ">
        <n-checkbox-group v-model:value="entities">
          <div class="flex w-full justify-center border-none items-center">
            <n-checkbox label="试卷" value="试卷"/>
            <n-checkbox label="考试" value="考试"/>
            <n-checkbox label="学生" value="学生"/>
            <n-checkbox label="学科" value="学科"/>
            <span class="btn btn-ghost btn-xs bg-white hover:bg-secondary ml-1 mr-2"
                  @click="entities=['试卷']">重置</span>
          </div>
        </n-checkbox-group>
      </div>
      <!--      <div>-->
      <!--        <span><text></text><input type="text"></span>-->
      <!--      </div>-->
    </div>
    
    <!--日志主区-->
    <div class="w-[76%] h-full min-h-[300px] border-box pl-4">
      
      <component :is="compName" v-model:check-log="checkLog">
        <CheckLog/>
      </component>
    
    </div>
  </div>
</template>

<style lang="scss" scoped>
  .item-choosen {
    background-color: #05aeec !important;
    color: #fff !important;
  }
</style>
