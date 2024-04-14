<template>
  <div
      :class="{ 'wide-with-side':!narrowScreen && appStore.sideOn, 'narrow-with-side':narrowScreen && appStore.sideOn }"
      class="test-wrapper mb-2"
      style="max-height:100vh; ">
    <!-- appStore.apptopbarOn控制顶栏显示-->
    <AppTopbar :style="{height:'80px','z-index':'990'}"></AppTopbar>
    <div id="applayout_main"
         class="test-main-wrapper bg-white dark:bg-[#2a2a2a] flex flex-col items-center justify-between rounded-lg"
         style="height:calc(100vh - 80px - 1rem); overflow:hidden;">
      
      <router-view/>
    </div>
    
    
    <!--右侧栏-->
    <div id="rside" class=" bg-[#f5f5f5] dark:bg-[#2a2a2a] test-rside fixed right-0 rounded-lg"
         style="min-height:calc(100vh - 80px - 1rem);">
      <!--      <div class="test-rside-btn absolute">-->
      <!--        <div class="btn" @click.prevent.stop="console.log('123')">click</div>-->
      <!--      </div>-->
      <div v-if="$route.name==='学科管理'">
        <SubjectRside/>
      </div>
      <div v-else-if="$route.name==='考试管理'">
        <ExamRside/>
      </div>
      <div v-else-if="$route.name==='评阅结果'">
        <AiCheckOutRside/>
      </div>
    
    </div>
    <div class="test-mask" @click="handleMaskTap"></div>
  </div>
</template>

<script>
  import useAppStore from "@/stores/useAppStore.js";
  import AppTopbar from "@/components/layout/AppTopbar.vue";
  import SubjectRside from "@/components/subject/SubjectRside.vue"
  import ExamRside from "@/components/exam/ExamRside.vue";
  import AiCheckOutRside from "@/components/aicheck/AiCheckOutRside.vue";
  
  export default {
    
    components: {AiCheckOutRside, AppTopbar, SubjectRside, ExamRside},
    data() {
      return {
        appStore: useAppStore(), //控制边栏显示，边栏的开关在顶栏中，而边栏在APPLayout中，顶栏AppTopbar和APPLayout之间用仓库通信
        narrowScreen: useAppStore().narrowScreen, //是否窄屏，只在APP启动时接收一次，后续默认不变（可进一步优化）
      }
    },
    mounted() {
      console.log(this.$route.name)
      
      //右侧栏添加事件监听
      document.getElementById('rside').addEventListener('click', () => {
        // console.log('rside-button clicked')
        this.appStore.sideOn = !this.appStore.sideOn
      })
    },
    methods: {
      handleMaskTap() {
        this.appStore.toggleSideOn()
      },
      
      
    },
    
    
  }
</script>

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
        
        .test-rside::before {
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
        }
      }
      
      .test-main-wrapper {
        margin-right: 20px;
      }
      
      .test-rside {
        /*transform : translateX(100%);*/
        right: -323px;
        transition: right 0.5s;
      }
      
      .test-rside::before {
        content: url("../../assets/left-btn.svg");
        color: red;
        position: absolute;
        top: 45%;
        right: calc(100% + 15px);
        font-size: 2rem;
        cursor: pointer;
      }
      
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
