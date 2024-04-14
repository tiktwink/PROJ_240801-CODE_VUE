<script>
  import UseExamStore from "@/stores/useExamStore.js";
  import {getMyDate} from "@/utils/datetime.js";
  import {NScrollbar} from "naive-ui";
  import axios from "axios";
  import useUserStore from "@/stores/useUserStore.js";
  import useCheckoutStore from "@/stores/useCheckoutStore.js";
  
  export default {
    data() {
      return {
        checkoutStore: useCheckoutStore(),
        examStore: UseExamStore(),
        exams: [],
        papers: [],
        mode: 'exam',
        timeText: getMyDate(Date.now()),
      }
    },
    mounted() {
      this.mode = this.checkoutStore.mode
      
      // console.log('123')
      // console.log(this.mode)
      
      if (this.mode === 'exam') {
        console.log('exam!!')
        this.checkoutStore.exams.forEach(exam => {
          axios.get(`/api/paper/indexs?examId=${exam.id}`, {
            headers: {
              username: useUserStore().username,
              token: useUserStore().token,
            }
          }).then(res => {
            if (res.data.code === 0) {
              this.exams.push({
                ...exam,
                'papers': res.data.data
              })
            }
          })
        })
      } else if (this.mode === 'paper') {
        this.checkoutStore.papers.forEach(item => {
          axios.get(`/api/paper/DS?paperId=${item.id}`, {
            headers: {
              username: useUserStore().username,
              token: useUserStore().token
            }
          })
          .then(res => {
            if (res.data.code === 0) {
              this.papers.push(res.data.data)
            }
          })
        })
      }
    },
    
    unmounted() {
      // this.examStore.examsChoosen = []
    },
    components: {
      NScrollbar,
    },
  }
</script>

<template>
  <div class="bg-accent h-[100vh] relative">
    <span class="absolute top-6 left-4 h-[36px] px-4 text-lg  btn btn-sm bg-white border-none hover:bg-secondary"
          @click="$router.push('/')">返回首页</span>
    <span class="absolute top-6 right-4 h-[36px] px-4 text-lg btn btn-sm bg-white border-none hover:bg-secondary"
          @click="$router.push('/aicheck')">继续阅卷</span>
    <p class="text-3xl font-bold text-center py-4 max-h-[5rem]">评阅详情</p>
    <div class="flex flex-wrap justify-center gap-x-8 pb-2 mt-1 mb-1 max-h-[3rem]">
      <span class="text-lg flex items-center "><text
          class="font-bold text-xl">评阅模式：</text>{{ mode === 'exam' ? '阅考试模式' : '阅试卷模式' }}</span>
      <span class="text-lg flex items-center "><text class="font-bold text-xl">评阅时间：</text>{{ timeText }}</span>
      <span v-if="mode==='exam'" class="text-lg flex items-center "><text
          class="font-bold text-xl">评阅考试数：</text>{{ exams.length }}</span>
      <span v-if="mode==='exam'" class="text-lg flex items-center "><text
          class="font-bold text-xl">评阅试卷数：</text>{{ exams.reduce((sum, item) => sum + item.num, 0) }}</span>
      <span v-if="mode==='paper'" class="text-lg flex items-center "><text
          class="font-bold text-xl">评阅试卷数：</text>{{ papers.length }}</span>
      <span class="text-lg flex items-center "><text class="font-bold text-xl">评阅操作人ID：</text>1001</span>
    </div>
    
    <div v-if="mode==='exam'">
      <NScrollbar
          style="box-sizing: border-box;background-color:#fff; padding:1rem 1.5rem; border:0 #f00 solid; height:calc(100vh - 8rem); width: calc(100vw - 2rem); margin-left:1rem; border-radius:0.75rem;">
        <p class="text-xl font-bold mb-4 mx-6">本次评阅的各考试信息</p>
        <div v-for="exam in exams" class="exam px-0 mb-4">
          <div class="px-2 flex gap-x-6 text-lg">
            <span class="flex items-center"><text class="font-bold">考试ID：</text>{{ exam.id }}</span>
            <span class="flex items-center"><text
                class="font-bold">考试时间：</text>{{ exam.examTime.slice(0, 19).replace('T', " ") }}</span>
            <span class="flex items-center"><text class="font-bold">考试地点：</text>{{ exam.examPos }}</span>
            <span class="flex items-center"><text class="font-bold">考试人数：</text>{{ exam.num }}</span>
            <span class="flex items-center"><text class="font-bold">考试科目：</text>{{ exam.subjectName }}</span>
            <span class="flex items-center"><text class="font-bold">科目ID：</text>{{ exam.subjectId }}</span>
            <!--          <span><text>阅卷时间：</text>{{exam.subjectId}}</span>-->
            <!--          <span><text>是否已阅完：</text>{{exam.subjectId}}</span>-->
          </div>
          <p class="text-lg font-bold mx-2 mt-4">该考试的各试卷信息</p>
          <NScrollbar style="max-height: 300px; border-radius:0.5rem; background-color:#fff; padding:1rem 0.5rem;">
            <div v-for="paper in exam.papers" class="paper mb-2 bg-accent hover:bg-secondary">
              <span><text>试卷ID：</text><text>{{ paper.id }}</text></span>
              <span><text>考生ID：</text><text>{{ paper.studentId }}</text></span>
              <span><text>考生姓名：</text><text>{{ paper.studentName }}</text></span>
              <span><text>是否参考：</text><text
                  :class="{'text-red':paper.tested===0}">{{ paper.tested === 0 ? '否' : '是' }}</text></span>
              <span><text>是否已导入：</text><text
                  :class="{'text-red':paper.hasUrl===0}">{{ paper.hasURL === 0 ? '否' : '是' }}</text></span>
              <span><text>是否已阅：</text><text
                  :class="{'text-red':paper.checked===0}">{{ paper.checked === 0 ? '否' : '是' }}</text></span>
            </div>
          </NScrollbar>
        </div>
      </NScrollbar>
    </div>
    
    <div v-else-if="mode==='paper'">
      <n-scrollbar
          style="box-sizing: border-box;background-color:#fff; padding:1rem 1.5rem; border:0 #f00 solid; height:calc(100vh - 8rem); width: calc(100vw - 2rem); margin-left:1rem; border-radius:0.75rem;">
        <div v-for="paper in papers"
             class="paper-paper cursor-pointer rounded-lg bg-accent hover:bg-secondary py-1.5 pl-4 pr-2 flex justify-between flex-wrap items-center gap-x-4 gap-y-2">
          <span><text>试卷ID：</text><text>{{ paper.id }}</text></span>
          <span><text>考试ID：</text><text>{{ paper.examId }}</text></span>
          <span><text>考生ID：</text><text>{{ paper.studentId }}</text></span>
          <span><text>考生姓名：</text><text>{{ paper.studentName }}</text></span>
          <span><text>参考时间：</text><text>{{ paper.testTime||'-' }}</text></span>
          <span><text>评阅时间：</text><text>{{ paper.checkTime.slice(0,19).replace('T'," ") }}</text></span>
         
          <span><text>评阅结果：</text><text>{{ paper.judge === '0' ? '正确' : '错误' }}</text></span>
          <span><text>评阅得分：</text><text class="text-red">{{ paper.judgeScore }}</text></span>
          <span><text>题目总分：</text><text>{{ paper.baseScore }}</text></span>
          <span
              class="mx-0 btn btn-sm btn-ghost border-none bg-white text-info hover:text-black hover:bg-info hover:text-white">详情</span>
        </div>
      </n-scrollbar>
    </div>
  
  
  </div>
</template>

<style lang="scss" scoped>
  .text-red {
    color: #f00 !important;
  }
  
  .exam {
    border-top: 1px #ccc solid;
    padding: 1rem;
    
    .paper {
      margin: 0 0 0.75rem;
      padding: 0.5rem 1.5rem;
      border-radius: 0.5rem;
      display: flex;
      flex-wrap: wrap;
      column-gap: 1.5rem;
      
      > span {
        font-size: 0.9rem;
        display: flex;
        align-items: center;
        //height:30px;
        
        text:first-child {
          font-size: 0.9rem;
          font-weight: bold;
        }
      }
    }
    
    .paper:last-child {
      margin-bottom: 0;
    }
  }
  
  .paper-paper {
    > span {
      > text:first-child {
        font-weight: bold;
      }
      
      > text:last-child {
        font-weight: normal;
      }
    }
    
    > span:last-child {
      font-weight: normal;
    }
  }
</style>
