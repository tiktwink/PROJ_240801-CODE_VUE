<script>
  import usePaperStore from "@/stores/usePaperStore.js";
  
  export default {
    data() {
      return {
        paperStore: usePaperStore(),
      }
    },
    computed: {
      paper() {
        return this.paperStore.paperScan
      }
    },
    mounted() {
    
    }
    
  }
</script>

<template>
  <div class="w-full max-h-full p-2">
    <p class="px-2 text-lg font-bold mb-2 mt-1">试卷信息</p>
    <div v-if="paperStore.paperScan!==''&&paperStore.paperType==='答卷'" class="paper px-6 ">
      <div class="flex flex-col gap-y-0.5">
        <span><text>试卷类型</text><text
            class="ml-1.5 font-normal rounded bg-info text-white cursor-pointer px-1.5 py-0.5"
            style="font-size:0.75rem;">答卷</text></span>
        <span><text>答卷ID：</text><text>{{ paper.id }}</text></span>
        <span><text>所属考试ID：</text><text>{{ paper.examId }}</text></span>
        <span><text>所属学生ID：</text><text>{{ paper.studentId }}</text></span>
        <span class=break-all><text>报考时间：</text><text>{{ paper.createTime }}</text></span>
        <span class=break-all><text>考试时间：</text><text>{{
            paper.examTime.slice(0, 19).replace('T', '')
          }}</text></span>
        <span><text>已参考：</text><text>{{ paper.tested === 0 ? '否' : '是' }}</text></span>
        <span v-if="paper.tested===1"><text>已导入：</text><text>{{ paper.hasUrl === 0 ? '否' : '是' }}</text></span>
        <span v-if="paper.hasUrl===1"><text>已评阅：</text><text>{{ paper.checked === 0 ? "否" : "是" }}</text></span>
        <span v-if="paper.hasUrl===1" class=break-all><text>评阅时间：</text><text>{{
            paper.checkTime.slice(0, 19).replace('T', '')
          }}</text></span>
        <span><text>审核信息：</text>
          <text :class="{'text-red':paper.code!==0&&paper.code!==1}"
                class="ml-1.5 font-normal text-sm rounded bg-info text-white cursor-pointer px-1.5 py-0.5"
                style="font-size:0.75rem;">{{
              paper.code === 0 ? '未审核(0)' : (paper.code === -1 ? '已审核，未通过(-1)' : (paper.code === 1 ? '已审核，通过(1)' : '异常(' + paper.code + ')'))
            }}</text></span>
      </div>
    </div>
    <div v-else-if="paperStore.paperScan!==''&&paperStore.paperType==='底卷'" class="paper px-6 ">
      <div class="flex flex-col gap-y-0.5">
        <span><text>试卷类型</text><text
            class="ml-1.5 font-normal rounded bg-info text-white cursor-pointer px-1.5 py-0.5"
            style="font-size:0.75rem;">底卷</text></span>
        <span><text>所属考试ID：</text><text>{{ paper.id }}</text></span>
        <span><text>所属考试名称：</text><text>{{ paper.name }}</text></span>
        <span class=break-all><text>考试时间：</text><text>{{
            paper.examTime.slice(0, 19).replace('T', '')
          }}</text></span>
        <span><text>所属考试地点：</text><text>{{ paper.pos }}</text></span>
        <span><text>所属科目ID：</text><text>{{ paper.subjectId }}</text></span>
        <span><text>所属科目名称：</text><text>{{ paper.subjectName }}</text></span>
        <span><text>相关试卷数：</text><text>{{ paper.num }}</text></span>
        <span><text>未导入试卷数：</text><text>{{ paper.num - paper.countHasUrl }}</text></span>
        <span><text>未批阅试卷数：</text><text>{{ paper.countUnchecked }}</text></span>
        <!-- <span ><text>已导入底卷：</text><text>{{ paper.hasUrlEmpty===0?'否':'是' }}</text></span>-->
        <span><text>已导入答案：</text><text>{{ paper.hasUrlRefer === 0 ? '否' : '是' }}
          <svg v-if="paper.hasUrlRefer" class="icon" height="16" p-id="9021" style="display:inline;"
               t="1713436103449" version="1.1" viewBox="0 0 1024 1024" width="16" xmlns="http://www.w3.org/2000/svg"><path
              d="M837.290667 464.408381L509.464381 798.598095a173.958095 173.958095 0 0 1-249.417143 0c-68.998095-70.363429-68.998095-184.56381 0-254.902857L592.749714 204.483048a104.886857 104.886857 0 0 1 150.430476 0c41.691429 42.520381 41.691429 111.494095 0 154.014476l-323.120761 329.386666a36.400762 36.400762 0 0 1-52.272762 0.024381c-14.628571-14.872381-14.628571-39.058286 0-53.955047l289.938285-295.643429-52.614095-53.00419-289.962667 295.643428c-43.349333 44.202667-43.349333 115.809524 0 159.98781a109.884952 109.884952 0 0 0 157.525334 0L795.794286 411.477333c70.460952-71.826286 70.460952-188.196571 0-260.022857a178.395429 178.395429 0 0 0-255.634286 0L207.433143 490.666667c-97.767619 99.718095-97.767619 261.266286 0 360.96a247.466667 247.466667 0 0 0 354.645333 0L889.904762 517.412571l-52.614095-53.00419z"
              fill="#05aefc" p-id="9022"></path></svg>
        </text>
        </span>
        <!-- <span v-if="paper.hasUrl===1"><text>已评阅：</text><text>{{ paper.checked===0?"否":"是" }}</text></span>-->
        <!-- <span class=break-all v-if="paper.hasUrl===1"><text>评阅时间：</text><text>{{ paper.checkTime.slice(0,19).replace('T','') }}</text></span>-->
        <span><text>所属考试审核信息：</text><text
            :class="{'text-red':paper.code!==0&&paper.code!==1}"
            class="ml-1.5 font-normal text-sm rounded bg-info text-white cursor-pointer px-1.5 py-0.5"
            style="font-size:0.75rem;">{{
            paper.code === 0 ? '未审核(0)' : (paper.code === -1 ? '已审核，未通过(-1)' : (paper.code === 1 ? '已审核，通过(1)' : '异常(' + paper.code + ')'))
          }}</text></span>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
  .text-red {
    color: #f00 !important;
    background: #e8e8e8 !important;
  }
  
  .paper {
    > div {
      background-color: #fff;
      border-radius: 0.5rem;
      padding: 0.5rem 0.45rem;
      
      > span {
        padding: 0.15rem 0.5rem;
        font-size: 0.79rem;
        border-radius: 0.5rem;
        //width:fit-content;
        cursor: pointer;
      }
      
      > span:hover {
        background-color: #bbf8ae;
        
      }
    }
    
    
  }
</style>
