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
        turnScanPaper: false,
        paperScan: '',
      }
    },
    mounted() {
      this.appStore.sideOn = false
      document.getElementById('rside').style.visibility = 'visible';
    },
    
    watch: {
      paperType(newValue, oldValue) {
        this.papers = []
      }
    },
    
    methods: {
      queryPaper() {
        // this.papers = []
        // this.paperScan=''
        this.turnQueryPaper = true
        // this.turnScanPaper = false
        if (this.paperType === '答卷') {
          console.log('dajuan')
          axiosJson.get(`/api/paper/indexs?id=${this.paperId}&examId=${this.examId}&examName=${this.examName}&studentId=${this.studentId}&studentName=${this.studentName}`)
          .then(response => {
            if (response.data.code === 0) {
              // console.log(response.data.data)
              this.papers = response.data.data
            } else {
              this.papers = []
            }
          })
          .catch(err => {
            this.papers = []
          })
        }
        
      },
      scanPaper(paper) {
        this.turnScanPaper = true
        this.paperScan = paper
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
  <div class="w-full h-full bg-accent rounded-lg">
    
    <div class="w-full h-full py-2 flex">
      <div class="px-4 py-1 h-full w-[20%] min-w-[300px] border-box relative" style="border-right:1px #ccc solid;">
        <p class="font-bold text-xl mb-3 ">查询试卷</p>
        <span class="btn btn-sm absolute border-none right-4 top-2 font-normal bg-white hover:bg-secondary"
              @click="queryPaper"><svg class="icon" height="16" p-id="3785" t="1713191560562" version="1.1"
                                       viewBox="0 0 1024 1024" width="16" xmlns="http://www.w3.org/2000/svg"><path
            d="M822.496 473.152a32 32 0 0 0-31.392 55.776l97.450667 54.848c20.32 11.434667 45.653333-2.005333 47.594666-25.248 1.674667-20.16 2.517333-35.573333 2.517334-46.528C938.666667 276.362667 747.637333 85.333333 512 85.333333S85.333333 276.362667 85.333333 512s191.029333 426.666667 426.666667 426.666667c144.106667 0 276.053333-72.032 354.752-189.536a32 32 0 1 0-53.173333-35.616C746.645333 813.461333 634.538667 874.666667 512 874.666667c-200.298667 0-362.666667-162.368-362.666667-362.666667s162.368-362.666667 362.666667-362.666667c197.098667 0 357.472 157.226667 362.538667 353.109334l-52.042667-29.290667z"
            fill="#515151" p-id="3786"></path></svg>
          查询/刷新</span>
        <div class="chooser relative pl-2">
          <p class="font-bold text-lg mb-2.5 ">试卷类型
            <text class="ml-1.5 font-normal text-sm rounded bg-info text-white cursor-pointer px-1.5 py-0.5">
              {{ paperType }}
            </text>
          </p>
          <n-radio-group v-model:value="paperType">
            <div class="flex justify-center border-none items-center gap-x-0.5 pl-2 text-base">
              <!--            <n-radio label="全部" value="全部"/>-->
              <n-radio label="答卷" value="答卷"/>
              <n-radio label="底卷" value="底卷"/>
              <n-radio label="答案" value="答案"/>
            </div>
          </n-radio-group>
        </div>
        
        <div v-if="paperType==='底卷'" class="args pl-2 mt-4 relative">
          <p class="font-bold text-lg mb-2 ">查询条件</p>
          <div class="text-[#999] text-xs mt-4 mb-2 pl-2">若未输入条件，将查询对应类型的所有试卷</div>
          <span class="absolute btn btn-sm bg-white border-none hover:bg-secondary right-0 font-normal top-0"
                @click="paperId='';examId='';examName='';studentName='';studentId=''"><svg class="icon" height="16"
                                                                                           p-id="3785" t="1713191560562"
                                                                                           version="1.1"
                                                                                           viewBox="0 0 1024 1024"
                                                                                           width="16"
                                                                                           xmlns="http://www.w3.org/2000/svg"><path
              d="M822.496 473.152a32 32 0 0 0-31.392 55.776l97.450667 54.848c20.32 11.434667 45.653333-2.005333 47.594666-25.248 1.674667-20.16 2.517333-35.573333 2.517334-46.528C938.666667 276.362667 747.637333 85.333333 512 85.333333S85.333333 276.362667 85.333333 512s191.029333 426.666667 426.666667 426.666667c144.106667 0 276.053333-72.032 354.752-189.536a32 32 0 1 0-53.173333-35.616C746.645333 813.461333 634.538667 874.666667 512 874.666667c-200.298667 0-362.666667-162.368-362.666667-362.666667s162.368-362.666667 362.666667-362.666667c197.098667 0 357.472 157.226667 362.538667 353.109334l-52.042667-29.290667z"
              fill="#515151" p-id="3786"></path></svg>重填</span>
          <div class="flex flex-col gap-y-1.5 pl-2 text-sm">
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
          <div class="text-[#999] text-xs mt-4 mb-2 pl-2">若未输入条件，将查询对应类型的所有试卷</div>
          <span class="absolute btn btn-sm bg-white border-none hover:bg-secondary right-0 font-normal top-0"
                @click="paperId='';examId='';examName='';studentName='';studentId=''"><svg class="icon" height="16"
                                                                                           p-id="3785" t="1713191560562"
                                                                                           version="1.1"
                                                                                           viewBox="0 0 1024 1024"
                                                                                           width="16"
                                                                                           xmlns="http://www.w3.org/2000/svg"><path
              d="M822.496 473.152a32 32 0 0 0-31.392 55.776l97.450667 54.848c20.32 11.434667 45.653333-2.005333 47.594666-25.248 1.674667-20.16 2.517333-35.573333 2.517334-46.528C938.666667 276.362667 747.637333 85.333333 512 85.333333S85.333333 276.362667 85.333333 512s191.029333 426.666667 426.666667 426.666667c144.106667 0 276.053333-72.032 354.752-189.536a32 32 0 1 0-53.173333-35.616C746.645333 813.461333 634.538667 874.666667 512 874.666667c-200.298667 0-362.666667-162.368-362.666667-362.666667s162.368-362.666667 362.666667-362.666667c197.098667 0 357.472 157.226667 362.538667 353.109334l-52.042667-29.290667z"
              fill="#515151" p-id="3786"></path></svg>重填</span>
          <div class="flex flex-col gap-y-1.5 pl-2 text-sm">
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
          <div class="text-[#999] text-xs mt-4 mb-2 pl-2">若未输入条件，将查询对应类型的所有试卷</div>
          <span class="absolute btn btn-sm bg-white border-none hover:bg-secondary right-0 font-normal top-0"
                @click="paperId='';examId='';examName='';studentName='';studentId=''"><svg class="icon" height="16"
                                                                                           p-id="3785" t="1713191560562"
                                                                                           version="1.1"
                                                                                           viewBox="0 0 1024 1024"
                                                                                           width="16"
                                                                                           xmlns="http://www.w3.org/2000/svg"><path
              d="M822.496 473.152a32 32 0 0 0-31.392 55.776l97.450667 54.848c20.32 11.434667 45.653333-2.005333 47.594666-25.248 1.674667-20.16 2.517333-35.573333 2.517334-46.528C938.666667 276.362667 747.637333 85.333333 512 85.333333S85.333333 276.362667 85.333333 512s191.029333 426.666667 426.666667 426.666667c144.106667 0 276.053333-72.032 354.752-189.536a32 32 0 1 0-53.173333-35.616C746.645333 813.461333 634.538667 874.666667 512 874.666667c-200.298667 0-362.666667-162.368-362.666667-362.666667s162.368-362.666667 362.666667-362.666667c197.098667 0 357.472 157.226667 362.538667 353.109334l-52.042667-29.290667z"
              fill="#515151" p-id="3786"></path></svg>重填</span>
          <div class="flex flex-col gap-y-1.5 pl-2 text-sm">
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
        
        <p class="text-lg font-bold pl-2 mt-4 mb-3 ">查询结果
          <span v-if="papers.length>0"
                class="text-sm font-normal bg-info text-white rounded ml-1 px-1.5 py-0.5">共{{
              papers.length
            }}条记录</span>
          <span v-if="turnQueryPaper && papers.length===0" class="text-sm font-normal px-1.5 py-0.5  ">
            <span class="text-red-500">未查询到数据...o_O</span>
          </span>
        </p>
        <div class="paper-wrapper p-1 rounded-lg bg-white">
          <NScrollbar style="max-height: 196px; min-height:196px; border:0 #ccc solid;">
            <div v-if="paperType==='答卷'">
              <div v-for="(paper,index) in papers" class="paper-brief ">
                <div :class="{'paper-choosen':paper.id===paperScan.id}"
                     class=" flex gap-y-1 gap-x-3 flex-wrap px-1.5 py-0.5 rounded" @click="scanPaper(paper)">
                  <span class="relative top-0.5 left-0.5 bg-info text-white text-xs"
                        style="width:1rem; height:1rem; cursor:default; border-radius:50%; display:flex;justify-content:center; align-items:center;">{{
                      index + 1
                    }}</span>
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
      
      <div v-if="turnScanPaper && paperType==='底卷'" class="p-2 h-full w-[80%] min-w-[300px]">
        <PaperEmpty :paper="paperScan"/>
      </div>
      <div v-else-if="turnScanPaper && paperType==='答卷'" class="p-2 h-full w-[80%] min-w-[300px]">
        <PaperFilled v-model:paper="paperScan"/>
      </div>
      <div v-else-if="turnScanPaper && paperType==='答案'" class="p-2 h-full w-[80%] min-w-[300px]">
        <PaperRefer :paper="paperScan"/>
      </div>
    
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
    
    .paper-choosen {
      background-color: #05aeec !important;
      color: #fff !important;
    }
  }
  
  input {
    outline: none !important;
  }
</style>
