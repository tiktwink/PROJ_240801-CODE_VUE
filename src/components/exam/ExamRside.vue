<script>
  import useAppStore from "@/stores/useAppStore.js";
  import useExamStore from "@/stores/useExamStore.js";
  import {NScrollbar} from "naive-ui";
  import axios from "axios";
  import useUserStore from "@/stores/useUserStore.js";
  // import {random} from "nanoid";
  
  export default {
    data() {
      return {
        appStore: useAppStore(),
        examStore: useExamStore(),
        
        studentList: [],
        studentsShow: true,
        
        papers: '',
      }
    },
    computed: {
      examChoosen() {
        return this.examStore.examChoosen
      }
    },
    unmounted() {
      this.examStore.examChoosen = ''
    },
    
    watch: {
      examChoosen(newValue, oldValue) {
        console.log(newValue.id)
        
        //无试卷ID，无考生ID，只差考试ID:examId
        axios.get(`/api/paper/indexs?id=&examId=${newValue.id}&studentId=`, {
          headers: {
            username: useUserStore().username,
            token: useUserStore().token,
          }
        })
        .then(res => {
          // console.log(res)
          if (res.data.code === 0) {
            this.papers = res.data.data
          } else {
            //没查到，应该将papers重置
            this.papers = []
          }
        })
        .catch(err => {
          //没查到(异常)，应该将papers重置
          this.papers = []
        })
      }
    },
    components: {
      NScrollbar,
    },
  }
</script>

<template>
  <div>
    <div v-if="examChoosen!==''" class="exam-choosen pt-4 pb-2 pl-5 pr-1 flex flex-col gap-y-2" @click.prevent.stop="">
      <p>考试详情</p>
      <div><span>考试ID：</span>
        <text>{{ examChoosen.id }}</text>
      </div>
      <div><span style="width:82.5px; min-width: 82.5px; ">考试名称：</span>
        <text style="word-break:break-all; ">{{ examChoosen.name }}</text>
      </div>
      <div><span>参考人数：</span>
        <text>{{ examChoosen.num }}</text>
      </div>
      <div><span>已阅：</span>
        <text>{{ examChoosen.checked === 1 ? '是' : '否' }}</text>
      </div>
      <div><span>复阅：</span>
        <text>{{ examChoosen.doubleChecked === 1 ? '是' : '否' }}</text>
      </div>
      
      <div v-show="studentsShow" class="students mt-2 py-1 px-1 mr-4 rounded bg-white" @click.prevent.stop="">
        <NScrollbar style="min-height:75px; max-height: 270px; border:1px #ccc solid; border-radius:0.25rem;">
          <div v-if="papers.length>0" class="papsers">
            <div v-for="(item, index) in papers"
                 class="paper hover:bg-secondary cursor-pointer flex flex-wrap gap-x-4 gap-y-0.5 py-1 px-2 ">
              <div>
                <span>学生ID：</span>
                <text>{{ item.studentId }}</text>
              </div>
              <div>
                <span>报名时间：</span>
                <text>{{ String(item.createTime).slice(0, 19).replace('T', ' ') }}</text>
              </div>
              <div>
                <span>已参考：</span>
                <text>{{ item.tested === 0 ? '否' : '是' }}</text>
              </div>
              <div>
                <span>已阅：</span>
                <text>{{ item.checked === 0 ? '否' : '是' }}</text>
              </div>
            </div>
          </div>
          <div v-else class="text-lg text-error flex justify-center items-center pt-[25px]">
            本场考试暂无考生
          </div>
        </NScrollbar>
      </div>
      
      <!-- <div>{{ examChoosen.doubleChecked }}</div>-->
      <div class="handles flex flex-col w-full justify-center gap-y-3 py-2 pr-4">
        <!--        v-show="examChoosen.num>0"-->
        <div class="handle btn btn-sm px-4 bg-white hover:bg-secondary"
             @click.prevent.stop="studentsShow=!studentsShow">
          {{ studentsShow ? '隐藏考生名单' : '查看考生名单' }}
        </div>
        <div v-show="examChoosen.checked===0" class="handle btn btn-sm px-4 bg-white hover:bg-secondary"
             @click.prevent.stop="">提交阅卷
        </div>
        <div v-show="examChoosen.checked===1 && examChoosen.doubleChecked===0"
             class="handle btn btn-sm px-4 bg-white hover:bg-secondary"
             @click.prevent.stop="">提交复阅
        </div>
      </div>
    </div>
    <div v-else>
      <div class="text-center w-full p-4 text-lg">点击选中一个考试查看详情并进行管理</div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
  .exam-choosen {
    > p {
      font-weight: bold;
      font-size: 1.25rem;
      margin-top: 0.25rem;
      margin-bottom: 0.5rem;
    }
    
    > div {
      display: flex;
      align-items: center;
      
      > span {
        font-weight: bold;
        font-size: 1rem;
      }
      
      > text {
        font-size: 0.85rem;
      }
    }
    
    .handles {
      
      .handle {
        border: none;
      }
    }
    
    .papsers {
      > .paper {
        border-bottom: 1px #ccc solid;
        
        > div {
          display: flex;
          align-items: center;
          
          > span {
            font-weight: bold;
          }
          
          > text {
            font-size: 0.85rem;
          }
        }
      }
      
      > .paper:last-child {
        border: none;
      }
    }
  }


</style>
