<script>
  import useAppStore from "@/stores/useAppStore.js";
  import axios from "axios";
  import useUserStore from "@/stores/useUserStore.js";
  import {NScrollbar} from "naive-ui";
  
  export default {
    data() {
      return {
        appStore: useAppStore(),
        students: [],
        //query
        id: '',
        name: '',
        classId: '',
        grade: '',
        //add
        addDialogShow: false,
        addStudentId: '',
        addStudentName: '',
        addStudentClassId: '',
        addStudentGrade: '',
        addStudentGender: '',
        //choose
        studentsChoosen: [],
        //delete
        delDialogShow: false,
        studentToDel: '',
      }
    },
    mounted() {
      //收起、但显示右侧栏
      this.appStore.sideOn = false
      document.getElementById('rside').style.visibility = 'hidden'
    },
    methods: {
      addStudent() {
        if (this.addStudentId === '' || this.addStudentName === '' || this.addStudentClassId === '' || this.addStudentGrade === '') {
          this.$toast.add({
            severity: 'warn',
            summary: '有必填信息未填',
            life: 1500
          })
          return
        }
        
        axios.post('/api/student/index', {
          id: this.addStudentId,
          name: this.addStudentName,
          classId: this.addStudentClassId,
          grade: this.addStudentGrade,
          birth: '2004-12-30',
          gender: this.addStudentGender === '0' ? 0 : 1, //gender是被处理成字符串，因此这里用 '0' 来判断
        }, {
          headers: {
            username: useUserStore().username,
            token: useUserStore().token,
          }
        })
        .then(res => {
          console.log(res)
          if (res.data.code === 0) {
            this.$toast.add({
              severity: 'success',
              summary: '新增学生成功！学生ID：' + res.data.data,
              detail: '通知10s后自动隐藏',
              life: 10000,
            })
            this.addDialogShow = false
          }
        })
        .catch(err => {
          this.$toast.add({
            severity: 'error',
            summary: '新增学生错误，请自查原因或联系管理员',
            // detail:'通知3s后自动隐藏',
            life: 3000
          })
          
        })
      },
      queryStudent() {
        if (this.id === '' && this.name === '' && this.classId === '' && this.grade === '') {
          this.$toast.add({
            severity: "warn",
            summary: '四个条件至少填写一个',
            life: 3000
          })
          return
        }
        axios.get(`/api/student/index?id=${this.id}&name=${this.name}&classId=${this.classId}&grade=${this.grade}`, {
          headers: {
            token: useUserStore().token,
            username: useUserStore().username
          }
        })
        .then(res => {
          if (res.data.code === 0) {
            this.$toast.add({
              severity: "success",
              summary: '查询成功',
              life: 1500
            })
            // console.log(res.data.data)
            this.students = res.data.data
          } else {
            this.$toast.add({
              severity: "warn",
              summary: '查询学生失败(错误)！请自查原因或联系管理员', life: 3000
            })
          }
        })
        .catch(err => {
          this.$toast.add({
            severity: "error",
            summary: '查询学生异常，请自查原因或联系管理员', life: 3000
          })
        })
      },
      resetQueryInfo() {
        this.id = ''
        this.name = ''
        this.classId = ''
        this.grade = ''
      },
      chooseStudent(student) {
        // if (this.studentsChoosen.some(item => item.id === student.id)) {
        //   this.studentsChoosen = this.studentsChoosen.filter(item => item.id !== student.id)
        // } else {
        //   this.studentsChoosen.push(student)
        // }
        if (this.studentsChoosen.some(i => i.id === student.id)) this.studentsChoosen = []
        else this.studentsChoosen = [student]
      }
    },
    components: {
      NScrollbar,
    },
  }
</script>

<template>
  <div class="student-root  w-full  flex flex-wrap gap-y-2.5 justify-center items-center  rounded-lg ">
    <div class="flex-auto p-4 min-w-[80%] min-h-[160px] bg-accent rounded-lg ">
      <p class="font-bold text-2xl mt-1 mx-2 ">查询 <span class="font-normal text-base text-[#888] ml-1">(四个条件独立查询，优先级向右递减)</span>
      </p>
      <div class="query-items flex flex-wrap items-center gap-x-3 pl-4">
        <div class="query-item w-[300px] ">
          <span>⚫ 学生ID</span>
          <input v-model="id" placeholder="精确查询(推荐)" type="text">
        </div>
        <div class="query-item">
          <span>⚫ 学生姓名</span>
          <input v-model.trim="name" placeholder="模糊查询" type="text">
        </div>
        <div class="query-item">
          <span>⚫ 班级ID</span>
          <input v-model.trim="classId" placeholder="精确查询" type="text">
        </div>
        <div class="query-item">
          <span>⚫ 年级</span>
          <input v-model.trim="grade" placeholder="精确查询(7~12)" type="text">
        </div>
        
        <div
            class="btn text-lg btn-sm bg-white hover:bg-secondary border-none mx-2 px-6 absolute right-[100px] top-[58px]"
            @click="resetQueryInfo">
          重填
        </div>
        <div class="btn text-lg btn-sm bg-white hover:bg-secondary border-none mx-2 px-6 absolute right-0 top-[58px]"
             @click="queryStudent">
          查询
        </div>
      </div>
      
      <div class="pl-2 mt-2 mb-1">
        <span class="font-bold text-2xl mb-3.5 mr-6">管理</span>
        <span class="btn bg-white mr-6 hover:bg-secondary btn-sm px-6 text-lg border-none " @click="addDialogShow=true">新增</span>
        <span :class="{'btn-disabled':studentsChoosen.length===0}"
              class="btn bg-white mr-6 hover:bg-secondary btn-sm px-6 text-lg border-none "
              @click="delDialogShow=true; studentToDel=studentsChoosen[0]">删除</span>
        <span :class="{'btn-disabled':studentsChoosen.length===0}"
              class="btn bg-white mr-2 hover:bg-secondary btn-sm px-6 text-lg border-none ">修改</span>
      </div>
    </div>
    
    <!--queried data-->
    <div class="flex-auto p-4 min-w-[80%] h-[464px] bg-accent rounded-lg px-4">
      <div class="labels flex">
        <span class="font-bold">姓名</span>
        <span class="font-bold">学号(ID)</span>
        <span class="font-bold">班级ID</span>
        <span class="font-bold">班级名称</span>
        <span class="font-bold">年级</span>
        <span class="font-bold">性别</span>
        <span class="font-bold">入学时间</span>
        <span class="font-bold">出生日期</span>
      </div>
      
      <NScrollbar style=" max-height: 380px; width:100%; margin-top:10px; padding: 0.5rem 1rem 1rem ;">
        
        <div v-for="item in students" :class="{'choosen-item':studentsChoosen.some(i=>i.id===item.id)}"
             class="students flex  cursor-pointer" @click="chooseStudent(item)">
          <span>{{ item.name }}</span>
          <span>{{ item.id }}</span>
          <span>{{ item.classId }}</span>
          <span>-</span>
          <span>{{ item.grade }}</span>
          <span>{{ item.gender }}</span>
          <span>{{ String(item.entryTime).slice(0, 10) }}</span>
          <span>{{ String(item.birth).slice(0, 10) }}</span>
        </div>
      
      </NScrollbar>
    
    
    </div>
  
  </div>
  
  <!--    新增对话框-->
  <Dialog v-model:visible="addDialogShow"
          :style="{ width: '20rem', padding:'0.5rem', 'border-radius':'1rem' }"
          header="新增学生" modal>
    
    <div class="text-lg mb-4 font-bold">新增学生信息</div>
    <div class="add-dialog flex flex-wrap gap-y-3 gap-x-2 ">
      
      <div class="flex justify-between items-center w-[16rem]">
        <span>ID：</span>
        <input v-model.trim="addStudentId" class="h-[35px] w-[14rem] rounded-md bg-accent"
               style="text-indent:1ch; outline:none; border:none; box-sizing: border-box; " type="text">
      </div>
      <div class="flex items-center justify-between w-[16rem]">
        <span>姓名：</span>
        <input v-model.trim="addStudentName" class="h-[35px] w-[13rem]  rounded-md bg-accent"
               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; " type="text">
      </div>
      <div class="flex items-center justify-between w-[16rem]">
        <span>性别：</span>
        <input v-model.trim="addStudentGender" class="h-[35px] w-[13rem]  rounded-md bg-accent"
               placeholder="1男，0女" style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; "
               type="text">
      </div>
      <div class="flex items-center justify-between w-[16rem]">
        <span>班级ID：</span>
        <input v-model.trim="addStudentClassId" class="h-[35px] w-[12rem]  rounded-md bg-accent"
               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; " type="text">
      </div>
      
      <div class="flex items-center justify-between w-[16rem]">
        <span>年级：</span>
        <input v-model.trim="addStudentGrade" class="h-[35px] w-[13rem]  rounded-md bg-accent"
               placeholder="注意：该项与班级ID绑定"
               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; "
               type="text">
      </div>
    
    </div>
    
    <div class="flex justify-between w-full mt-5">
          <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-secondary"
                @click="addDialogShow=false">取消</span>
      <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-error hover:text-white"
            @click="addStudent">确认新增</span>
    </div>
  
  </Dialog>
  
  <!--    删除对话框-->
  <Dialog v-model:visible="delDialogShow"
          :style="{ width: '16rem', padding:'0.5rem', 'border-radius':'1rem' }"
          header="删除学生" modal>
    
    <div class="text-lg mb-4 font-bold">删除学生信息</div>
    <div class="add-dialog flex flex-wrap gap-y-3 gap-x-2 ">
      
      <div class="flex justify-between items-center w-[16rem]">
        <span>ID：</span>
        <text>{{ studentToDel.id }}</text>
      </div>
      <div class="flex items-center justify-between w-[16rem]">
        <span>姓名：</span>
        <text>{{ studentToDel.name }}</text>
      </div>
      <div class="flex items-center justify-between w-[16rem]">
        <span>性别：</span>
        <text>{{ studentToDel.gender === 0 ? '女' : '男' }}</text>
      </div>
      <div class="flex items-center justify-between w-[16rem]">
        <span>班级ID：</span>
        <text>{{ studentToDel.classId }}</text>
      </div>
      
      <div class="flex items-center justify-between w-[16rem]">
        <span>年级：</span>
        <text>{{ studentToDel.grade }}</text>
      </div>
    
    </div>
    
    <div class="flex justify-between w-full mt-5">
          <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-secondary"
                @click="delDialogShow=false">取消</span>
      <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-error hover:text-white"
            @click="delStudent">确认删除</span>
    </div>
  
  </Dialog>

</template>

<style lang="scss" scoped>
  
  .query-items {
    position: relative;
    
    .query-item {
      width: 300px;
      
      flex: 1 1 auto;
      //border: 1px #f00 solid;
      display: flex;
      align-items: center;
      margin: 0.3rem 0 0.2rem;
      padding: 0.25rem 0.5rem;
      
      > span {
        width: fit-content;
        flex: none;
        font-weight: bold;
        font-size: 1rem;
      }
      
      > input {
        flex: auto;
        outline: none !important;
        background-color: #fff;
        border-radius: 0.35rem;
        height: 32px;
        text-indent: 1ch;
        margin-left: 0.75rem;
        width: 300px;
        font-weight: normal !important;
        font-size: 0.85rem;
      }
    }
    
    .query-item:nth-child(4) {
      max-width: 32.6%;
    }
  }
  
  .add-dialog {
    font-weight: normal;
    
    input {
      font-size: 0.85rem;
    }
  }
  
  .students, .labels {
    width: 100%;
    justify-content: center;
    
    > span {
      padding: 0.25rem 0.5rem;
      margin: 0.25rem 0;
      font-size: 0.85rem;
      width: 100px;
      height: 30px;
      border-right: 1px #333 solid;
      box-sizing: border-box;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    
    > span:first-child {
      width: 170px;
      padding: 0.25rem 0;
    }
    
    > span:last-child {
      border: none;
    }
  }
  
  
  .students {
    background-color: #fff;
    border-radius: 0.5rem;
    margin: 0.5rem 0;
  }
  
  .students:hover {
    background-color: #bbf8ae;
  }
  
  .choosen-item {
    background-color: #05aeec !important;
    color: #fff !important;
    
    > span {
      border-color: #fff !important;
    }
    
  }
</style>
