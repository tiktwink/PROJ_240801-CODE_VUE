<script>
  import PaperEmpty from "@/components/paper/PaperEmpty.vue";
  import PaperFilled from "@/components/paper/PaperFilled.vue";
  import PaperRefer from "@/components/paper/PaperRefer.vue";
  import PaperDetail from "@/components/paper/PaperDetail.vue";
  import {NRadio, NRadioGroup, NSwitch} from "naive-ui";
  import useAppStore from "@/stores/useAppStore.js";
  import {axiosJson} from "@/apis/axios/axiosJson.js";
  
  export default {
    name: 'Paper',
    
    components: {
      PaperEmpty, PaperFilled, PaperRefer, PaperDetail, NRadioGroup, NRadio, NSwitch
    },
    data() {
      return {
        appStore: useAppStore(),
        compName: 'PaperBase',
        paperType: '答卷',
        examId: '',
        examName: '',
        paperId: '',
        studentId: "",
        studentName: '',
        onlyLatest30Day: false,
        papers: [],
        turnQueryPaper: false,
      }
    },
    
    methods: {
      queryPaper() {
        this.papers = []
        this.turnQueryPaper = true
        if (this.paperType === '答卷') {
          console.log('dajuan')
          axiosJson.get(`/api/paper/indexs?id=${this.paperId}&examId=${this.examId}&examName=${this.examName}&studentId=${this.studentId}&studentName=${this.studentName}`)
          .then(response => {
            if (response.data.code === 0) {
              // console.log(response.data.data)
              this.papers = response.data.data
            }
          })
        }
        
      }
    },
    // computed: {
    //   compName() {
    //     return 'PaperBase'
    //   }
    // },
  }
</script>


<template>
  <div class="w-full h-full flex bg-accent rounded-lg">
    <div class="p-4 h-full w-[20%] min-w-[300px] border-box relative" style="border-right:1px #ccc solid;">
      <p class="font-bold text-xl mb-2 ">查询试卷</p>
      <span class="btn btn-sm absolute border-none right-4 top-4 font-normal bg-white hover:bg-secondary"
            @click="queryPaper">查询/刷新</span>
      <div class="chooser relative pl-2">
        <p class="font-bold text-lg mb-2.5 ">试卷类型
          <text class="ml-1.5 font-normal text-sm rounded bg-info text-white cursor-pointer px-1.5 py-0.5">
            {{ paperType }}
          </text>
        </p>
        <n-radio-group v-model:value="paperType">
          <div class="flex justify-center border-none items-center gap-x-0.5 pl-1 text-base">
            <!--            <n-radio label="全部" value="全部"/>-->
            <n-radio label="答卷" value="答卷"/>
            <n-radio label="底卷" value="底卷"/>
            <n-radio label="答案" value="答案"/>
          </div>
        </n-radio-group>
      </div>
      
      <div v-if="paperType==='底卷'" class="args pl-2 mt-4 relative">
        <p class="font-bold text-lg mb-2 ">查询条件</p>
        <div class="text-[#aaa] text-xs mt-3 mb-1 pl-1">若未输入条件，将查询对应类型的所有试卷</div>
        <span class="absolute btn btn-sm bg-white border-none hover:bg-secondary right-0 font-normal top-0"
              @click="paperId='';examId='';examName='';studentName='';studentId=''">重填</span>
        <div class="flex flex-col gap-y-1.5 pl-1 text-base">
          <span :class="{'text-info':onlyLatest30Day}" class="flex items-center">仅查询最近30天 <input
              v-model="onlyLatest30Day"
              class="toggle toggle-sm toggle-info mx-2"
              style="height:16px; width:28px;"
              type="checkbox"/></span>
          <span :class="{'text-info':examId!==''}" class="flex items-center"><text class="w-fit">所属考试ID：</text>
            <input v-model.trim="examId" class="flex-auto py-0.5 rounded indent-2" placeholder="精确查询"
                   type="text"></span>
          <span :class="{'text-info':examName!==''}" class="flex items-center ">
            <text class="min-w-[6.4rem]">所属考试名称：</text>
            <input v-model.trim="examName" class="w-[9.5rem] py-0.5 flex-auto rounded indent-2" placeholder="模糊查询"
                   type="text"></span>
        </div>
      </div>
      
      <div v-else-if="paperType==='答案'" class="args pl-2 mt-4 relative">
        <p class="font-bold text-lg mb-2 ">查询条件</p>
        <div class="text-[#aaa] text-xs mt-3 mb-1 pl-1">若未输入条件，将查询对应类型的所有试卷</div>
        <span class="absolute btn btn-sm bg-white border-none hover:bg-secondary right-0 font-normal top-0"
              @click="paperId='';examId='';examName='';studentName='';studentId=''">重填</span>
        <div class="flex flex-col gap-y-1.5 pl-1 text-base">
          <span :class="{'text-info':onlyLatest30Day}" class="flex items-center">仅查询最近30天 <input
              v-model="onlyLatest30Day"
              class="toggle toggle-sm toggle-info mx-2"
              style="height:16px; width:28px;"
              type="checkbox"/></span>
          <span :class="{'text-info':examId!==''}" class="flex items-center"><text class="w-fit">所属考试ID：</text>
            <input v-model.trim="examId" class="flex-auto py-0.5 rounded indent-2" placeholder="精确查询"
                   type="text"></span>
          <span :class="{'text-info':examName!==''}" class="flex items-center ">
            <text class="min-w-[6.4rem]">所属考试名称：</text>
            <input v-model.trim="examName" class="w-[9.5rem] py-0.5 flex-auto rounded indent-2" placeholder="模糊查询"
                   type="text"></span>
        </div>
      </div>
      
      <div v-else-if="paperType==='答卷'" class="args pl-2 mt-4 relative">
        <p class="font-bold text-lg mb-2 ">查询条件</p>
        <div class="text-[#aaa] text-xs mt-3 mb-1 pl-1">若未输入条件，将查询对应类型的所有试卷</div>
        <span class="absolute btn btn-sm bg-white border-none hover:bg-secondary right-0 font-normal top-0"
              @click="paperId='';examId='';examName='';studentName='';studentId=''">重填</span>
        <div class="flex flex-col gap-y-1.5 pl-1 text-base">
          <span :class="{'text-info':onlyLatest30Day}" class="flex items-center">仅查询最近30天 <input
              v-model="onlyLatest30Day"
              class="toggle toggle-sm toggle-info mx-2"
              style="height:16px; width:28px;"
              type="checkbox"/></span>
          <span :class="{'text-info':paperId!==''}" class="flex items-center"><text class="w-fit">答卷ID：</text>
            <input v-model.trim="paperId" class="flex-auto py-0.5 rounded indent-2" placeholder="精确查询"
                   type="text"></span>
          <span :class="{'text-info':examId!==''}" class="flex items-center"><text class="w-fit">考试ID：</text>
            <input v-model.trim="examId" class="flex-auto py-0.5 rounded indent-2" placeholder="精确查询"
                   type="text"></span>
          <span :class="{'text-info':examName!==''}" class="flex items-center ">
            <text class="min-w-[4.4rem]">考试名称：</text>
            <input v-model.trim="examName" class="w-[9.5rem] py-0.5 flex-auto rounded indent-2" placeholder="模糊查询"
                   type="text"></span>
          <span :class="{'text-info':studentId!==''}" class="flex items-center"><text class="w-fit">所属学生ID：</text>
            <input v-model.trim="studentId" class="flex-auto py-0.5 rounded indent-2" placeholder="精确查询"
                   type="text"></span>
          <span :class="{'text-info':studentName!==''}" class="flex items-center ">
            <text class="min-w-[6.4rem]">所属学生姓名：</text>
            <input v-model.trim="studentName" class="w-[9.5rem] py-0.5 flex-auto rounded indent-2"
                   placeholder="模糊查询"
                   type="text"></span>
        </div>
      </div>
      
      <p class="text-lg font-bold pl-2 mt-4 mb-3">查询结果 <span v-show="papers.length>0"
                                                                 class="text-sm font-normal bg-info text-white rounded px-1.5 py-0.5">共{{ papers.length }}条记录</span>
      </p>
      <div class="paper-wrapper p-1 rounded-lg bg-white">
        <NScrollbar style="max-height: 190px; min-height:190px; border:0 #ccc solid;">
          <div v-if="turnQueryPaper && papers.length===0" class="flex justify-center pt-4">
            <span class="text-red-500">未查询到数据...o_O</span>
          </div>
          <div v-else-if="paperType==='答卷'">
            <div v-for="(paper,index) in papers" class="paper-brief ">
              <div class=" flex gap-y-1 gap-x-3 flex-wrap px-1.5 py-0.5 rounded">
                <span>答卷ID：{{ paper.id }}</span>
                <span>学生ID：{{ paper.studentId }}</span>
                <span>考试ID：{{ paper.examId }}</span>
                <span>已阅：{{ paper.checked === 0 ? '否' : '是' }}</span>
              </div>
            </div>
          
          </div>
          <div v-else-if="paperType==='底卷'">
            <div></div>
          </div>
          <div v-else-if="paperType==='答案'">
            <div></div>
          </div>
        </NScrollbar>
      </div>
    
    </div>
    
    <div class="p-4 h-full w-[80%] min-w-[300px]">
      
      <PaperEmpty/>
      <PaperFilled/>
      <PaperRefer/>
    </div>
  </div>
</template>


<style lang="scss" scoped>
  .paper-wrapper {
    .paper-brief {
      cursor: pointer;
      font-size: 0.8rem;
      border-bottom: #ccc solid 1px;
      margin-bottom: 0.25rem;
      padding-bottom: 0.25rem;
      padding-inline: 0.1rem;
      
      > div:hover {
        background-color: #bbf8ae;
      }
    }
    
    .paper-brief:last-child {
      border: none;
      margin-bottom: 0;
    }
    
  }
  
  input {
    outline: none !important;
  }
</style>
