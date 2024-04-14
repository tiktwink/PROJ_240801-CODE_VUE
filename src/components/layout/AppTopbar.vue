<script>
  import useAppStore from "@/stores/useAppStore.js";
  import AppTopbarControl from "@/components/layout/utility/AppTopbarControl.vue";
  
  
  export default {
    props: {
      mode: {
        type: String,
        default: 'fixed' //可选'popup'弹窗模式，mode=='popup'时，要搭配hiddenEl一起使用
      },
      hiddenEl: {
        type: String,
        default: 'app' //mode为'popup'时，鼠标移动到id值为hiddenEl元素(默认'app')区域时，令AppTopbar隐藏
      },
      bgColor: { //背景色
        type: String,
        default: 'bg-base-100'
      }
    },
    
    components: {
      AppTopbarControl,
    },
    
    data() {
      return {
        appStore: useAppStore(),
        isDark: document.getElementById('app').classList.contains('dark'),
        // appStore:useAppStore(), //控制边栏显示，边栏的开关在顶栏中，而边栏在APPLayout中，顶栏AppTopbar和APPLayout之间用仓库通信
      }
    },
    
    methods: {
      toggleDarkMode() {
        this.$toast.add({
          severity: 'info',
          summary: '深色模式暂未完全适配！',
          life: 1500
        })
        
        if (document.getElementById('app').classList.contains('dark')) {
          console.log('change to light mode!')
          this.isDark = false
          //切换浅色模式
          document.getElementById('app').classList.remove('dark')
          //切换当前主题对应的浅色模式
          let theme = document.getElementById('app').getAttribute('data-theme')
          document.getElementById('app').setAttribute('data-theme', theme.slice(0, theme.lastIndexOf('dark')))
          this.appStore.selectedDark = false
        } else {
          console.log('change to dark mode!')
          this.isDark = true
          //切换深色模式
          document.getElementById('app').classList.add('dark')
          //切换当前主题对应的深色模式
          let theme = document.getElementById('app').getAttribute('data-theme')
          document.getElementById('app').setAttribute('data-theme', theme.concat('dark'))
          this.appStore.selectedDark = true
        }
      }
    },
    
    mounted() {
      this.$nextTick(() => {
        //应用上次的深浅模式
        let app = document.getElementById('app')
        if (this.appStore.selectedDark) {
          app.setAttribute('data-theme', 'mythemedark')
          app.classList.add('dark')
        } else {
          if (app.classList.contains('dark')) {
            app.classList.remove('dark')
          }
          app.setAttribute('data-theme', 'mytheme')
        }
        
        //popup弹窗模式
        document.getElementById('apptopbarcontrol').style.display = 'none' //默认fixed模式
        if (this.mode === 'popup') {
          document.getElementById('apptopbarcontrol').style.display = 'block' //激活popup模式
          let apptopbar = document.getElementById('apptopbar')
          apptopbar.style.height = '80px'
          apptopbar.style.borderBottom = '1px #333 dashed'
          apptopbar.style.borderRadius = '0 0 20px 20px'
          apptopbar.style.position = 'fixed'
          apptopbar.style.top = '-85px'
          //mode为'popup'时，鼠标移动到id值为hiddenEl元素(默认'app')区域时，令AppTopbar隐藏
          const handleMouseMove = () => {
            apptopbar.style.top = '-85px'
          }
          document.getElementById(this.hiddenEl).addEventListener("mousemove", handleMouseMove)
        }
      })
    },
    
  }
</script>

<template>
  <AppTopbarControl :class="bgColor"/>
  
  <div id="apptopbar" :class="bgColor" class="navbar flex px-2 dark:bg-base-100"
       style="height:80px; z-index:998">
    
    <div class="flex-none mx-2">
      <button class="btn btn-square btn-ghost" @click="appStore.drawerOn=true">
        <svg class="inline-block w-5 h-5 stroke-current" fill="none" viewBox="0 0 24 24"
             xmlns="http://www.w3.org/2000/svg">
          <path d="M4 6h16M4 12h16M4 18h16" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"></path>
        </svg>
      </button>
    </div>
    
    <div class="flex-none mr-2 ">
      <span class="btn btn-ghost text-xl px-2" @click="$router.push('/')">
<svg class="icon" height="28" p-id="8478" t="1710086472633" version="1.1" viewBox="0 0 1024 1024"
     width="28" xmlns="http://www.w3.org/2000/svg"><path
    d="M549.61981 133.022476l319.683047 203.605334A70.851048 70.851048 0 0 1 902.095238 396.361143v434.883047A70.89981 70.89981 0 0 1 831.146667 902.095238h-282.819048l0.024381-218.112h-71.826286v218.087619L192.853333 902.095238A70.89981 70.89981 0 0 1 121.904762 831.24419V390.241524c0-24.527238 12.678095-47.299048 33.54819-60.220953l318.659048-197.485714a70.972952 70.972952 0 0 1 75.50781 0.487619zM828.952381 828.952381V397.214476L511.488 195.047619 195.047619 391.119238V828.952381h211.309714v-216.551619h212.187429v216.527238L828.952381 828.952381z"
    fill="#2c2c2c" p-id="8479"></path></svg>
      
      </span>
    </div>
    
    <div class="flex-none dropdown dropdown-hover dropdown-bottom ml-1 mr-4">
      <div class="btn btn-ghost btn-circle avatar" role="button" tabindex="0">
        <div class="w-10 rounded-full">
          <img alt="" src="/icon2.jpg">
          <!--            <img alt="Tailwind CSS Navbar component"-->
          <!--                 src="https://daisyui.com/images/stock/photo-1534528741775-53994a69daeb.jpg"/>-->
        </div>
      </div>
      
      <ul class="dropdown-content mt-5 z-[10] p-2 shadow menu menu-sm bg-base-100 rounded-box w-24 -top-4" tabindex="0">
        <li>
          <a class="justify-between ">
            个人信息
            <!--            <span class="badge">New</span>-->
          </a>
        </li>
        <li><a>设置</a></li>
        <li><a>登出</a></li>
      </ul>
    </div>
    <!--搜索框-->
    <div class="flex-1 gap-1 ml-2 ">
      <input class=" input input-bordered rounded-xl w-24 h-10 md:w-auto " placeholder="搜索..." type="text"/>
    </div>
    
    <!--顶栏中部-->
    <div class="relative navbar-center bg-[#f5f5f5] rounded-xl mx-4 py-1 flex-1 font-bold  text-2xl"
         style=" display: flex; justify-content: center;">
      <div style="position:absolute; left:1rem;">
        <span class="cursor-pointer mr-4 tooltip tooltip-secondary tooltip-right flex items-center"
              data-tip="返回上一页" @click="$router.go(-1)">
      <svg class="icon" height="24" p-id="8746" t="1710095156499" version="1.1"
           viewBox="0 0 1024 1024" width="24" xmlns="http://www.w3.org/2000/svg"><path
          d="M482.962286 492.032l-51.687619 51.736381-196.559238-196.437333 196.510476-196.608 51.736381 51.687619-99.230476 99.279238h223.329523c140.434286 0 254.659048 111.957333 258.364953 251.465143l0.097524 6.997333c0 142.726095-115.712 258.438095-258.438096 258.438095H154.819048v-73.142857H607.085714a185.295238 185.295238 0 1 0 0-370.590476l-241.371428-0.024381 117.248 117.199238z"
          p-id="8747"></path>
      </svg>
      
      </span>
      </div>
      
      
      当前 : {{ $route.name }}
    </div>
    
    <!--顶栏右侧-->
    <div class="navbar-end flex-1">
      <div v-if="mode==='popup'"
           class="btn btn-sm text-white hover:text-black font-normal bg-info hover:bg-secondary border-none mr-1"
           @click="appStore.apptopbarControl=(appStore.apptopbarControl+1)%3 ">控制条
      </div>
      
      <button class="btn btn-ghost btn-circle mx-1" @click="toggleDarkMode">
        <div class="indicator">
          <svg v-if="!isDark" class="icon" height="32" p-id="41505" t="1704981820704"
               version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg">
            <path
                d="M482.669714 192v-128h58.660572v128h-58.660572zM482.669714 960v-128h58.660572v128h-58.660572zM264.97219 306.468571L174.470095 215.966476l41.496381-41.496381 90.502095 90.502095-41.496381 41.496381zM808.033524 849.529905L717.531429 758.979048l41.472-41.472 90.526476 90.502095-41.496381 41.496381zM808.033524 174.470095l-90.502095 90.502095 41.472 41.496381 90.526476-90.502095-41.496381-41.496381zM264.97219 717.531429l-90.502095 90.502095 41.496381 41.496381 90.502095-90.526476-41.496381-41.472zM832 482.669714h128v58.660572h-128v-58.660572zM64 482.669714h128v58.660572h-128v-58.660572zM789.333333 512c0 153.161143-124.17219 277.333333-277.333333 277.333333S234.666667 665.161143 234.666667 512 358.838857 234.666667 512 234.666667 789.333333 358.838857 789.333333 512z m-58.660571 0a218.672762 218.672762 0 1 0-437.345524 0 218.672762 218.672762 0 0 0 437.345524 0z"
                fill="#2c2c2c" p-id="41506"></path>
          </svg>
          <svg v-else class="icon" height="32" p-id="43324" t="1704982338718"
               version="1.1" viewBox="0 0 1024 1024" width="32" xmlns="http://www.w3.org/2000/svg">
            <path
                d="M725.333333 533.333333a234.666667 234.666667 0 0 1-234.666666-234.666666 233.569524 233.569524 0 0 1 41.155047-132.778667 236.007619 236.007619 0 0 1 50.346667-53.150476 407.893333 407.893333 0 0 0-78.677333-5.973334C283.574857 111.274667 106.666667 290.986667 106.666667 512c0 223.865905 181.467429 405.333333 405.333333 405.333333 221.013333 0 400.725333-176.88381 405.23581-396.824381a411.452952 411.452952 0 0 0-5.948953-78.677333 236.007619 236.007619 0 0 1-53.174857 50.346667 233.569524 233.569524 0 0 1-132.778667 41.155047z m0 58.660572c46.592 0 90.672762-10.873905 129.77981-30.208-24.137143 167.862857-168.569905 296.886857-343.113143 296.886857-191.463619 0-346.672762-155.209143-346.672762-346.672762 0-174.567619 129.024-318.976 296.886857-343.113143a292.132571 292.132571 0 0 0-30.232381 129.77981c0 162.011429 131.34019 293.327238 293.351619 293.327238z"
                fill="#e6e6e6" p-id="43325"></path>
            <path
                d="M797.330286 128v98.669714h98.669714v58.660572h-98.669714v98.669714h-58.660572v-98.669714H640V226.669714h98.669714V128h58.660572z"
                fill="#e6e6e6" p-id="43326"></path>
          </svg>
        
        
        </div>
      </button>
      
      <button class="btn btn-ghost btn-circle mx-1">
        <div class="indicator">
          <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path
                d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
                stroke-linecap="round" stroke-linejoin="round"
                stroke-width="2"/>
          </svg>
          <span class="badge badge-xs badge-primary indicator-item"></span>
        </div>
      </button>
      
      <!--      <button class="btn btn-square btn-ghost mx-1" @click="appStore.sideOn=!appStore.sideOn">-->
      <!--        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"-->
      <!--             class="inline-block w-5 h-5 stroke-current">-->
      <!--          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>-->
      <!--        </svg>-->
      <!--      </button>-->
      
      <div class="btn btn-ghost">
        <span
            class="px-0">{{ new Date().getFullYear() }}年{{ new Date().getMonth() + 1 }}月{{
            new Date().getDate()
          }}日</span>
        
        <!--        <span v-if="new Date().getDay()!==7">星期{{new Date().getDay()}}</span>-->
        <!--        <span v-else>星期日</span>-->
      </div>
    </div>
  </div>

</template>

<style lang="scss" scoped>
  .dropdown-bottom .dropdown-content {
    top: 1.5rem;
  }
  
  input {
    outline: none !important;
  }
</style>
