<script>
  import useAppStore from "@/stores/useAppStore.js";
  import AppTopbar from "@/components/layout/AppTopbar.vue";
  import SubjectRside from "@/components/subject/SubjectRside.vue"
  import ExamRside from "@/components/exam/ExamRside.vue";
  import AiCheckOutRside from "@/components/aicheck/AiCheckOutRside.vue";
  import StudentRside from "@/components/student/StudentRside.vue";
  import PaperUpload from "@/components/aicheck/PaperUpload.vue";
  import usePaperStore from "@/stores/usePaperStore.js";
  import PaperRside from "@/components/paper/PaperRside.vue";
  
  export default {
    
    components: {PaperUpload, PaperRside, StudentRside, AiCheckOutRside, AppTopbar, SubjectRside, ExamRside},
    data() {
      return {
        appStore: useAppStore(), //控制边栏显示，边栏的开关在顶栏中，而边栏在APPLayout中，顶栏AppTopbar和APPLayout之间用仓库通信
        paperStore: usePaperStore(),
        narrowScreen: useAppStore().narrowScreen, //是否窄屏，只在APP启动时接收一次，后续默认不变（可进一步优化）
      }
    },
    mounted() {
      console.log(this.$route.name)
      /*//右侧栏添加事件监听
      document.getElementById('rside').addEventListener('click', () => {
        // console.log('rside-button clicked')
        this.appStore.sideOn = !this.appStore.sideOn
      })*/
    },
    methods: {
      handleMaskTap() {
        this.appStore.toggleSideOn()
      },
      
      
    },
  }
</script>

<template>
  <div
      :class="{ 'wide-with-side':!narrowScreen && appStore.sideOn, 'narrow-with-side':narrowScreen && appStore.sideOn }"
      class="test-wrapper mb-2 relative"
      style="max-height:100vh; ">
    <!-- appStore.apptopbarOn控制顶栏显示-->
    <AppTopbar :style="{height:'80px','z-index':'990'}"></AppTopbar>
    <!--主区-->
    <div id="applayout_main"
         class="test-main-wrapper bg-white dark:bg-[#2a2a2a] flex flex-col items-center justify-between rounded-lg"
         style="height:calc(100vh - 80px - 1rem); overflow:hidden;">
      
      <!--配置需要缓存的路由-->
      <router-view v-slot="{ Component }">
        <keep-alive include="Paper">
          <component :is="Component"/>
        </keep-alive>
      </router-view>
    </div>
    
    <!--右侧栏收起时的打开开关-->
    <span v-if="appStore.sideOn===false" class="absolute top-[54.5%] right-[-5px] cursor-pointer"
          @click="appStore.sideOn=true">
      <svg class="icon" height="28" p-id="9235" t="1713353708460" version="1.1"
           viewBox="0 0 1024 1024" width="28" xmlns="http://www.w3.org/2000/svg"><path
          d="M754.346667 193.584762l51.687619 51.712-266.703238 266.581333 266.727619 266.849524-51.736381 51.687619-318.415238-318.561524L754.322286 193.584762z m-268.190477 0l51.68762 51.712-266.703239 266.581333 266.727619 266.849524-51.73638 51.687619L167.740952 511.878095 486.13181 193.584762z"
          fill="#05aefc" p-id="9236"></path></svg>
    </span>
    <!--右侧栏-->
    <div id="rside" class=" bg-[#f5f5f5] dark:bg-[#2a2a2a] test-rside fixed right-0 rounded-lg"
         style="min-height:calc(100vh - 80px - 1rem);">
      <!--右侧栏展开时的关闭开关-->
      <div class="btn absolute top-[47%] left-[-15px] py-0 pr-0 pl-2" @click.prevent.stop="appStore.sideOn=false">
        <svg height="28" p-id="9505" t="1713353876465" version="1.1" viewBox="0 0 1024 1024"
             width="28" xmlns="http://www.w3.org/2000/svg">
          <path
              d="M536.380952 193.584762l318.415238 318.268952L536.380952 830.415238l-51.712-51.687619 266.727619-266.873905-266.727619-266.581333L536.380952 193.584762z m-268.190476 0l318.415238 318.268952L268.190476 830.415238 216.502857 778.727619l266.727619-266.873905L216.454095 245.272381 268.190476 193.584762z"
              fill="#05aefc" p-id="9506"></path>
        </svg>
      </div>
      
      <div v-if="$route.name==='学科管理'">
        <SubjectRside/>
      </div>
      
      <div v-else-if="$route.name==='考试管理'">
        <ExamRside/>
      </div>
      
      <div v-else-if="$route.name==='评阅结果'">
        <AiCheckOutRside/>
      </div>
      
      <div v-else-if="$route.name==='学生管理'">
        <StudentRside/>
      </div>
      
      <div v-else-if="$route.name==='试卷管理'">
        <div v-if="appStore.sidePart==='paperfilledupload'&&paperStore.reImportOn"
             class="py-2 px-2">
          <div>
            <p class="mt-1 ml-2 mb-3">
              <text class="font-bold text-lg ">要上传的答卷ID：</text>
              <text>{{ appStore.sidePaperId === '' ? '-' : appStore.sidePaperId }}</text>
            </p>
          </div>
          <PaperUpload :paper-id="appStore.sidePaperId"/>
        </div>
        <div v-else-if="appStore.sidePart==='paperreferupload'&&paperStore.reImportOn"
             class="py-2 px-2">
          <div>
            <p class="mt-1 ml-2 mb-3">
              <text class="font-bold text-lg ">要上传的答案所属考试ID：</text>
              <text>{{ appStore.sidePaperId === '' ? '-' : appStore.sidePaperId }}</text>
            </p>
          </div>
          <PaperUpload :paper-id="appStore.sidePaperId"/>
        </div>
        <div v-else-if="appStore.sidePart==='paperemptyupload'&&paperStore.reImportOn"
             class="py-2 px-2">
          <div>
            <p class="mt-1 ml-2 mb-3">
              <text class="font-bold text-lg ">要上传的底卷所属考试ID：</text>
              <text>{{ appStore.sidePaperId === '' ? '-' : appStore.sidePaperId }}</text>
            </p>
          </div>
          <PaperUpload :paper-id="appStore.sidePaperId"/>
        </div>
        
        <PaperRside/>
      </div>
    
    
    </div>
    
    <!--蒙层(窄屏模式)-->
    <div class="test-mask" @click="handleMaskTap"></div>
  </div>
</template>

<style lang="scss" scoped>
  
  .test-wrapper {
    min-height: 50vh; /*optional, none-required*/
    position: relative;
    
    .test-main-wrapper {
      
      margin-left: 20px;
      margin-right: 80px;
      margin-bottom: 0 !important; /*❗*/
      min-height: 600px;
      //border: 1px #ccc solid;
      //box-shadow: -3px -3px 2px #ccc;
      transition: margin-right 0.5s;
      
      .test-main {
        margin: 1rem;
        padding: 1rem;
      }
    }
    
    .test-rside {
      position: absolute;
      top: 80px;
      right: 0;
      //border: 1px #ccc solid;
      //background-color: #fff;
      width: 290px;
      
      //pointer-events: none;  /*让右侧栏点击事件失效*/
    }
    
    .test-rside::before {
      pointer-events: all; /*让右侧栏点击事件由伪元素接管*/
    }
    
    .test-mask {
      background-color: rgba(189, 186, 186, 0.7);
      
      position: fixed;
      top: 0;
      left: 0;
    }
  }
  
  @media screen and (min-width: 992px) {
    .test-wrapper {
      &.wide-with-side {
        .test-main-wrapper {
          margin-right: 330px;
        }
        
        .test-rside {
          right: 20px;
          transition: right 0.8s;
          overflow: hidden;
        }
        
        /* .test-rside::before {
           content: url("../../assets/right-btn-white.svg");
           position: absolute;
           top: 45%;
           right: calc(100% - 33px);
           font-size: 2rem;
           z-index: 999;
           cursor: pointer;
           //background-color: #f5f5f5;
           //padding:0;
           //border-radius:50%;
           //border:1px #f00 solid;
           //width:60px;
           //height:60px;
           //display:flex;
           //justify-content:center;
           //align-items: center;
         }*/
      }
      
      .test-main-wrapper {
        margin-right: 20px;
      }
      
      .test-rside {
        /*transform : translateX(100%);*/
        right: -323px;
        transition: right 0.5s;
      }
      
      /*.test-rside::before {
        content: url("../../assets/left-btn.svg");
        position: absolute;
        top: 45%;
        right: calc(100% + 15px);
        font-size: 2rem;
        cursor: pointer;
      }*/
      
      .test-mask {
        display: none !important;
      }
    }
  }
  
  @media screen and (max-width: 991px) {
    .test-wrapper {
      &.narrow-with-side {
        .test-rside {
          transform: translateX(0);
          z-index: 999;
        }
        
        .test-mask {
          width: 100vw !important;
          height: 100vh !important;
          z-index: 998 !important;
        }
        
      }
      
      .test-rside {
        transform: translateX(100%);
      }
      
    }
    
  }
</style>
