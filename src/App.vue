<template>
  
  <Toast/>
  
  <div class="drawer">
    
    <div class="drawer-content">
      
      <router-view></router-view>
      
      <!--      <RouterView v-slot="{Component}" >-->
      <!--        <keep-alive exclude="Article,ArticleEditor,UserSignup">-->
      <!--          <component :is="Component"/>-->
      <!--        </keep-alive>-->
      <!--      </RouterView>-->
    
    </div>
    
    <!-- 控制边栏显示的输入域-->
    <input id="my-drawer" v-model="appStore.drawerOn" class="drawer-toggle" type="checkbox"/>
    <!--左侧全局抽屉-->
    <div class="drawer-side" style="z-index: 10000; ">
      <!--与抽屉开关建立链接的label-->
      <label aria-label="close sidebar" class="drawer-overlay" for="my-drawer"></label>
      
      <div class="min-h-full flex flex-col bg-white">
        
        <div class="bg-info pb-4 overflow-hidden"
             style="border-radius: 0 0 80% 2.5rem;" @click="appStore.drawerOn=false">
          
          <ul class="menu p-4 w-56 min-w-56 text-base-content gap-y-3 my-1">
            
            <li :class="{'current':$route.name==='首页'}" @click="appStore.title='首页'">
              <router-link to="/">首页</router-link>
            </li>
            <li :class="{'current':$route.name==='智能阅卷系统'}" @click="appStore.title='智能阅卷系统'">
              <router-link to="/aicheck">阅卷</router-link>
            </li>
            <li :class="{'current':$route.name==='考试管理'}" @click="appStore.title='考试管理'">
              <router-link to="/exam">考试管理</router-link>
            </li>
            <!--            <li :class="{'current':$route.name==='班级管理'}" @click="appStore.title='班级管理'">-->
            <!--              <router-link to="/class">班级管理</router-link>-->
            <!--            </li>-->
            <!--            <li :class="{'current':$route.name==='教师管理'}" @click="appStore.title='教师管理'">-->
            <!--              <router-link to="/teacher">教师管理</router-link>-->
            <!--            </li>-->
            
            <li :class="{'current':$route.name==='学生管理'}" @click="appStore.title='学生管理'">
              <router-link to="/student">学生管理</router-link>
            </li>
            <li :class="{'current':$route.name==='学科管理'}" @click="appStore.title='学科管理'">
              <router-link to="/subject">学科管理</router-link>
            </li>
            <li :class="{'current':$route.name==='历史管理'}" @click="appStore.title='历史管理'">
              <router-link to="/history">历史管理</router-link>
            </li>
          
          </ul>
          
          <div class="divider"></div>
          
          <ul class="menu px-4 pt-4 pb-0 w-56 min-w-56 text-base-content gap-y-2">
            <!--            <li>-->
            <!--              <router-link to="/articledisplay">我的主页</router-link>-->
            <!--            </li>-->
            <!--            <li>-->
            <!--              <router-link to="/articledisplay">作品管理</router-link>-->
            <!--            </li>-->
            
            <li>
              <router-link to="/articledisplay">个人信息</router-link>
            </li>
            <li>
              <router-link to="/articledisplay">联系反馈</router-link>
            </li>
            <li>
              <router-link to="/login">登录页</router-link>
            </li>
            <li>
              <router-link to="/articledisplay">设置</router-link>
            </li>
          
          </ul>
        </div>
        
        <!--<div class="divider"></div>-->
        
        <!--底部区域-->
        <div class="absolute p-4 w-56 m-w-56 flex flex-col gap-y-3 mb-3">
          <div
              class="cursor-pointer flex flex-row justify-between px-3 py-2 gap-x-2 rounded-lg bg-[#f3f3f3] hover:bg-secondary text-base">
            <span class="flex justify-between items-center gap-x-1">
              <div class="info-dot bg-green-500"></div>
              <div class="info-dot bg-red-500"></div>
              <div class="info-dot bg-blue-500"></div>
            </span>
            <span>126未读</span>
            <span class="text-[#ef4444] ">今日新增</span>
          </div>
          <div
              class="cursor-pointer flex flex-row items-center pl-4 pr-4 py-1.5 gap-x-1 rounded-lg bg-[#f3f3f3] hover:bg-secondary text-base">
            <span style="min-width:50px;">
              <img alt="" src="/icon2.jpg" style="width: 40px; height:40px; border-radius:50%;">
            </span>
            <span class="text-center text-lg font-bold " style=" white-space: nowrap; width: calc(10rem - 40px); max-width: calc(10rem - 40px); text-overflow: ellipsis;
overflow: hidden;"
                  title="1001">管理员 1001</span>
          </div>
        </div>
      </div>
    
    </div>
  </div>

</template>

<script>
  import AppTopbar from "@/components/layout/AppTopbar.vue";
  import useAppStore from "@/stores/useAppStore.js";
  
  export default {
    components: {AppTopbar},
    data() {
      return {
        appStore: useAppStore(),
        
      }
    },
    methods: {},
    
  }
</script>

<style lang="scss" scoped>
  
  
  /* 覆盖，左侧抽屉的动画时长 */
  .drawer-side > *:not(.drawer-overlay) {
    transition-duration: 0.4s;
  }
  
  .drawer-side {
    
    .current {
      background-color: #fff !important;
      color: #000 !important;
    }
    
    
    .menu:first-child {
      li {
        border-radius: 0.5rem;
        transition: all 1s linear;
        color: #fff;
        font-weight: bold;
        font-size: 1rem;
      }
      
      li:hover {
        color: #000;
        background-color: #bcf8ae;
        border-radius: 0.5rem;
        transition: all 0.4s linear;
      }
    }
    
    .menu:last-child li {
      border-radius: 0.5rem;
      width: fit-content;
      font-weight: normal;
      font-size: 0.85rem;
      color: #fff;
    }
    
    .menu:last-child li:hover {
      color: #000;
      background-color: #bcf8ae;
    }
    
    .divider {
      width: 100%;
      height: 2px;
      max-height: 2px;
      border: 1px solid #fafafa;
      margin: 0;
    }
    
    .absolute {
      position: absolute;
      bottom: 0;
      
      .info-dot {
        border-radius: 50%;
        width: 10px;
        height: 10px;
      }
    }
  }
</style>
