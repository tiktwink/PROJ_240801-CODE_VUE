<script>
  import {NScrollbar} from "naive-ui";
  import useSubjectStore from "@/stores/useSubjectStore.js";
  import useUserStore from "@/stores/useUserStore.js";
  import Dialog from 'primevue/dialog';
  import axios from "axios";
  import useAppStore from "@/stores/useAppStore.js";
  
  export default {
    data() {
      return {
        appStore: useAppStore(),
        subjectStore: useSubjectStore(),
        itemsChoosen: [],
        subjects: useSubjectStore().subjects.map(item => {
          return {...item, choosen: false}
        }),
        deleteDialogShow: false,
        addDialogShow: false,
        addSubjectId: undefined,
        addSubjectTitle: '',
        addSubjectType: '类别',
        addSubjectSuggGrade: '建议修读年级',
        addSubjectNote: undefined,
        
      }
    },
    watch: {
      itemsChoosen: {
        deep: true,
        handler(newValue, oldValue) {
          this.subjectStore.subjectsChoosen = this.itemsChoosen
          // console.log('subject仓库中subjectsChoosen值已变')
        }
      }
    },
    name: 'Subject',
    components: {
      NScrollbar,
      Dialog
    },
    mounted() {
      //收起、但显示右侧栏
      this.appStore.sideOn = false
      document.getElementById('rside').style.visibility = 'visible'
      
      this.queryAllSubjects()
    },
    
    methods: {
      queryAllSubjects() {
        axios.get('/api/subject/queryall', {
          headers: {
            token: useUserStore().token,
            username: useUserStore().username
          }
        }).then(res => {
          this.subjects = res.data.data
        })
      },
      chooseSubject(item, index) {
        
        this.subjects = this.subjects.map(item => {
              return {
                ...item,
                choosen: false
              }
            }
        )
        
        this.subjects[index].choosen = true
        this.itemsChoosen = [item]
      },
      deleteSubject() {
        this.deleteDialogShow = false
        // let i = 0
        // for (i = 0; i < this.itemsChoosen.length; i++) {
        //   this.subjectStore.subjects = this.subjectStore.subjects.filter(item => item.id !== this.itemsChoosen[i].id)
        // }
        
        axios.delete(`/api/subject/${this.itemsChoosen[0].id}`, {
          headers: {
            username: useUserStore().username,
            token: useUserStore().token
          }
        }).then(res => {
          if (res.data.code === 0) {
            this.$toast.add({
              severity: 'success',
              summary: '删除科目成功！删除的科目ID：' + res.data.data,
              // detail: 'Message Content',
              life: 3000
            });
          } else {
            this.$toast.add({
              severity: 'success',
              summary: '删除科目失败！请自查原因或联系管理员',
              // detail: 'Message Content',
              life: 3000
            });
          }
        }).catch(err => {
          this.$toast.add({
            severity: 'success',
            summary: '删除科目错误！请自查原因或联系管理员',
            // detail: 'Message Content',
            life: 3000
          });
        })
        
        this.queryAllSubjects()
        this.itemsChoosen = []
      },
      addSubject() {
        if (this.addSubjectId === undefined || this.addSubjectTitle === '' || this.addSubjectType === '类别' || this.addSubjectSuggGrade === '建议修读年级') {
          this.$toast.add({
            severity: 'warn',
            summary: '有必填信息未填！',
            life: 1500
          })
          return
        }
        /*if (this.subjectStore.subjects.some(item => item.id === this.addSubjectId)) {
          this.$toast.add({
            severity: 'warn',
            summary: '科目ID重复！',
            life: 1500
          })
          return;
        }*/
        //正式发起添加请求
        axios.put('/api/subject/index', {
          
          id: this.addSubjectId,
          title: this.addSubjectTitle,
          type: this.addSubjectType,
          sugsGrade: this.addSubjectSuggGrade,
          num: 0,
          note: this.addSubjectNote
          
          
        }, {
          headers: {
            username: useUserStore().username,
            token: useUserStore().token
          }
        }).then(res => {
          console.log(res)
          if (res.data.code === 0) {
            this.queryAllSubjects()
            this.itemsChoosen = []
            this.addDialogShow = false
            this.$toast.add({
              severity: 'success',
              summary: '新增科目成功！科目ID：' + this.addSubjectId,
              detail: '通知显示10s后自动隐藏',
              life: 10000,
            })
          } else {
            this.$toast.add({
              severity: 'error',
              summary: '新增科目失败！请自查原因或联系管理员',
              detail: '通知显示3s后自动隐藏',
              life: 3000,
            })
          }
        })
        .catch(err => {
          this.$toast.add({
            severity: 'error',
            summary: '新增科目错误！请自查原因或联系管理员',
            detail: '通知显示3s后自动隐藏',
            life: 3000,
          })
        })
        
        
      },
      diffChoose() {
        this.subjects = this.subjects.map(item => {
          return {
            ...item,
            choosen: !item.choosen
          }
        })
        
        this.itemsChoosen = this.subjects.filter(item => item.choosen)
      },
      allChoose() {
        if (this.subjects.every(item => item.choosen)) {
          this.subjects = this.subjects.map(item => {
            return {
              ...item,
              choosen: false
            }
          })
          // console.log('yse')
          this.itemsChoosen = []
        } else {
          this.subjects = this.subjects.map(item => {
            return {
              ...item,
              choosen: true
            }
          })
          // console.log('no')
          this.itemsChoosen = this.subjects.slice(0) //不能直接相等，要用slice函数复制
        }
      },
      ubject() {
      
      },
    },
  }
</script>

<template>
  <div id="subject" class=" flex flex-col items-center p-2">
    
    <span class=" text-base mt-4 mb-4 pt-0.5 px-2 font-normal"
          style=" width:300px; text-align:left; vertical-align: center; ">
        TIP：点击科目可以选中并进行操作
      </span>
    <div class="subject-prop-title flex gap-x-2 relative">
      <span>科目ID</span>
      <span>科目名称</span>
      <span>科目类别</span>
      <span>建议修读年级</span>
      <span>修读人数</span>
      <span>备注</span>
      
      
      <div class="flex gap-x-2 absolute right-0 bottom-[0.15rem]">
        <div class="btn btn-ghost btn-sm hover:bg-secondary bg-[#f5f5f5]" @click="addDialogShow=true">新增</div>
        <div class="btn btn-ghost btn-sm hover:bg-secondary bg-[#f5f5f5]" @click="deleteDialogShow=true">删除</div>
        <div :class="{'all-choose':subjects.every(item => item.choosen)}"
             class="btn btn-ghost btn-sm hover:bg-secondary bg-[#f5f5f5]"
             @click="allChoose">全选
        </div>
        <!-- <div class="btn btn-ghost btn-sm hover:bg-secondary bg-[#f5f5f5]" @click="diffChoose">反选</div>-->
      </div>
    </div>
    <NScrollbar
        style="padding:0.8rem; max-height:450px; min-height:100px; min-width:70vw;  border:1px #ccc solid; border-radius:0.5rem; background-color:#fff;">
      <div v-for="(item,index) in subjects" :class="{'subject-choosen': item.choosen}"
           class="subject rounded-lg flex items-center gap-x-2 px-2 py-4 cursor-pointer bg-[#f5f5f5] hover:bg-secondary "
           @click="chooseSubject(item,index)">
        <div class="subject-prop">{{ item.id }}</div>
        <div class="subject-prop">{{ item.title }}</div>
        <div class="subject-prop">{{ item.type }}</div>
        <div class="subject-prop">{{ item.sugsGrade }}</div>
        <div class="subject-prop">{{ item.num }}</div>
        <div class="subject-prop">{{ item.note === undefined ? '无' : item.note }}</div>
      </div>
    
    </NScrollbar>
    
    
    <!--    删除对话框-->
    <Dialog v-model:visible="deleteDialogShow" :style="{ width: '20rem', padding:'0.5rem', 'border-radius':'1rem' }"
            header="删除科目" modal>
      
      <div v-if="itemsChoosen.length>0">
        <div class="text-lg mb-4">将要删除的科目：</div>
        <div>
          <div v-for="(item,index) in itemsChoosen" class="text-base flex gap-x-2 bg-accent rounded-lg my-1.5 p-2">
            <span>{{ item.id }}</span>
            <span>{{ item.title }}</span>
          </div>
        </div>
        
        <div class="flex justify-between w-full mt-5">
          <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-secondary"
                @click="deleteDialogShow=false">取消</span>
          <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-error hover:text-white" @click="deleteSubject">确认删除</span>
        </div>
      
      </div>
      
      <div v-else>没有选中任何科目！</div>
    
    </Dialog>
    
    <!--    新增对话框-->
    <Dialog v-model:visible="addDialogShow" :style="{ width: '20rem', padding:'0.5rem', 'border-radius':'1rem' }"
            header="新增科目" modal>
      
      <div class="text-lg mb-3">新增科目信息：</div>
      <div class="add-dialog flex flex-wrap gap-y-2 gap-x-2 ">
        
        <div class="flex justify-between items-center w-[16rem]">
          <span>ID：</span>
          <input v-model.trim="addSubjectId" class="h-[35px] w-[14rem] rounded-md bg-accent"
                 style="text-indent:1ch; outline:none; border:none; box-sizing: border-box; " type="text">
        </div>
        <div class="flex items-center justify-between w-[16rem]">
          <span>名称：</span>
          <input v-model.trim="addSubjectTitle" class="h-[35px] w-[13rem]  rounded-md bg-accent"
                 style="text-indent:1ch; outline:none;border:none;  box-sizing: border-box; " type="text">
        </div>
        <div class="flex justify-between w-[16rem]">
          
          <div class="flex flex-col">
            <span class="mb-1">类别:</span>
            <select v-model="addSubjectType" class="select outline-none h-[25px] w-[7.6rem] rounded-lg bg-accent"
                    style="outline:none; border:none;  box-sizing: border-box; height:35px!important;">
              <!--            <option disabled selected>类别</option>-->
              <option style="height: 35px!important; ">文科类</option>
              <option style="height: 35px!important; ">理科类</option>
              <option style="height: 35px!important; ">工科类</option>
              <option style="height: 35px!important; ">社科类</option>
              <option style="height: 35px!important; ">医科类</option>
            </select>
          
          </div>
          
          <div class="flex flex-col">
            <span class="mb-1">建议修读年级:</span>
            <select v-model="addSubjectSuggGrade"
                    class="select outline-none max-h-[25px] w-[7.6rem] rounded-lg bg-accent "
                    style="outline:none; border:none;  box-sizing: border-box; ">
              <!--            <option disabled selected>建议修读年级</option>-->
              <option selected>7</option>
              <option>8</option>
              <option>9</option>
              <option>10</option>
              <option>11</option>
              <option>12</option>
            </select>
          </div>
        
        </div>
        
        <input v-model.trim="addSubjectNote" class="h-[35px] w-[15rem] rounded-lg bg-accent" maxlength="150"
               placeholder="备注(150字以内)"
               style=" width:16rem; text-indent:1ch; font-size:0.85rem; outline:none; border:none;  box-sizing: border-box; "
               type="text">
      </div>
      
      <div class="flex justify-between w-full mt-5">
          <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-secondary"
                @click="addDialogShow=false">取消</span>
        <span class="border-none btn btn-sm px-4 bg-[#f5f5f5] hover:bg-error hover:text-white"
              @click="addSubject">确认新增</span>
      </div>
    
    </Dialog>
  
  </div>
</template>

<style lang="scss" scoped>
  .add-dialog {
    span {
      font-size: 0.85rem;
    }
  }
  
  .all-choose {
    background-color: #05aeec !important;
    color: #fff !important;
  }
  
  .subject {
    &.subject-choosen {
      background-color: #05aeec !important;
      color: #ffffff !important;
      
      .subject-prop {
        border-right: #fff 1px solid;
      }
    }
    
    //border-bottom: 1px #ccc solid ;
    margin-bottom: 0.75rem;
    height: 30px;
    border-radius: 0.5rem;
    
    .subject-prop {
      width: 80px;
      text-align: center;
      border-right: #555 1px solid;
      padding-right: 0.5rem;
      
    }
    
    .subject-prop:last-child {
      border-right: none;
      padding-right: 0;
    }
    
    .subject-prop:nth-child(6) {
      width: 500px;
      text-align: left;
      padding: 0 2rem 0 calc(50px - 2.25ch);
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
    }
    
    .subject-prop:nth-child(4) {
      width: 100px;
    }
    
    
  }
  
  .subject:last-child {
    //border-bottom:none;
    margin-bottom: 1.75rem;
  }
  
  .subject-prop-title {
    padding: 0.5rem 1.35rem;
    min-width: 70vw;
    margin-bottom: 0.25rem;
    //background-color: #f00;
    > span {
      font-weight: bold;
      width: 80px;
      text-align: center;
      border-right: #555 1px solid;
      padding-right: 0.5rem;
    }
    
    span:nth-child(4) {
      width: 100px;
    }
    
    span:nth-child(6) {
      width: 100px;
    }
    
    span:last-child {
      border-right: none;
      padding-right: 0;
    }
    
    span:nth-child(6) {
      border-right: none;
    }
  }
</style>
