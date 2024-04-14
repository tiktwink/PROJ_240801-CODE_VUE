<script>
  import {NCheckbox, NCheckboxGroup, NScrollbar, NSpace} from 'naive-ui'
  import useAppStore from '../../stores/useAppStore.js'
  import useExamStore from "@/stores/useExamStore.js";
  
  export default {
    props: {
      exams: {
        type: Array,
        default: []
      },
    },
    data() {
      return {
        appStore: useAppStore(),
        examStore: useExamStore(),
        chooses: []
      }
    },
    methods: {
      chooseExam(item) {
        if(this.examStore.examsChoosen.some(exam => exam.id === item.id)){
          this.examStore.examsChoosen =  this.examStore.examsChoosen.filter(exam => exam.id!==item.id)
        }
        else{
          this.examStore.examsChoosen.push(item)
        }
      }
    },
    components: {
      NCheckbox,
      NCheckboxGroup,
      NSpace,
      NScrollbar
    },
    watch: {
      chooses: {
        deep: true, //深层监听
        handler(newValue, oldValue) {
          // console.log(newValue)
          this.examStore.examsChoosen = this.exams.filter((item, index) => newValue[index])
        }
        
        
      }
    },
  }
</script>

<template>
  <div class="aicheck-chooseout py-4 px-2" style="border-top:1px #ccc solid;">
    
    <div v-if="exams.length===0">
      <text>没有查询到考试信息 ..o_O</text>
    </div>
    <div v-else>
      <NScrollbar style="min-height:50px; max-height: 318px; padding: 0.8rem 0.9rem ;background-color:#fff; border-radius: 0.5rem; ">
        
        <div v-for="(item, index) in exams" :class="{'choosen-exam': examStore.examsChoosen.some(exam => exam.id===item.id)} "
             class="exam min-h-[60px] border-none rounded-xl mb-3 bg-[#f0f0f0] cursor-pointer flex flex-wrap items-center text-sm "
             @click="chooseExam(item)">
          <div><span class="font-bold">考试ID：</span>{{ item.id }}</div>
          <div><span class="font-bold">考试名称：</span>{{ item.name }}</div>
          <div><span class="font-bold">科目ID：</span>{{ item.subjectId }}</div>
          <div><span class="font-bold">科目名称：</span>{{ item.subjectName }}</div>
          <div><span class="font-bold">所属考试组：</span>{{ item.title }}</div>
          
          <div><span class="font-bold">考试时间：</span>{{ String(item.examTime).slice(0, 19).replace('T', " ") }}</div>
          <div><span class="font-bold">报考人数：</span>{{ item.num }}</div>
          <div><span class="font-bold">已考人数：</span>{{ item.num - item.countUntested }}</div>
          <div><span class="font-bold">已导入答卷数：</span>{{ item.countHasUrl }}</div>
          <div><span class="font-bold">已阅答卷数：</span>{{ item.num - item.countUnchecked }}</div>
          <div class=" "><span class="font-bold">已阅：</span>
            <text :class="{'text-red':item.checked===0}">{{ item.checked === 1 ? '是' : '否' }}</text>
          </div>
          
          <!--        <div><span class="font-bold">无效试卷：</span>{{ item.outCounts }}</div>-->
          <!--        <div class="flex "><span class="font-bold">班级：</span>-->
          <!--          <div v-for="iitem in item.classes">-->
          <!--            <div>班级ID：{{ iitem.id }}</div>-->
          <!--            <div>班级名：{{ iitem.title }}</div>-->
          <!--            <div>参考人数：-</div>-->
          <!--            <div>无效试卷：{{ iitem.outCounts }}</div>-->
          <!--          </div>-->
          <!--        </div>-->
        </div>
      
      </NScrollbar>
    </div>
   
  
  </div>
</template>

<style lang="scss" scoped>
  .exam {
    box-sizing: border-box !important;
    padding: 0.1rem;
  }
  
  .exam > div {
    margin: 0.2rem 0.25rem 0.1rem 0.5rem;
  }
  
  .exam:last-child {
    margin-bottom: 1.75rem;
  }
  .exam:only-child{
    margin-bottom:0!important;
  }
  
  .choosen-exam {
    border: 2px #05aeec solid !important;
    padding: calc(0.1rem - 2px) !important;
    //>div{
    //  margin: calc(0.2rem - 2px) calc(0.25rem - 2px) calc(0.1rem - 2px) calc(0.5rem - 2px);
    //}
    
  }
  
  .text-red {
    color: #f00 !important;
    font-weight: bold !important;
  }
</style>
