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
      
      axiosJson.get('/api/check/vl-all')
      .then(res => {
        if (res.data.code === 0) {
          console.log(res.data.data)
          this.checkLog = res.data.data
        }
      })
    },
    methods: {},
    components: {
      CheckLog, QueryLog, OtherLog, CudLog,
      NCheckbox, NCheckboxGroup, NSpace
    },
  }
</script>

<template>
  <div class="flex h-full w-full px-4 py-4 bg-accent rounded-lg">
    <div class="w-[24%] h-full min-w-[330px] border-box p-1" style="border-right:1px #ccc solid;">
      
      <p class="text-xl font-bold mb-2">筛选</p>
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
    <div class="w-[76%] h-full min-h-[300px] border-box ">
      
      <component :is="compName" :check-log="checkLog">
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
