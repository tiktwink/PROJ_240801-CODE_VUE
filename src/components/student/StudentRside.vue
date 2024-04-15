<script>
  import useStudentStore from "@/stores/useStudentStore.js";
  import axios from "axios";
  import UseUserStore from "@/stores/useUserStore.js";
  import {NScrollbar} from "naive-ui";
  
  export default {
    name: "StudentRside",
    data() {
      return {
        studentStore: useStudentStore(),
        studentDetail: '',
      }
    },
    watch: {
      'studentStore.studentsChoosen'(newValue, oldValue) {
        // console.log('仓库中的studentsChoosen值变更:')
        // console.log(newValue)
        if (newValue.length > 0) {
          axios.get(`/api/student/do-id/${newValue[0].id}`, {
            headers: {
              username: UseUserStore().username,
              token: UseUserStore().token
            }
          })
          .then(res => {
            if (res.data.code === 0) {
              this.studentDetail = res.data.data
            } else {
              this.studentDetail = ''
            }
          }).catch(err => {
            this.studentDetail = ''
          })
        }
      }
    },
    components: {
      NScrollbar,
    },
    // props: {
    //   student: {
    //     type: Object,
    //     required: false,
    //     default: null
    //   }
    // },
  }
</script>

<template>
  <div v-if="studentDetail!==''" class="p-3" @click.prevent.stop="">
    <div class="relative">
      <p class="font-bold text-lg mb-2">学生信息</p>
      <span class="absolute right-[-0.25rem] top-[-0.25rem] bg-info text-white btn btn-sm btn-ghost border-none hover:text-black hover:bg-secondary"
            style="">详情</span>
      <div class="student ">
        <span><text>ID：</text><text>{{ studentDetail.student.id }}</text></span>
        <span><text>姓名：</text><text>{{ studentDetail.student.name }}</text></span>
        <span><text>年级：</text><text>{{ studentDetail.student.grade }}</text></span>
      </div>
      <p class="font-bold text-lg mt-3 mb-2">相关考试({{ studentDetail.exams.length }})</p>
      <div class="py-2 px-1 bg-white rounded-lg">
        <NScrollbar class="bg-white rounded-lg" style="max-height:200px;">
          <div v-for="exam in studentDetail.exams" class="exam">
            <span><text>考试ID：</text><text>{{ exam.id }}</text></span>
            <span><text>考试名称：</text><text>{{ exam.name }}</text></span>
            <!--            <span><text>已参考：</text><text>{{ studentDetail.papers.some(paper => paper.examId === exam.id && paper.tested === 1) ? '是' : '否' }}</text></span>-->
          </div>
        </NScrollbar>
      </div>
      <p class="font-bold text-lg mt-3 mb-2">相关试卷({{ studentDetail.papers.length }})</p>
      <div class="py-2 px-1 bg-white rounded-lg">
        <NScrollbar class="bg-white rounded-lg" style="max-height:200px;">
          <div v-for="paper in studentDetail.papers" class="paper">
            <span><text>试卷ID：</text><text>{{ paper.id }}</text></span>
            <span><text>考试ID：</text><text>{{ paper.examId }}</text></span>
            <span><text>已参考：</text><text>{{ paper.tested === 1 ? '是' : '否' }}</text></span>
            <span><text>已导入：</text><text>{{ paper.hasUrl === 1 ? '是' : '否' }}</text></span>
            <span><text>已阅：</text><text>{{ paper.checked === 1 ? '是' : '否' }}</text></span>
            <!--            <span><text>反馈：</text><text>{{paper.checked===1?'是':'否'}}</text></span>-->
          </div>
        </NScrollbar>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
  .student, .exam, .paper {
    font-size: 0.8rem;
    cursor: pointer;
    padding: 0.35rem 0.25rem 0.35rem 0.5rem;
    margin-bottom: 0.5rem;
    background-color: #f5f5f5;
    border-radius: 0.5rem;
    margin-left: 0.25rem;
    margin-right: 0.15rem;
    //padding:0.1rem 0.15rem;
    
    > span {
      margin-right: 1rem;
      
      > text:first-child {
        font-weight: bold;
      }
    }
  }
  
  .exam:last-child, .paper:last-child {
    margin-bottom: 0;
  }
  
  .exam:hover, .paper:hover {
    background-color: #bbf8ae;
  }
</style>
