<script>
  import {NCheckbox, NRadio, NRadioGroup, NSpace} from "naive-ui";
  
  export default {
    data() {
      return {
        chooserLeft: '时间最近'
      }
    },
    props: {
      checkLog: {
        type: Array,
        default: []
      },
    },
    mounted() {
      // console.log('here is CheckLog.vue!');
    },
    watch: {
      chooserLeft(newValue, oldValue) {
        this.checkLog.reverse()
      }
    },
    components: {
      NCheckbox, NRadioGroup, NSpace, NRadio
    },
  }
</script>

<template>
  <div>此处：阅卷日志
    
    <div class="p-2 bg-accent rounded-lg">
      <NScrollbar style="max-height:calc(100vh - 80px - 5.25rem); padding-bottom:0.25rem; border:0 solid #ccc;">
        
        <div class="log-wrapper flex border-none py-4 relative">
          
          <div class="board-left w-[50%] border-box" style="border-right:1px #ccc solid;">
            <div v-for="(check,index) in checkLog" v-show="index%2===0" class="log-item relative">
              <span class="relative top-0.5 left-0.5 bg-info text-white"
                    style="margin-right:0.25rem; width:1.45rem; height:1.45rem; cursor:default; border-radius:50%; display:flex;justify-content:center; align-items:center;">{{ index + 1 }}</span>
              <span class="text-sm"><text class="font-bold text-sm ">阅卷任务ID：</text>{{ check.id }}</span>
              <span class="text-sm"><text class="font-bold text-sm ">操作员ID：</text>{{ check.operatorId }}</span>
              <span class="text-sm"><text class="font-bold text-sm ">审核员ID：</text>{{ check.auditorId }}</span>
              <span class="text-sm"><text class="font-bold text-sm ">阅卷模式：</text><text
                  :class="{'text-info':check.mode==='exam','text-green-500':check.mode==='paper'}">{{
                  check.mode === 'paper' ? '阅试卷模式' : '阅考试模式'
                }}</text></span>
              <span class="text-sm"><text class="font-bold text-sm ">已审核：</text><text
                  :class="{'text-red':check.code===0}">{{ check.code === 0 ? '否' : '是' }}</text></span>
              <span v-if="check.code !== 0" class="text-sm"><text class="font-bold text-sm ">审核通过：</text><text
                  :class="{'text-red':check.code!==1}">{{ check.code === 1 ? '是' : '否' }}</text></span>
              <span v-if="check.mode==='paper'" class="text-sm"><text
                  class="font-bold text-sm ">交阅试卷数：</text>{{ check.papersTocheck.length }}</span>
              <span v-if="check.mode==='paper'" class="text-sm"><text
                  class="font-bold text-sm ">批阅试卷数：</text>{{ check.papersChecked.length }}</span>
              <span v-if="check.mode==='exam'" class="text-sm"><text
                  class="font-bold text-sm ">交阅考试数：</text>{{ check.examsTocheck.length }}</span>
              <span v-if="check.mode==='exam'" class="text-sm"><text
                  class="font-bold text-sm ">批阅考试数：</text>{{ check.examsChecked.length }}</span>
              <span class="text-sm"><text class="font-bold text-sm ">开始时间：</text>{{
                  check.submitTime.slice(0, 16).replace('T', ' ')
                }}</span>
              <span class="text-sm"><text
                  class="font-bold text-sm ">完成时间：</text>{{ check.submitTime.slice(0, 16).replace('T', ' ') }}</span>
            
            </div>
          </div>
          
          <div class="board-right w-[50%] border-box" style="border:0 #ccc solid;">
            <div v-for="(check,index) in checkLog" v-show="index%2===1" class="log-item">
              <span class="relative top-0.5 left-0.5 bg-info text-white"
                    style="margin-right:0.25rem; width:1.45rem; height:1.45rem; cursor:default; border-radius:50%;  display:flex;justify-content:center; align-items:center;">{{ index + 1 }}</span>
              <span class="text-sm"><text class="font-bold text-sm ">阅卷任务ID：</text>{{ check.id }}</span>
              <span class="text-sm"><text class="font-bold text-sm ">操作员ID：</text>{{ check.operatorId }}</span>
              <span class="text-sm"><text class="font-bold text-sm ">审核员ID：</text>{{ check.auditorId }}</span>
              <span class="text-sm"><text class="font-bold text-sm ">阅卷模式：</text><text
                  :class="{'text-info':check.mode==='exam','text-green-500':check.mode==='paper'}">{{
                  check.mode === 'paper' ? '阅试卷模式' : '阅考试模式'
                }}</text></span>
              <span class="text-sm"><text class="font-bold text-sm ">已审核：</text><text
                  :class="{'text-red':check.code===0}">{{ check.code === 0 ? '否' : '是' }}</text></span>
              <span v-if="check.code !== 0" class="text-sm"><text class="font-bold text-sm ">审核通过：</text><text
                  :class="{'text-red':check.code!==1}">{{ check.code === 1 ? '是' : '否' }}</text></span>
              <span v-if="check.mode==='paper'" class="text-sm"><text
                  class="font-bold text-sm ">交阅试卷数：</text>{{ check.papersTocheck.length }}</span>
              <span v-if="check.mode==='paper'" class="text-sm"><text
                  class="font-bold text-sm ">批阅试卷数：</text>{{ check.papersChecked.length }}</span>
              <span v-if="check.mode==='exam'" class="text-sm"><text
                  class="font-bold text-sm ">交阅考试数：</text>{{ check.examsTocheck.length }}</span>
              <span v-if="check.mode==='exam'" class="text-sm"><text
                  class="font-bold text-sm ">批阅考试数：</text>{{ check.examsChecked.length }}</span>
              <span class="text-sm"><text class="font-bold text-sm ">开始时间：</text>{{
                  check.submitTime.slice(0, 16).replace('T', ' ')
                }}</span>
              <span class="text-sm"><text
                  class="font-bold text-sm ">完成时间：</text>{{ check.submitTime.slice(0, 16).replace('T', ' ') }}</span>
            
            </div>
          </div>
          
          <div class="chooser absolute left-[52%] top-4 flex items-center">
            <n-radio-group v-model:value="chooserLeft">
              <div class="flex justify-center border-none items-center gap-x-2">
                <n-radio label="时间最近" value="时间最近"/>
                <n-radio label="时间最早" value="时间最早"/>
                <!--                <n-radio label="学生" value="学生"/>-->
                <!--                <n-radio label="学科" value="学科"/>-->
                <!--                <span class="btn btn-ghost btn-xs bg-white hover:bg-secondary ml-1 mr-2"-->
                <!--                      @click="chooserLeft='时间最近'">重置</span>-->
              </div>
            </n-radio-group>
          </div>
        </div>
      
      
      </NScrollbar>
    
    </div>
  
  </div>
</template>

<style lang="scss" scoped>
  .text-red {
    color: #f00 !important;
  }
  
  .log-wrapper {
    .board-left, .board-right {
      display: flex;
      flex-direction: column;
      //row-gap:0.2rem;
    }
    
    .board-right {
      padding-top: 2.5rem;
    }
    
    .log-item {
      padding: 0.35rem 0.2rem 0.35rem 0.35rem;
      //word-break: break-all;
      margin: 0 0.75rem 2rem;
      background-color: #fff;
      border-radius: 0.75rem;
      display: flex;
      //flex-direction: column;
      //row-gap: 0.15rem;
      column-gap: 0.1rem;
      flex-wrap: wrap;
      
      > span {
        width: fit-content;
        border-radius: 0.5rem;
        padding: 0.25rem 0.5rem;
        cursor: url("/search.png"), pointer;
      }
      
      > span:last-child {
        margin: 0;
      }
      
      > span:nth-child(1n+2):hover {
        background-color: #bbf8ae;
      }
    }
    
    //.log-item:hover {
    //
    //}
  }
</style>
