<script>
  import {NDatePicker, NScrollbar, NSelect, NSpace} from 'naive-ui'
  import AiCheckChooseOut from './AiCheckChooseOut.vue'
  import useAppStore from "@/stores/useAppStore.js";
  import axios from "axios";
  import useUserStore from "@/stores/useUserStore.js";
  import useExamStore from "@/stores/useExamStore.js";
  import PaperUpload from '@/components/aicheck/PaperUpload.vue';
  import ExamUpload from "@/components/aicheck/ExamUpload.vue";
  import useCheckoutStore from "@/stores/useCheckoutStore.js";
  
  export default {
    data() {
      return {
        appStore: useAppStore(),
        examStore: useExamStore(),
        
        // check exams
        range: [94670000e4, Date.now()],
        examId: '',
        examTitle: '',
        doubleCheck: true,
        subject: '',
        subOptions: [
          {
            label: "不限",
            value: "",
          },
          {
            label: "计算机",
            value: "计算机",
          },
          {
            label: "语文",
            value: "语文",
            disabled: false
          },
          {
            label: "数学",
            value: "数学"
          },
          {
            label: "英语",
            value: "英语"
          },
          {
            label: "物理",
            value: "物理",
            disabled: false
          },
          {
            label: "化学",
            value: "化学"
          },
          {
            label: "生物",
            value: "生物"
          },
        ],
        grade: '',
        graOptions: [
          {
            label: "不限",
            value: ""
          },
          {
            label: '初一',
            value: 7
          },
          {
            label: '初二',
            value: 8
          },
          {
            label: '初三',
            value: 9
          },
          {
            label: '高一',
            value: 10
          },
          {
            label: '高二',
            value: 11
          },
          {
            label: '高三',
            value: 12
          },
        ],
        countAllExams: '',
        exams: [], // data by query
        //选择结果显示
        chooseOutShow: false,
        // check papers
        paperId: '',
        studentId: '',
        studentName: '',
        checkExamShow: false, //提交阅卷弹窗
        
        //****阅试卷
        checkPaperId: '',
        checkPaperExamId: '',
        checkPaperStudentId: '',
        checkPaperStudentName: '',
        hadQueryCheckPaper: false,
        papers: [],
        papersChoosen: [],
        
        //import papers
        importTypeNow: 'paper',
        importPaperId: '',
        importPaper: '',
        importExamId: '',
        importExam: '',
        unimportedPapers: [],
        
        //others
        keepitem: useAppStore().itemHolder === 0 ? 0 : useAppStore().itemHolder, //与缓存同步
        itemHolder: useAppStore().itemHolder, //与缓存同步
      }
    },
    components: {
      NDatePicker,
      NSelect,
      NSpace,
      NScrollbar,
      AiCheckChooseOut,
      PaperUpload,
      ExamUpload,
    },
    mounted() {
      document.getElementById('apptopbar').addEventListener('mouseenter', () => {
        if (this.itemHolder === 0) {
          this.keepitem = 0
        }
        
      })
      
      //隐藏右侧栏
      this.appStore.sideOn = false
      document.getElementById('rside').style.visibility = 'hidden'
      
      //query count of all exams
      axios.get('/api/exam/count?mgr=', {
        headers: {
          username: useUserStore().username,
          token: useUserStore().token,
        }
      })
      .then(res => {
        if (res.data.code === 0) {
          this.countAllExams = res.data.data
        }
      })
      .catch(err => {
        console.log(err)
      })
      
    },
    unmounted() {
      // this.examStore.examsChoosen = []
    },
    
    methods: {
      //****阅考试
      denyDoubleCheck() {
        this.doubleCheck = true
        alert('你的账号未获得授权更改此项，请联系管理员了解详情！')
      },
      submitChoose() {
        this.exams = []
        this.examStore.examsChoosen = []
        if (this.range === null && this.subject === '' && this.grade === ''
            && this.examId === '' && this.examTitle === '') {
          this.$toast.add({
            severity: 'warn',
            summary: '至少选择一个条件查询',
            life: 3000
          })
          return
        }
        console.log('已提交筛选，请等待结果...')
        axios.get(`/api/exam/indexs?id=${this.examId}&name=${this.examTitle}&subjectId=&subjectTitle=${this.subject}&grade=${this.grade}`, {
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
            this.chooseOutShow = true
            this.exams = res.data.data
            // console.log(res.data.data)
          } else {
            this.$toast.add({
              severity: 'warn',
              summary: '查询失败(错误)',
              life: 3000
            })
          }
        })
        .catch(err => {
          this.$toast.add({
            severity: 'error',
            summary: '查询异常',
            life: 3000
          })
        })
        
        setTimeout(() => {
          this.chooseOutShow = true
        }, 1000)
      },
      submitCheckExams() {
        if (this.examStore.examsChoosen.reduce((sum, item) => sum + item.num, 0) > this.examStore.examsChoosen.reduce((sum, item) => sum + item.countHasUrl, 0)) {
          this.$toast.add({
            severity: 'warn',
            summary: '尚有试卷未导入，请导入全部试卷后提交阅卷',
            life: '3000'
          })
          return
        }
        
        this.checkExamShow = false
        
        // 查看选中的考试
        // console.log(this.examStore.examsChoosen)
        
        //这里是上传评阅的代码
        const exams = this.examStore.examsChoosen.map(item => item.id)
        // console.log(exams)
        let count = 0
        exams.forEach(item => {
          axios.post(`/api/check/exam-single/${item}`, {}, {
            headers: {
              username: useUserStore().username,
              token: useUserStore().token
            }
          })
          .then(res => {
            if (res.data.code === 0) {
              count = count + 1
              if (count === exams.length) {
                this.$toast.add({
                  severity: 'success',
                  summary: '评阅完成',
                  life: 1500
                })
                
                // 评阅完成后前往结果页
                // this.$router.push(`/checkout?countExam=${this.examStore.examsChoosen.length}&countPaper=${this.examStore.examsChoosen.reduce((sum,item)=>sum+item.num,0)}`)
                setTimeout(() => {
                  useCheckoutStore().mode = 'exam'
                  useCheckoutStore().exams = this.examStore.examsChoosen.slice()
                  useCheckoutStore().papers = []
                  this.$router.push('/checkoutresult?mode=exam')
                }, 500)
                
              }
            }
          })
        })
        
        
      },
      
      //****阅试卷
      queryCheckPaper() {
        this.papers = []
        this.papersChoosen = []
        if (this.checkPaperId === '' && this.checkPaperExamId === '' && this.checkPaperStudentId === '' && this.checkPaperStudentName === '') {
          this.$toast.add({
            severity: 'warn',
            summary: '至少选择一个条件查询',
            life: 3000
          })
          return
        }
        //将查询动作标为已发生
        this.hadQueryCheckPaper = true
        axios.get(`/api/paper/indexs?id=${this.checkPaperId}&examId=${this.checkPaperExamId}&studentId=${this.checkPaperStudentId}&studentName=${this.checkPaperStudentName}`, {
          headers: {
            username: useUserStore().username,
            token: useUserStore().token
          }
        })
        .then(res => {
          if (res.data.code === 0) {
            // console.log(res.data.data)
            this.$toast.add({
              severity: 'success',
              summary: '查询成功',
              life: 1500
            })
            this.papers = res.data.data
            // console.log(this.papers)
          } else {
            this.papers = []
            this.$toast.add({
              severity: 'warn',
              summary: '查询失败(错误)',
              life: 3000
            })
          }
        })
        .catch(err => {
          this.papers = []
          this.$toast.add({
            severity: 'error',
            summary: '查询异常',
            life: 3000
          })
        })
      },
      chooseAllPapers() {
        if (this.papersChoosen.length === this.papers.length)
          this.papersChoosen = []
        else {
          this.papersChoosen = this.papers.slice()
          console.log('choose all')
        }
        
        
      },
      choosePaper(paper) {
        if (this.papersChoosen.some(item => item.id === paper.id)) {
          this.papersChoosen = this.papersChoosen.filter(item => item.id !== paper.id)
        } else {
          this.papersChoosen.push(paper)
        }
      },
      submitCheckPapers() {
        let count = 0
        this.papersChoosen.forEach(item => {
          axios.post(`/api/check/paper-single/${item.id}`, {}, {
            headers: {
              username: useUserStore().username,
              token: useUserStore().token
            }
          })
          .then(res => {
            if (res.data.code === 0) {
              count = count + 1
              console.log(res.data.data + '评阅成功')
              console.log(count)
              if (count === this.papersChoosen.length) {
                this.$toast.add({
                  severity: 'success',
                  summary: '评阅完成',
                  life: 1500
                })
                
                setTimeout(() => {
                  useCheckoutStore().mode = 'paper'
                  useCheckoutStore().papers = this.papersChoosen.slice()
                  useCheckoutStore().exams = []
                  this.$router.push('/checkoutresult?mode=paper')
                }, 500)
                
                
              }
            }
          })
        })
        
        // if(count === this.papersChoosen.length){
        //   console.log('所有答卷评阅完成，共评阅'+count+'份答卷')
        // }
      },
      
      //****导入答卷
      //查询将要导入的答卷
      queryImportPaper() {
        axios.get(`/api/paper/index?id=${this.importPaperId}`, {
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
            this.importPaper = res.data.data[0]
          } else {
            this.$toast.add({
              severity: 'warn',
              summary: "查询失败(错误)",
              life: 3000
            })
            this.importPaper = ''
          }
        })
        .catch(err => {
          this.importPaper = ''
          this.$toast.add({
            severity: 'error',
            summary: "查询异常",
            life: 3000
          })
        })
      },
      //查询将要导入试卷的考试
      queryImportExam() {
        axios.get(`/api/exam/index?id=${this.importExamId}`, {
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
            this.importExam = res.data.data
            if (res.data.data.countHasUrl + res.data.data.countUntest !== res.data.data.num)
              this.queryUnimportedPapers() //继续查询未导入试卷
          } else {
            this.importExam = ''
            this.$toast.add({
              severity: 'warn',
              summary: '查询失败(错误)',
              life: 3000
            })
          }
        })
        .catch(err => {
          this.importExam = ''
          this.$toast.add({
            severity: 'error',
            summary: '查询异常',
            life: 3000
          })
        })
      },
      //切换导入试卷模式
      toggleImportType(type) {
        this.importTypeNow = type
        // if (this.importTypeNow === 'paper')
        //   this.importTypeNow = 'exam'
        // else this.importTypeNow = 'paper'
      },
      //依据考试ID查询未导入试卷
      queryUnimportedPapers() {
        axios.get(`/api/paper/unimportedBL?examId=${this.importExamId}`, {
          headers: {
            username: useUserStore().username,
            token: useUserStore().token
          }
        })
        .then(res => {
          if (res.data.code === 0) {
            this.unimportedPapers = res.data.data
          } else {
            this.unimportedPapers = []
          }
        })
        .catch(err => {
          this.unimportedPapers = []
        })
      },
      
      //others
      setkeepitem(n) {
        if (this.itemHolder === 0)
          this.keepitem = n
      },
      setItemHolder(n) {
        if (this.itemHolder === n) {
          this.itemHolder = 0
        } else {
          this.itemHolder = n
        }
      }
    },
    watch: {
      itemHolder(newValue, oldValue) {
        if (newValue !== 0) {
          this.keepitem = newValue
        }
        this.appStore.itemHolder = newValue
      }
    },
  }
</script>

<template>
  <div class="aicheck-root">
    <div class="aicheck-main flex ">
      <!--待阅、复阅、历史记录-->
      <div id="item3" :class="{'keepitem':keepitem===3}" class="item item-3 relative flex-auto "
           style="border-right:10px #ffffff solid; "
           @mouseover="setkeepitem(3)">
        <span :class="{'item-holder-avtive':itemHolder===3}" class="item-holder" @click="setItemHolder(3)">
        
        
     <svg v-if="itemHolder===3" class="icon" height="32" p-id="7490" t="1710987262119"
          version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg"><path
         d="M648.728381 130.779429a73.142857 73.142857 0 0 1 22.674286 15.433142l191.561143 191.756191a73.142857 73.142857 0 0 1-22.137905 118.564571l-67.876572 30.061715-127.341714 127.488-10.093714 140.239238a73.142857 73.142857 0 0 1-124.684191 46.445714l-123.66019-123.782095-210.724572 211.699809-51.833904-51.614476 210.846476-211.821714-127.926857-128.024381a73.142857 73.142857 0 0 1 46.299428-124.635429l144.237715-10.776381 125.074285-125.220571 29.379048-67.779048a73.142857 73.142857 0 0 1 96.207238-38.034285z"
         fill="#05aefc" p-id="7491"></path></svg>
      <svg v-else class="icon" height="32" p-id="7490" t="1710987262119"
           version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg"><path
          d="M648.728381 130.779429a73.142857 73.142857 0 0 1 22.674286 15.433142l191.561143 191.756191a73.142857 73.142857 0 0 1-22.137905 118.564571l-67.876572 30.061715-127.341714 127.488-10.093714 140.239238a73.142857 73.142857 0 0 1-124.684191 46.445714l-123.66019-123.782095-210.724572 211.699809-51.833904-51.614476 210.846476-211.821714-127.926857-128.024381a73.142857 73.142857 0 0 1 46.299428-124.635429l144.237715-10.776381 125.074285-125.220571 29.379048-67.779048a73.142857 73.142857 0 0 1 96.207238-38.034285z"
          fill="#bfbfbf" p-id="7491"></path></svg>
        
        </span>
        <p class="font-bold text-4xl mb-2">
          待阅
        </p>
        <div class="item-tip">待阅试卷：1079份</div>
        <div class="item-tip">待阅考试：28场</div>
        
        <p class="font-bold text-4xl mb-2 mt-8">
          复阅申请
        </p>
        <div class="item-tip">申请人数：3人</div>
        <div class="item-tip">申请试卷数：4份</div>
        
        <p class="font-bold text-4xl mb-2 mt-8">
          历史记录
        </p>
        <div class="item-tip">今天：...</div>
        <div class="item-tip">本周：...</div>
        <div class="item-tip">本学期：...</div>
        <div class="item-tip">总计：...</div>
      </div>
      <!--阅考试-->
      <div id="item1" :class="{'keepitem':keepitem===1}" class="item item-1 relative flex-auto "
           style="border-right:10px #ffffff solid;"
           @mouseover="setkeepitem(1)">
         <span :class="{'item-holder-avtive':itemHolder===1}" class="item-holder" @click="setItemHolder(1)">
         
          <svg v-if="itemHolder===1" class="icon" height="32" p-id="7490" t="1710987262119"
               version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg"><path
              d="M648.728381 130.779429a73.142857 73.142857 0 0 1 22.674286 15.433142l191.561143 191.756191a73.142857 73.142857 0 0 1-22.137905 118.564571l-67.876572 30.061715-127.341714 127.488-10.093714 140.239238a73.142857 73.142857 0 0 1-124.684191 46.445714l-123.66019-123.782095-210.724572 211.699809-51.833904-51.614476 210.846476-211.821714-127.926857-128.024381a73.142857 73.142857 0 0 1 46.299428-124.635429l144.237715-10.776381 125.074285-125.220571 29.379048-67.779048a73.142857 73.142857 0 0 1 96.207238-38.034285z"
              fill="#05aefc" p-id="7491"></path></svg>
      <svg v-else class="icon" height="32" p-id="7490" t="1710987262119"
           version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg"><path
          d="M648.728381 130.779429a73.142857 73.142857 0 0 1 22.674286 15.433142l191.561143 191.756191a73.142857 73.142857 0 0 1-22.137905 118.564571l-67.876572 30.061715-127.341714 127.488-10.093714 140.239238a73.142857 73.142857 0 0 1-124.684191 46.445714l-123.66019-123.782095-210.724572 211.699809-51.833904-51.614476 210.846476-211.821714-127.926857-128.024381a73.142857 73.142857 0 0 1 46.299428-124.635429l144.237715-10.776381 125.074285-125.220571 29.379048-67.779048a73.142857 73.142857 0 0 1 96.207238-38.034285z"
          fill="#bfbfbf" p-id="7491"></path></svg>
        
        
        </span>
        <p class="font-bold text-4xl mb-2">
          阅考试
        </p>
        
        <div class="item-tip">面向考试，有组织、系统性阅卷</div>
        <div v-show="countAllExams!==''" class="item-tip text-[#05aeec]">名下可批阅的考试：
          <text>{{ countAllExams }}</text>
          场
        </div>
        <div class="item-tip text-[#05aeec]">选择“考试阅卷-选择考试-提交阅卷”</div>
        <div class="item-tip">高效化、大批量</div>
        <div class="item-tip">二次批阅</div>
        <div class="item-tip">数据可视化</div>
        <div class="item-tip">学情追踪</div>
        
        <!--选择筛选条件-->
        <div class="item-detail">
          <p class="font-bold mb-2 text-lg">请选择待阅考试：</p>
          <div class="aicheck-choose flex flex-wrap gap-x-0 justify-between">
            <div class="w-[48%] ">
              <p class=" font-bold mb-1">⚫ 时间</p>
              <NDatePicker v-model:value="range" clearable type="daterange"/>
              <!--<pre>{{ JSON.stringify(range) }}</pre>-->
            </div>
            
            <div class="w-[24%] ">
              <p class=" font-bold mb-1">⚫ 科目</p>
              <n-select v-model:value="subject" :options="subOptions"/>
            
            </div>
            
            <div class="w-[24%] ">
              <p class=" font-bold mb-1">⚫ 年级</p>
              <n-select v-model:value="grade" :options="graOptions"/>
            
            </div>
            
            <div class="w-[48%] flex gap-x-3 items-center ">
              <p class=" font-bold">⚫ 考试ID</p>
              <input
                  v-model="examId"
                  class="outline-none" placeholder="精确查询"
                  style="height:32px; box-sizing:border-box; border-radius:0.15rem; text-indent:1.25ch; border:1px #ccc solid;"
                  type="text"/>
            </div>
            
            <div class="w-[48%] flex gap-x-3 items-center h-[40px] ">
              <p class="mb-1 font-bold">⚫ 考试名称</p>
              <input
                  v-model="examTitle"
                  class="outline-none" placeholder="模糊查询"
                  style="height:32px; box-sizing:border-box; border-radius:0.15rem; text-indent:1.25ch; border:0.5px #ccc solid;"
                  type="text"/>
            
            </div>
            
            <div class=" w-[50%]">
              <p class="mt-2 mb-1 font-bold flex items-center">⚫ 包含已阅考试
                <span
                    style="font-size: 0.7rem; font-weight: normal; margin-left:0.5rem;">(复阅考试需获取授权)</span>
                <input v-model="doubleCheck" class="toggle toggle-sm toggle-info mx-2" type="checkbox"
                       @click.prevent.stop="denyDoubleCheck"/>
              </p>
            </div>
            
            <div v-show="examStore.examsChoosen.length>0 && chooseOutShow" @click="checkExamShow=true">
              <span
                  class="mt-1 btn btn-ghost btn-sm bg-info text-white hover:bg-secondary hover:text-black relative right-[-3rem]">
                提交阅卷({{ examStore.examsChoosen.length }})</span>
            </div>
            
            <div @click="submitChoose">
              <span
                  class="mt-1 btn btn-ghost btn-sm bg-info text-white hover:bg-secondary hover:text-black relative right-2">开始筛选</span>
            </div>
          
          </div>
        </div>
        
        <!--筛选结果显示-->
        <div class="item-detail">
          <AiCheckChooseOut v-show="chooseOutShow" :exams="exams"/>
        </div>
      
      </div>
      <!--阅试卷-->
      <div id="item2" :class="{'keepitem':keepitem===2}" class="item item-2 relative flex-auto "
           style="border-right:10px #fff solid; "
           @mouseover="setkeepitem(2)">
         <span :class="{'item-holder-avtive':itemHolder===2}" class="item-holder" @click="setItemHolder(2)">
         <svg v-if="itemHolder===2" class="icon" height="32" p-id="7490" t="1710987262119"
              version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg"><path
             d="M648.728381 130.779429a73.142857 73.142857 0 0 1 22.674286 15.433142l191.561143 191.756191a73.142857 73.142857 0 0 1-22.137905 118.564571l-67.876572 30.061715-127.341714 127.488-10.093714 140.239238a73.142857 73.142857 0 0 1-124.684191 46.445714l-123.66019-123.782095-210.724572 211.699809-51.833904-51.614476 210.846476-211.821714-127.926857-128.024381a73.142857 73.142857 0 0 1 46.299428-124.635429l144.237715-10.776381 125.074285-125.220571 29.379048-67.779048a73.142857 73.142857 0 0 1 96.207238-38.034285z"
             fill="#05aefc" p-id="7491"></path></svg>
      <svg v-else class="icon" height="32" p-id="7490" t="1710987262119"
           version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg"><path
          d="M648.728381 130.779429a73.142857 73.142857 0 0 1 22.674286 15.433142l191.561143 191.756191a73.142857 73.142857 0 0 1-22.137905 118.564571l-67.876572 30.061715-127.341714 127.488-10.093714 140.239238a73.142857 73.142857 0 0 1-124.684191 46.445714l-123.66019-123.782095-210.724572 211.699809-51.833904-51.614476 210.846476-211.821714-127.926857-128.024381a73.142857 73.142857 0 0 1 46.299428-124.635429l144.237715-10.776381 125.074285-125.220571 29.379048-67.779048a73.142857 73.142857 0 0 1 96.207238-38.034285z"
          fill="#bfbfbf" p-id="7491"></path></svg>
        
        </span>
        <p class="font-bold text-4xl mb-2">阅试卷</p>
        
        <div class="item-tip">面向试卷，可批量、自由组合阅卷</div>
        <div class="item-tip text-[#05aeec]">名下可批阅的试卷：483份</div>
        <div class="item-tip text-[#05aeec]">选择“自由阅卷-选择试卷-提交批阅”</div>
        <div class="item-tip">精准化、复阅</div>
        <div class="item-tip">单卷批阅历史可溯</div>
        <div class="item-tip">跨科目、可筛选、不限量</div>
        
        <div class="item-detail">
          <p class="font-bold mb-2 text-lg">请选择待阅试卷</p>
          <!--查询试卷的输入-->
          <div class="paper-chooser flex flex-wrap items-center gap-x-4 gap-y-1 px-2 relative border-none">
            <span><text>试卷ID：</text><input v-model.trim="checkPaperId" placeholder="精确查询" type="text"></span>
            <span><text>学生ID：</text><input v-model.trim="checkPaperStudentId" placeholder="精确查询"
                                             type="text"></span>
            <span style="max-width: calc(50% - 0.5rem);"><text>考试ID：</text><input v-model.trim="checkPaperExamId"
                                                                                    placeholder="精确查询" type="text"></span>
            <span style="max-width:calc(50% - 5rem);"><text>学生姓名：</text><input v-model.trim="checkPaperStudentName"
                                                                                   placeholder="模糊查询"
                                                                                   type="text"></span>
            <div class="btn border-none btn-sm absolute right-3 top-12 flex-none  px-3  bg-white hover:bg-secondary "
                 @click="queryCheckPaper">查询
            </div>
          </div>
          <!--查询结果-->
          <div v-show="hadQueryCheckPaper" class="paper-choose-out relative pt-0.5">
            <p class="font-bold mt-2 mb-3 text-lg">查询结果<text class="text-base text-info">(共{{papers.length}}条)</text></p>
            <div v-show="this.papers.length===0 " class="flex justify-center items-center text-lg text-red-500">
              未查询到数据 ...o_O
            </div>
            <div v-show="this.papers.length>0" class="pr-3 absolute top-1 right-0 flex gap-x-2 border-none">
               <span v-show="papersChoosen.length>0" class="btn btn-sm bg-white hover:bg-secondary border-none"
                     :class="{'choosen-text-info':papersChoosen.length>0}" @click="submitCheckPapers">提交阅卷({{papersChoosen.length}})
              </span>
              
              <span class="btn btn-sm bg-white hover:bg-secondary border-none" @click="papersChoosen=[]">取消已选</span>
              <span :class="{'all-choosen':papersChoosen.length === papers.length}"
                    class="btn btn-sm bg-white hover:bg-secondary border-none"
                    @click="chooseAllPapers">全选</span>
            </div>
            <div class="px-3 text-sm">
              <div v-show="this.papers.length>0"
                   style="background-color:#fff;margin-bottom:0.75rem; padding:0.15rem 0.15rem 0.75rem; border-radius:0.75rem;">
                <NScrollbar
                    style="max-height:230px; background-color:#fff; border-radius:0.5rem; padding:0.75rem 0.75rem;">
                  <div v-for="item in papers" :class="{'choosen':papersChoosen.some(i=>i.id===item.id)}"
                       class="paper cursor-pointer flex flex-wrap gap-x-4" @click.prevent.stop="choosePaper(item)">
                    <span><text>试卷ID：</text>{{ item.id }}</span>
                    <span><text>考试ID：</text>{{ item.examId }}</span>
                    <span><text>学生ID：</text>{{ item.studentId }}</span>
                    <span><text>学生姓名：</text>{{ item.studentName }}</span>
                    <span><text>已阅：</text><span
                        :class="{'text-red':item.checked===0}">{{ item.checked === 0 ? '否' : '是' }}</span></span>
                    <span><text>复阅：</text>{{ item.checked === 0 ? '否' : '是' }}</span>
                    <span><text>是否已导入：</text><span
                        :class="{'text-red':item.hasUrl===0}">{{ item.hasUrl === 0 ? '否' : '是' }}</span></span>
                    <span v-show="item.hasUrl===1"><text>答卷地址：</text>{{
                        item.url === null ? '已失效' : item.url
                      }}</span>
                  </div>
                </NScrollbar>
              </div>
            </div>
            <div v-show="this.papersChoosen.length>0" class=" pt-2 border-t-[1px] border-t-[#ccc]">
              <p class="font-bold text-base mb-1">已选试卷的ID</p>
              <div class="p-1 flex flex-wrap gap-x-1.5">
                <span v-for="item in papersChoosen"
                      class="bg-white py-0.5 px-1.5 rounded-lg border-none flex justify-center items-center font-sm">
                  {{ item.id }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!--导入答卷-->
      <div id="item4" :class="{'keepitem':keepitem===4}" class="item item-4 relative flex-auto " style=""
           @mouseover="setkeepitem(4)">
         <span :class="{'item-holder-avtive':itemHolder===4}" class="item-holder" @click="setItemHolder(4)">
          <svg v-if="itemHolder===4" class="icon" height="32" p-id="7490" t="1710987262119"
               version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg"><path
              d="M648.728381 130.779429a73.142857 73.142857 0 0 1 22.674286 15.433142l191.561143 191.756191a73.142857 73.142857 0 0 1-22.137905 118.564571l-67.876572 30.061715-127.341714 127.488-10.093714 140.239238a73.142857 73.142857 0 0 1-124.684191 46.445714l-123.66019-123.782095-210.724572 211.699809-51.833904-51.614476 210.846476-211.821714-127.926857-128.024381a73.142857 73.142857 0 0 1 46.299428-124.635429l144.237715-10.776381 125.074285-125.220571 29.379048-67.779048a73.142857 73.142857 0 0 1 96.207238-38.034285z"
              fill="#05aefc" p-id="7491"></path></svg>
      <svg v-else class="icon" height="32" p-id="7490" t="1710987262119"
           version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg"><path
          d="M648.728381 130.779429a73.142857 73.142857 0 0 1 22.674286 15.433142l191.561143 191.756191a73.142857 73.142857 0 0 1-22.137905 118.564571l-67.876572 30.061715-127.341714 127.488-10.093714 140.239238a73.142857 73.142857 0 0 1-124.684191 46.445714l-123.66019-123.782095-210.724572 211.699809-51.833904-51.614476 210.846476-211.821714-127.926857-128.024381a73.142857 73.142857 0 0 1 46.299428-124.635429l144.237715-10.776381 125.074285-125.220571 29.379048-67.779048a73.142857 73.142857 0 0 1 96.207238-38.034285z"
          fill="#bfbfbf" p-id="7491"></path></svg>
        </span>
        
        <p class="font-bold text-4xl mb-2">
          导入答卷
        </p>
        
        <div class="item-detail">
          <div class="import-type flex justify-center gap-x-20 ">
            <div :class="{'import-type-now':importTypeNow==='paper'}" @click="toggleImportType('paper')">
              <span>按试卷ID导入</span>
              <text>
                <text v-if="importTypeNow==='exam'">⚫</text>
                <text v-else>⚪</text>
                一次只能导入一张试卷
              </text>
              <text>
                <text v-if="importTypeNow==='exam'">⚫</text>
                <text v-else>⚪</text>
                文件名无格式要求
              </text>
            </div>
            <div :class="{'import-type-now':importTypeNow==='exam'}" @click="toggleImportType('exam')">
              <span>按考试ID导入</span>
              <text>
                <text v-if="importTypeNow==='paper'">⚫</text>
                <text v-else>⚪</text>
                可以批量导入试卷
              </text>
              <text>
                <text v-if="importTypeNow==='paper'">⚫</text>
                <text v-else>⚪</text>
                文件名格式：以“
                <text :class="{'text-red':importTypeNow==='paper'}" class="font-bold">试卷ID-</text>
                ”开头的jpg或png
              </text>
            </div>
          </div>
          
          <div class="import-area">
            <div v-if="importTypeNow==='paper'" class="flex items-center gap-x-2 pl-8" style="margin-top:10px;">
              <span class="font-bold  h-[30px] flex justify-center items-center mr-1">试卷ID</span>
              <input v-model.trim="importPaperId" class="border-none rounded-lg indent-2 h-[32px]"
                     style="outline: none;"
                     type="text">
              <span class="btn btn-ghost bg-white hover:bg-secondary btn-sm h-[20px]"
                    @click="queryImportPaper">刷新</span>
            </div>
            <div v-else class="flex items-center gap-x-2 pl-8" style="margin-top:10px;">
              <span class="font-bold  h-[30px] flex justify-center items-center mr-1">考试ID</span>
              <input v-model.trim="importExamId" class="border-none rounded-lg indent-2 h-[32px]" style="outline: none;"
                     type="text">
              <span class="btn btn-ghost bg-white hover:bg-secondary btn-sm h-[20px]"
                    @click="queryImportExam">刷新</span>
            </div>
            
            <div v-show="importTypeNow==='paper' && importPaper!==''"
                 class="flex flex-col pt-2 pl-4  justify-between  rounded-lg bg-white "
                 style="margin-top:20px;">
              <p class="font-bold text-lg mb-2.5 my-0">试卷信息</p>
              <div class=" flex flex-wrap gap-x-4 gap-y-1.5">
                <span><text class="font-bold">试卷ID：</text>{{ importPaper.id }}</span>
                <span><text class="font-bold">考试ID：</text>{{ importPaper.examId }}</span>
                <span><text class="font-bold">学生ID：</text>{{ importPaper.studentId }}</span>
                <span class="font-bold"><text>是否上传过试卷：</text>{{ importPaper.hasUrl === 1 ? '是' : '否' }}</span>
                <span v-if="importPaper.hasUrl===1"><text class="font-bold">答卷地址：</text>
                  <a :class="{'cursor-not-allowed':importPaper.url===null || importPaper.url===''}"
                     :href="'http://'+importPaper.url"
                     class="cur text-blue-500"
                     target="_blank">{{ importPaper.url === null ? '已失效' : '*****' + importPaper.url.slice(14) }}</a>
                </span>
              </div>
            </div>
            <div v-show="importTypeNow==='paper' && importPaper!==''" style="margin-top:20px;">
              <PaperUpload :paper-id="importPaperId" @uploadsuccess="queryImportPaper"/>
            </div>
            <div v-show="importTypeNow==='exam' && importExam!==''"
                 class="flex flex-col pt-2 pl-4  justify-between  rounded-lg bg-white "
                 style="margin-top:20px;">
              <p class="font-bold text-lg mb-2.5 mt-0.5">考试信息</p>
              <div class=" flex flex-wrap gap-x-4 gap-y-1.5 ">
                <span><text class="font-bold">考试ID：</text>{{ importExam.id }}</span>
                <span><text class="font-bold">考试名称：</text>{{ importExam.name }}</span>
                <span><text class="font-bold">考试时间：</text>{{
                    String(importExam.examTime).slice(0, 19).replace('T', " ")
                  }}</span>
                <span><text class="font-bold">考试地点：</text>{{ importExam.examPos }}</span>
                <span><text class="font-bold">报考人数：</text>{{ importExam.num }}</span>
                <span><text class="font-bold">已考人数：</text>{{ importExam.num - importExam.countUntested }}</span>
                <span><text class="font-bold">未导入答卷数：</text>{{ importExam.num - importExam.countHasUrl }}</span>
              </div>
              
              <p class="font-bold text-base mb-2.5 my-0 pt-1.5 mr-4 border-t border-t-[#ccc] flex items-center">
                未导入试卷的ID-所属学生ID-是否已参考
                <text class="text-xs ml-1 font-normal text-gray-500">(名单过长时可横向滚动)</text>
              </p>
              <div>
                <NScrollbar style="min-height:30px; max-height:40px;  max-width:calc(45vw); white-space: nowrap;"
                            x-scrollable>
                    <span v-for="item in unimportedPapers"
                          class="mr-2 mt-10 cursor-pointer  px-1.5 pt-1 pb-0.5  text-sm bg-[#f5f5f5] hover:bg-secondary"
                          style="border-radius:0.5rem 0.5rem 0.4rem 0.4rem;">
                      {{ item.id }}-{{ item.studentId }}-<text
                        :class="{'text-red':item.tested===0}">{{ item.tested === 0 ? '否' : '是' }}</text>
                    </span>
                </NScrollbar>
              </div>
            
            
            </div>
            <div v-show="importTypeNow==='exam' && importExam!==''" style="margin-top:20px;">
              <ExamUpload :paper-id="importExamId"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  <!--  阅考试提交阅卷弹窗-->
  <Dialog v-model:visible="checkExamShow" :pt="{
        root: 'border-none',
        mask: { style: 'backdrop-filter: blur(2px)'  }
    }"
          :style="{ width: '24rem' }" header="阅考试" modal position="top">
    
    <div class=" flex flex-col gap-y-5">
      <div class="flex flex-col gap-y-1.5 text-base">
        <p class="text-lg font-bold mb-2">阅卷信息</p>
        <span>⚫ 共选择 {{ examStore.examsChoosen.length }} 场考试</span>
        <span>⚫ 涉及 {{ examStore.examsChoosen.reduce((sum, item) => sum + item.num, 0) }} 份试卷</span>
        <span>⚫ 已导入 {{ examStore.examsChoosen.reduce((sum, item) => sum + item.countHasUrl, 0) }} 份试卷</span>
      </div>
      
      <div class="flex flex-col gap-y-1.5 text-base">
        <p class="text-lg font-bold mb-2">阅卷码</p>
        <span>⚫ 当前账号为管理员账号，已跳过核验阅卷码信息</span>
        <span>⚫ 阅卷流程将记录在后台日志</span>
      </div>
    </div>
    
    <div class="my-4">
      <span class="text-[#05aeec] font-bold">请选择下一步操作：</span>
    </div>
    <div class=" flex justify-between w-full text-lg mt-6 px-4">
      <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-secondary "
            @click="checkExamShow=false">取消</span>
      <span class="border-none btn btn-sm px-4 bg-[#05aeec] text-white hover:text-black hover:bg-secondary"
            @click="checkExamShow=false">查看试卷详情</span>
      <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-secondary"
            @click="submitCheckExams">阅卷</span>
    </div>
  
  </Dialog>

</template>

<style lang="scss" scoped>
  
  .aicheck-main {
    width: calc(100vw - 2.75rem);
    max-width: 100%;
    overflow: hidden;
    border-radius: 0.5rem;
    height: calc(100vh - 80px - 1.25rem);
    
    .item {
      max-width: 65vw !important;
      padding: 1rem;
      width: 25%;
      box-sizing: border-box;
      height: 100%;
      transition: all 0.8s;
      background-color: #f5f5f5;
      border-radius: 1rem 0 0 1rem;
      
      .item-detail {
        display: none;
      }
      
      div {
        margin: 0 0 0.5rem;
      }
      
      .item-tip {
        margin: 1rem 0;
        font-weight: bold;
      }
      
      .item-tip::before {
        content: '⚫';
        margin-right: 0.2rem;
      }
    }
    
    .keepitem {
      width: 90%;
      transition: all 0.8s;
      border: 1.5px #05aefc solid !important;
      margin-right: 10px;
      
      > p {
        color: #05aefc;
      }
      
      .item-tip {
        display: none;
      }
      
      .item-detail {
        display: block;
      }
    }
    
    .import-type {
      padding: 0.25rem;
      
      > div {
        cursor: pointer;
        display: flex;
        flex-direction: column;
        padding: 0.5rem 1rem;
        border-radius: 0.75rem;
        background-color: #fff;
        
        > span {
          font-weight: bold;
          font-size: 0.9rem;
          margin-bottom: 0.25rem;
        }
        
        > text {
          font-size: 0.8rem;
          margin-top: 0.25rem;
        }
      }
      
      > div.import-type-now {
        background-color: #05aeec;
        color: #fff;
      }
    }
    
    > div.item-2 {
      > div.item-detail {
        > div.paper-chooser {
          
          > span {
            width: calc(50% - 1rem);
            border-radius: 0.5rem;
            padding: 0.25rem;
            flex: 1 1 auto;
            display: flex;
            align-items: center;
            
            > text {
              width: 80px;
              font-weight: bold;
            }
            
            > input {
              flex: 1 1 auto;
              padding: 0.35rem 0.5rem;
              border-radius: 0.5rem;
              text-indent: 0.5ch;
              outline: none !important;
            }
          }
        }
        
        > div.paper-choose-out {
          .paper {
            box-sizing: border-box;
            padding: 0.35rem 0.65rem;
            margin-bottom: 0.6rem;
            background-color: #f5f5f5;
            border-radius: 0.5rem;
            row-gap: 0.25rem;
            
            > span {
              
              > text {
                font-weight: bold;
              }
            }
          }
          
          .paper:last-child {
            margin-bottom: 1rem !important;
          }
        }
      }
    }
    
    
  }
  
  .text-red {
    color: #f00 !important;
  }
  
  .choosen-text-info{
    background-color: #05aeec;
    color:#fff;
    
  }
  
  .choosen-text-info:hover{
    background-color:#bbf8ae;
    color:#000;
  }
  
  .item-2 {
    .all-choosen {
      background-color: #05aeec !important;
      color: #fff !important;
    }
    
    .choosen {
      border: 1.5px #05aeec solid !important;
    }
  }
  
  //others
  .aicheck-main > .item > .item-holder {
    position: absolute;
    right: 1rem;
    top: 1rem;
    cursor:pointer;
    //background-color: #f00;
  }
</style>
