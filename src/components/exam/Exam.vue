<script>
  import {NDatePicker, NScrollbar} from "naive-ui";
  import axios from "axios";
  import useUserStore from "@/stores/useUserStore.js";
  import useAppStore from "@/stores/useAppStore.js";
  import useExamStore from "@/stores/useExamStore.js";
  
  export default {
    name: 'Exam',
    
    data() {
      return {
        mode: 'auto', //页面下半部分展示内容的类型，默认是显示考试查询内容
        appStore: useAppStore(),
        examStore: useExamStore(),
        
        //query info
        exams: [],
        id: '',
        name: '',
        range: [94670000e4, Date.now()], //考试时间范围
        subjectId: '',
        
        //add info
        addDialogShow: false,
        addExamId: '',
        addExamName: '',
        addExamTitle: '',
        addExamSubjectId: '',
        addExamSubjectTitle: '',
        addExamTime: '',
        addExamPos: '',
        addedExam: {}, //刚刚添加的考试
        addedExamChoosen: false, //是否选中刚刚添加的考试，以便于进行管理
        
        checkCodeShow: false, //阅卷码对话框显示
        examChoosen: '', //查询列表中点击选中的考试
      }
    },
    mounted() {
      document.getElementById('rside').style.visibility = 'visible';
    },
    methods: {
      queryExam() {
        // console.log(this.range)
        if (this.id === '' && this.name === '' && this.subjectId === '' && this.range === null) {
          this.$toast.add({
            severity: 'warn',
            summary: '至少选择一个条件进行查询',
            life: 3000
          })
          return
        }
        //单查
        if (this.id !== '') {
          axios.get(`/api/exam/index?id=${this.id}`, {
            headers: {
              username: useUserStore().username,
              token: useUserStore().token
            }
          })
          .then(res => {
            if (res.data.code === 0) {
              this.$toast.add({
                severity: 'success',
                summary: "查询成功",
                life: 1500
              })
              this.exams = [res.data.data]
              this.mode = 'auto'
              console.log(res.data.data)
            } else {
              this.$toast.add({
                severity: 'warn',
                summary: "查询失败(错误)，请自查原因或联系管理员",
                life: 3000
              })
            }
          })
          .catch(err => {
            this.$toast.add({
              severity: 'error',
              summary: "查询异常，请自查原因或联系管理员",
              life: 3000
            })
          })
          return;
        }
        //多查
        axios.get(`/api/exam/indexs?name=${this.name}&subjectId=${this.subjectId}`, {
          headers: {
            username: useUserStore().username,
            token: useUserStore().token
          }
        })
        .then(res => {
          if (res.data.code === 0) {
            this.$toast.add({
              severity: 'success',
              summary: '查询成功',
              life: 1500
            })
            this.exams = res.data.data
            this.mode = 'auto' //将模式设为auto，便于下半部分显示考试查询内容
            console.log(res.data.data)
          }
        })
        .catch(err => {
          this.$toast.add({
            severity: 'error',
            summary: '查询异常',
            life: 3000
          })
        })
      },
      addExam() {
        if (this.addExamId === '' || this.addExamTitle === '' || this.addExamTime === '' || this.addExamName === ''
            || this.addExamSubjectId === '' || this.addExamSubjectTitle === '' || this.addExamPos === '') {
          this.$toast.add({
            severity: 'warn',
            summary: '有必填信息未填',
            life: 3000,
          })
          return
        }
        axios.post('/api/exam/index', {
          id: this.addExamId,
          name: this.addExamName,
          title: this.addExamTitle,
          subjectId: this.addExamSubjectId,
          subjectName: this.addExamSubjectTitle,
          examPos: this.addExamPos,
          examTime: this.addExamTime
          
        }, {
          headers: {
            username: useUserStore().username,
            token: useUserStore().token
          }
        })
        .then(res => {
          if (res.data.code === 0) {
            this.addDialogShow = false
            this.$toast.add({
              severity: 'success',
              summary: '新增考试成功！考试ID：' + res.data.data,
              detail: "通知10s后自动隐藏",
              life: 10000,
            })
            //将mode改为added,在下方显示刚刚创建的考试
            this.mode = 'added'
            axios.get(`/api/exam/index?id=${res.data.data}`, {
              headers: {
                username: useUserStore().username,
                token: useUserStore().token
              }
            })
            .then(res => {
              if (res.data.code === 0) {
                this.addedExam = res.data.data
              } else {
                this.addedExam = 'NONE'
              }
            })
            .catch(err => {
              this.addedExam = "NONE"
            })
          }
        })
      },
      accessCheckCode(exam) {
        this.examChoosen = exam
        this.checkCodeShow = true
        this.appStore.sideOn = true
      },
      chooseExam(exam) {
        this.examChoosen = exam
        this.appStore.sideOn = true
        // 放弃原有的重复点击取消选中策略
        // if (this.examChoosen.id !== exam.id) {
        //   this.examChoosen = exam
        //   this.appStore.sideOn = true
        // } else {
        //   this.examChoosen = ''
        //   this.appStore.sideOn = false
        // }
      },
    },
    
    watch: {
      examChoosen(newValue, oldValue) {
        this.examStore.examChoosen = newValue
        // console.log('exam 修改了选中目标')
        // console.log(this.examStore.examChoosen)
      }
    },
    components: {NDatePicker, NScrollbar},
    
  }
</script>

<template>
  
  <div class="student-root  w-full  flex flex-wrap gap-y-2.5 justify-center items-center  rounded-lg ">
    <div class="flex-auto p-4 min-w-[80%] min-h-[160px] bg-accent rounded-lg ">
      <p class="font-bold text-2xl mt-1 mx-2 ">查询
        <!-- <span class="font-normal text-base text-[#888] ml-1">(四个条件独立查询，优先级向右递减)</span>-->
      </p>
      <div class=" query-items flex flex-wrap items-center gap-x-3 pl-4">
        <div class="query-item w-[290px] ">
          <span>⚫ 考试ID</span>
          <input v-model="id" placeholder="精确查询(推荐)" type="text">
        </div>
        <div class="query-item w-[290px] ">
          <span>⚫ 考试名称</span>
          <input v-model.trim="name" placeholder="模糊查询" type="text">
        </div>
        <div class="query-item w-[380px] ">
          <span style="margin-right: 0.75rem;">⚫ 考试时间</span>
          <NDatePicker v-model:value="range" clearable type="daterange"/>
        </div>
        <div class="query-item w-[290px] ">
          <span>⚫ 科目ID</span>
          <input v-model.trim="subjectId" placeholder="精确查询" type="text">
        </div>
        
        <div
            class="btn text-lg btn-sm bg-white hover:bg-secondary border-none mx-2 px-6 absolute right-[100px] top-[58px]">
          重填
        </div>
        <div class="btn text-lg btn-sm bg-white hover:bg-secondary border-none mx-2 px-6 absolute right-0 top-[58px]"
             @click="queryExam">
          查询
        </div>
      </div>
      
      <div class="pl-2 mt-2 mb-1">
        <span class="font-bold text-2xl mb-3.5 mr-6">管理</span>
        <span class="btn bg-white mr-6 hover:bg-secondary btn-sm px-6 text-lg border-none "
              @click="addDialogShow=true">新增考试</span>
        <span class="btn bg-white mr-6 hover:bg-secondary btn-sm px-6 text-lg border-none "
              @click="addDialogShow=false">删除考试</span>
        <span class="btn bg-white mr-6 hover:bg-secondary btn-sm px-6 text-lg border-none "
              @click="addDialogShow=false">编辑考试信息</span>
        <span class="btn bg-white mr-6 hover:bg-secondary btn-sm px-6 text-lg border-none ">加入考生或班级</span>
        <span class="btn bg-white mr-2 hover:bg-secondary btn-sm px-6 text-lg border-none ">排除考生或班级</span>
      </div>
    </div>
    
    <!--下面打算做成可以同时显示的(根据具体情况确定)，但限于时限，留待以后-->
    <!--added exam show-->
    <div v-if="mode==='added' || mode==='addedauto'" :class="{'added-exam-choosen':addedExamChoosen}"
         class="added-exam-wrapper w-full px-6 pb-3"
         @click="addedExamChoosen=!addedExamChoosen ; examStore.examChoosen=addedExam">
      <p class="text-2xl font-bold my-4 mb-6">刚刚创建的考试
        <span class="tip btn btn-sm border-none">
          点击卡片切换选中  <!--@click.prevent.stop="addedExamChoosen=!addedExamChoosen" -->
        </span></p>
      <div class="added-exam flex flex-wrap gap-x-4">
        <span>考试ID：<text class="text-[#00c83c] hover:text-black" style="font-weight: bold">{{
            addedExam.id
          }}</text></span>
        <span>考试名称：<text>{{ addedExam.name }}</text></span>
        <span>考试组：<text>{{ addedExam.title }}</text></span>
        <span>考试时间：<text>{{ addedExam.examTime }}</text></span>
        <span>考试地点：<text>{{ addedExam.examPos }}</text></span>
        <span>科目ID：<text>{{ addedExam.subjectId }}</text></span>
        <span>科目名称：<text>{{ addedExam.subjectName }}</text></span>
        <span>考试人数：<text>{{ addedExam.num }}</text></span>
        <span>考试码：<text>{{ addedExam.testCode || '尚未创建' }}</text></span>
        <span>阅卷码：<text class="text-[#00c83c] hover:text-black" style="font-weight: bold">>>点击查看<<</text></span>
        <span>已阅：<text class="text-red-500 " style="font-weight: bold">{{ addedExam.checked || '否' }}</text></span>
        <span>复阅：<text class="text-red-500 "
                         style="font-weight: bold">{{ addedExam.doubleChecked || '否' }}</text></span>
      
      </div>
    </div>
    <!--queried data-->
    <div v-else-if="mode==='auto' || mode==='addedauto'"
         class="flex-auto p-4 min-w-[80%] h-[464px] bg-accent rounded-lg px-4">
      <div class="labels flex ">
        <span class="font-bold">考试名称</span>
        <span class="font-bold">考试ID</span>
        <span class="font-bold">科目ID</span>
        <span class="font-bold">科目名称</span>
        <span class="font-bold" style="width:100px;">考试时间</span>
        <span class="font-bold">考试地点</span>
        <span class="font-bold">考试码</span>
        <span class="font-bold" style="width:120px;">阅卷码</span>
        <span class="font-bold" style="width:70px;">已阅</span>
        <span class="font-bold" style="width:70px;">复阅</span>
      </div>
      <NScrollbar style=" max-height: 380px; width:100%; margin-top:5px; padding: 0.5rem 1rem 1.5rem ;">
        <div v-for="item in exams" :class="{'added-exam-choosen':examChoosen.id===item.id}"
             class="exams flex cursor-pointer mb-3"
             @click="chooseExam(item)">
          <span>{{ item.name }}</span>
          <span>{{ item.id }}</span>
          <span>{{ item.subjectId }}</span>
          <span>{{ item.subjectName }}</span>
          <span style="width:100px;  ">{{ String(item.examTime).slice(0, 19).replace("T", " ") }}</span>
          <span>{{ item.examPos }}</span>
          <span>{{ item.examCode || '尚未创建' }}</span>
          <span style="font-weight:bold; color:#0bcc45;width:120px; " @click.prevent.stop="accessCheckCode(item)"> >>点击查看<< </span>
          <span :class="{'text-red':item.checked===1}"
                style="width:70px;  font-weight:normal;">{{ item.checked === 1 ? '是' : '否' }}</span>
          <span :class="{'text-red':item.doubleChecked===1}"
                style="width:70px;  font-weight:normal;">{{ item.doubleChecked === 1 ? '是' : '否' }}</span>
        </div>
      </NScrollbar>
    </div>
  </div>
  
  <!--    新增对话框-->
  <Dialog v-model:visible="addDialogShow" :style="{ width: '20rem', padding:'0.5rem', 'border-radius':'1rem' }"
          header="新增考试" modal>
    
    <div class="text-lg mb-4 font-bold">新增考试信息</div>
    
    <div class="add-dialog flex flex-wrap gap-y-3 gap-x-2 ">
      
      <div class="flex justify-between items-center w-[16rem]">
        <span>考试ID：</span>
        <input v-model.trim="addExamId" class="h-[35px] w-[12rem] rounded-md bg-accent"
               style="text-indent:1ch; outline:none; border:none; box-sizing: border-box; " type="text">
      </div>
      <div class="flex items-center justify-between w-[16rem]">
        <span>考试名称：</span>
        <input v-model.trim="addExamName" class="h-[35px] w-[11rem]  rounded-md bg-accent"
               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; " type="text">
      </div>
      <div class="flex items-center justify-between w-[16rem]">
        <span>考试组：</span>
        <input v-model.trim="addExamTitle" class="h-[35px] w-[12rem]  rounded-md bg-accent"
               placeholder="非必填，由考试组织者确定"
               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; "
               type="text">
      </div>
      <div class="flex items-center justify-between w-[16rem]">
        <span>科目ID：</span>
        <input v-model.trim="addExamSubjectId" class="h-[35px] w-[12rem]  rounded-md bg-accent"
               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; " type="text">
      </div>
      
      <div class="flex items-center justify-between w-[16rem]">
        <span>科目名称：</span>
        <input v-model.trim="addExamSubjectTitle" class="h-[35px] w-[11rem]  rounded-md bg-accent"
               placeholder="注意：该项与科目ID绑定"
               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; "
               type="text">
      </div>
      
      <div class="flex items-center justify-between w-[16rem]">
        <span>考试时间：</span>
        <input v-model.trim="addExamTime" class="h-[35px] w-[11rem]  rounded-md bg-accent"
               placeholder="如2024-10-10T08:50:00"
               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; "
               type="text">
      </div>
      
      <div class="flex items-center justify-between w-[16rem]">
        <span>考试地点：</span>
        <input v-model.trim="addExamPos" class="h-[35px] w-[11rem]  rounded-md bg-accent"
               placeholder=""
               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; "
               type="text">
      </div>
      
      <!--      <div class="flex items-center justify-between w-[16rem]">-->
      <!--        <span>计划阅卷时间：</span>-->
      <!--        <input v-model.trim="addExamPlanCheckTime" class="h-[35px] w-[13rem]  rounded-md bg-accent"-->
      <!--               placeholder="非必填"-->
      <!--               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; "-->
      <!--               type="text">-->
      <!--      </div>-->
      <!--      -->
      <!--      <div class="flex items-center justify-between w-[16rem]">-->
      <!--        <span>阅卷码：</span>-->
      <!--        <input v-model.trim="addExamCheckCode" class="h-[35px] w-[13rem]  rounded-md bg-accent"-->
      <!--               placeholder="非必填，阅卷时用于鉴权"-->
      <!--               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; "-->
      <!--               type="text">-->
      <!--      </div>-->
    </div>
    
    <div class="flex justify-between w-full mt-5">
          <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-secondary"
                @click="addDialogShow=false">取消
          </span>
      <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-error hover:text-white"
            @click="addExam">
        确认新增
      </span>
    </div>
  
  </Dialog>
  <!--    阅卷码对话框-->
  <Dialog v-model:visible="checkCodeShow" :style="{ width: '20rem', padding:'0.5rem', 'border-radius':'1rem' }"
          header="查看阅卷码" modal>
    
    <div class="text-lg mb-4 font-bold">阅卷码信息</div>
    
    <div class="add-dialog flex flex-wrap gap-y-3 gap-x-2 cursor-not-allowed">
      
      <div class="flex justify-between items-center w-[16rem] ">
        <span>考试ID：</span>
        <input v-model.trim="examChoosen.id" class="h-[35px] w-[12rem] rounded-md bg-accent cursor-not-allowed" disabled
               style="text-indent:1ch; outline:none; border:none; box-sizing: border-box; " type="text">
      </div>
      <div class="flex items-center justify-between w-[16rem]">
        <span>考试名称：</span>
        <input v-model.trim="examChoosen.name" class="h-[35px] w-[11rem] cursor-not-allowed rounded-md bg-accent"
               disabled
               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; " type="text">
      </div>
      <div class="flex items-center justify-between w-[16rem]">
        <span>阅卷码：</span>
        <!-- ev-model.trim="xamChoosen.checkCode"  -->
        <input class="h-[35px] w-[12rem] font-bold text-neutral rounded-md bg-accent cursor-not-allowed" disabled
               style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; " type="text"
               value="105220">
      </div>
    
    </div>
    
    <div class="flex justify-center w-full mt-5">
      <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-secondary"
            @click="checkCodeShow=false">关闭弹窗</span>
    </div>
  
  </Dialog>
</template>

<style lang="scss" scoped>
  .query-items {
    position: relative;
    
    .query-item {
      //width: 300px;
      flex: 1 1 auto;
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
      max-width: 29.6%;
    }
  }
  
  .add-dialog {
    font-weight: normal;
    
    input {
      font-size: 0.85rem;
    }
  }
  
  .exams, .labels {
    width: 100%;
    justify-content: center;
    padding: 0.5rem 0;
    //border:0.5px #ccc solid;
    > span {
      word-break: break-all;
      padding: 0.25rem 0.5rem;
      //margin: 0.25rem 0;
      font-size: 0.85rem;
      width: 85px;
      height: 40px;
      border-right: 1px #333 solid;
      box-sizing: border-box;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    
    > span:first-child {
      width: 200px;
      padding: 0.25rem 0.5rem 0.25rem 0.5rem;
      word-break: break-all;
    }
    
    > span:last-child {
      border: none;
    }
  }
  
  .exams {
    background-color: #fff;
    border-radius: 0.5rem;
    
    &.added-exam-choosen {
      background-color: #05aeec !important;
      color: #fff;
      
      > span {
        border-right: #fff 1px solid !important;
        color: #fff !important;
      }
      
      > span:last-child {
        border-right: none !important;
      }
    }
  }
  
  .exams:hover {
    background-color: #bbf8ae;
  }
  
  .added-exam-wrapper {
    background-color: #f5f5f5;
    border-radius: 0.5rem;
    
    &.added-exam-choosen {
      background-color: #05aeec;
      color: #fff;
      
      span {
        color: #000;
      }
      
      .tip {
        color: #00c83c;
      }
    }
    
    .added-exam {
      > span {
        font-size: 1rem;
        font-weight: bold;
        background-color: #fff;
        border-radius: 0.5rem;
        padding: 0.35rem 0.65rem;
        cursor: pointer;
        margin-bottom: 0.75rem;
        display: flex;
        align-items: center;
        
        > text {
          font-weight: normal;
          font-size: 0.85rem;
        }
      }
      
      > span:hover {
        background-color: #bbf8ae;
      }
    }
    
    .tip {
      //padding: 0.5rem 0 0 0.5rem;
      margin-left: 2rem;
      color: #00c83c;
      background-color: #fff;
      font-size: 0.85rem;
      //font-weight:normal;
    }
  }
  
  .text-red {
    color: #f00 !important;
  }
  
  .exams:last-child {
    margin-bottom: 2rem !important;
  }
</style>
