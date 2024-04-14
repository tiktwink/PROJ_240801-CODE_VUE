<script>
  import axios from "axios";
  import useUserStore from "@/stores/useUserStore.js";
  import md5 from "js-md5";
  import useAppStore from "@/stores/useAppStore.js";
  
  export default {
    name: 'UserLogin',
    data() {
      return {
        userStore: useUserStore(),
        checked: false, //勾选协议
        unchecked_login: false, //“未勾选协议且拟登录”状态
        username: '',
        password: ''
      }
    },
    watch: {
      username(newValue, oldValue) {
        if (newValue.trim() !== '') {
          let unlabel = document.getElementById('unlabel')
          unlabel.style.transform = 'translateY(-30px) translateX(10px) scale(0.75)'
          unlabel.style.color = '#ffffff'
          
          document.getElementById('uncut').style.backgroundColor = '#05aefc'
          document.getElementById('uncut').style.transform = 'translateY(8px)'
        } else {
          console.log('usename null')
          let unlabel = document.getElementById('unlabel')
          unlabel.style.transform = ''
          unlabel.style.color = '#666'
          
          document.getElementById('uncut').style.backgroundColor = ''
          document.getElementById('uncut').style.transform = ''
        }
      },
      password(newValue, oldValue) {
        if (newValue.trim() !== '') {
          let pwlabel = document.getElementById('pwlabel')
          pwlabel.style.transform = 'translateY(-30px) translateX(10px) scale(0.75)'
          pwlabel.style.color = '#ffffff'
          
          document.getElementById('pwcut').style.backgroundColor = '#05aefc'
          document.getElementById('pwcut').style.transform = 'translateY(8px)'
        } else {
          let pwlabel = document.getElementById('pwlabel')
          pwlabel.style.transform = ''
          pwlabel.style.color = '#666'
          
          document.getElementById('pwcut').style.backgroundColor = ''
          document.getElementById('pwcut').style.transform = ''
        }
      }
    },
    mounted() {
      let login_card = document.getElementById('login_card')
      login_card.style.position = 'relative'
      login_card.style.transform = 'translateX(-500px)'
      login_card.style.transition = 'all 1.25s'
      
      setTimeout(() => {
        login_card.style.transform = 'translateX(0)'
      }, 150)
      
    },
    methods: {
      shake(elemId) {
        let elem = document.getElementById(elemId)
        if (elem) {
          elem.classList.add('shake')
          
          setTimeout(() => {
            elem.classList.remove('shake')
          }, 800)
        }
      },
      toLogin() {
        if (!this.checked) {
          //如果未勾选协议，而此时已经点击了登录，说明用户处于“未勾选协议且拟登录”状态，将该字段设为true
          this.unchecked_login = true
          //用户处于“未勾选协议且拟登录”状态时，显示并振动提醒文字
          this.shake('xieyi1')
          setTimeout(() => {
            //振动提醒文字800ms后，继续显示500ms，再将提醒文字隐藏（一共是1300ms）
            this.unchecked_login = false
          }, 1300)
          return
        }
        
        //准备登录
        axios.post('/api/user/login', {
          username: this.username,
          password: md5(this.password),
        })
        .then(res => {
          console.log('ur token: ')
          console.log(res.data)
          if (res.data.code === 0) {
            console.log('登录成功')
            this.userStore.token = res.data.data.tokenValue
            this.userStore.tokenValidDt = res.data.data.validDT
            this.userStore.username = this.username
            
            useAppStore().title = '首页'
            this.$router.push('/')
            this.$toast.add({
              severity: 'success',
              summary: '登录成功！',
              life: 3000,
            })
          }
        })
        .catch(err => {
          console.log(err)
        })
        
      },
      toSignin() {
        // const res = confirm('注册需要邀请码')
        // if(res){
        //   this.$router.push('/signup')
        // }
        this.$router.push('/signup')
      },
      test() {
        
        axios.get('/api/user/test', {
          headers: {
            token: this.userStore.token,
            username: this.userStore.username
          }
        })
        .then(res => {
          console.log(res)
        })
        .catch(err => {
          console.log(err)
        })
      }
      
    },
  }
</script>


<template>
  <div class="userlogin-root">
    <div class="absolute-text"></div>
    <div class="absolute-bg"></div>
    <div class="absolute-footer flex justify-center items-center text-base">AI智能阅卷平台</div>
    
    <div class="wide-bg relative">
      
      <div class="login-left flex flex-col items-center justify-center px-1">
        <div class="nav flex items-center justify-between ">
          <div class="cursor-pointer" style="color:#05aefc" @click="test">订阅</div>
          <div class="cursor-pointer">简介</div>
          <div class="cursor-pointer">指南</div>
          <div class="cursor-pointer">链接</div>
          <div class="cursor-pointer">反馈</div>
          <div class="hover-from cursor-pointer">开发团队
            <div class="inner">
              <img alt="" src="" style="width:50px; height:50px; border-radius: 25px;"></div>
          </div>
          <div class="hover-text">
            <div class="cursor-pointer" title="网站、后端">Y. NP</div>
            <div class="cursor-pointer" title="AI">Z. L</div>
            <div class="cursor-pointer" title="AI">S. ZA</div>
            <div class="cursor-pointer" title="AI">W. LL</div>
            <div class="cursor-pointer" title="后端、文档">Z. CW</div>
          </div>
        
        </div>
        
        <div id="login_card" class="card flex flex-col justify-center items-center gap-y-1 px-2 md:px-3 lg:px-4 xl:px-5"
             style="max-width: 500px; min-width:280px; border-radius:25px;">
          
          <div class="welcome flex flex-col items-start px-4 text-white" style="width:100%;">
            <div class="font-bold text-5xl">Hello<span class="font-bold text-xl">，welcome to</span></div>
            <div class="font-bold text-xl">一 言 阅 卷</div>
          </div>
          
          <div class="username-password">
            <div class="input-container ic1">
              <input id="uninput" v-model="username" class="input border-none" placeholder="" type="text">
              <div id="uncut" class="cut" style="min-width:56px;"></div>
              <label id="unlabel" class="iLabel" for="uninput">用户名</label>
            </div>
            <div class="input-container ic2">
              <input id="pwinput" v-model="password" class="input" placeholder="" type="password">
              <div id="pwcut" class="cut" style="min-width:44px;"></div>
              <label id="pwlabel" class="iLabel" for="pwinput">密码</label>
            </div>
          </div>
          
          <div class="card-above flex justify-between items-center px-5 text-base font-bold"
               style="width: 100%; color:#fff;">
            <div class="forget cursor-pointer">忘记密码</div>
            <div class="forget cursor-pointer">用户协议</div>
          </div>
          
          <div class="card-bottom flex flex-col gap-y-2 mt-1 w-[90%]" style="position: relative; ">
            
            <div class="bottom-link flex items-center">
              <div v-show="unchecked_login" id="xieyi1" class="text-sm">请确认是否同意遵守协议</div>
              <div class="flex items-center">
                
                <input id="xieyi" v-model="checked" class="toggle toggle-xs toggle-info mr-1.5" type="checkbox"/>
                <label for="xieyi" style="font-size: 0.7rem; ">我已阅读并同意《用户协议》</label>
              
              </div>
            </div>
            
            <div class="bottom-action w-full flex justify-between items-center mt-2 ">
              <div class="bg-[#05aefc] hover:bg-secondary text-white hover:text-black mr-4 btn btn-sm border-none "
                   style="min-width:85px;" @click="toLogin">
                登录
              </div>
              <div class="bg-[#05aefc] hover:bg-secondary text-white hover:text-black btn btn-sm border-none "
                   style="min-width:85px;" @click="toSignin">
                注册
              </div>
            
            </div>
          
          </div>
        
        </div>
      
      </div>
      
      <div class="login-right">
        <div class="fixed top-12 right-20"
             style="font-size: 1.25rem; font-weight: normal; font-weight: bold; margin-bottom:1rem; color:#fff;">
          这是一个
          <span
              style="margin:0 5px 0 1px;padding:0.1rem 0.25rem 0.1rem 0.4rem;text-align:center;color: #000;font-size: 1.2rem;  font-weight: bold; border-radius:0.5rem;background-color:#fff;">
            测试
          </span>
          程序，内容仅作展示
        </div>
        <p class="line1">AI智能阅卷平台</p>
        <p class="line2">基于百度文心大模型</p>
        <p class="line3" style="font-size: 2rem;">智能阅卷，考试管理，学情追踪</p>
        <p class="line4">更多精彩探索...</p> <!--style="color:#05aefc;"-->
      </div>
    
    </div>
  
  </div>
</template>


<style lang="scss" scoped>
  
  .userlogin-root {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100vh;
    background-color: #fff;
    background-image: url("../../assets/img/wide-bg.png");
    background-repeat: no-repeat;
    background-size: cover;
    background-position-x: center;
    background-position-y: center;
    
    @media screen and (orientation: portrait) {
      //background-image: url("../assets/img/haibao1.png");
      background-repeat: no-repeat;
      background-size: cover;
    }
    
    .absolute-footer {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 60%;
      height: 32px;
      //font-weight: bold;
      color: #aaa;
    }
    
    //.absolute-bg {
    //  position: absolute;
    //  width: 55%;
    //  height: 100%;
    //  left: 45%;
    //  border-radius: 50% 0 0 50%;
    //  background-color: #05aefc;
    //  z-index: 0;
    //}
    
    
    .wide-bg {
      display: flex;
      align-items: center;
      justify-content: space-between;
      
      @media screen and (orientation: portrait) {
        display: none;
      }
      
      @media screen and (max-width: 1000px) {
        
        padding: 2rem;
        justify-content: center;
      }
      @media screen and (min-width: 1000px) {
        padding-left: 0;
        padding-right: 0.75rem;
      }
      @media screen and (min-width: 1100px) {
        padding-left: 0.5rem;
        padding-right: 1.25rem;
      }
      @media screen and (min-width: 1200px) {
        padding-left: 1.25rem;
        padding-right: 1.75rem;
      }
      @media screen and (min-width: 1400px) {
        padding-left: 2.5rem;
        padding-right: 2.5rem;
      }
      @media screen and (min-width: 1600px) {
        padding-left: 4rem;
        padding-right: 4rem;
      }
      @media screen and (min-width: 1800px) {
        padding-left: 5.25rem;
        padding-right: 5.25rem;
      }
      @media screen and (min-width: 2100px) {
        padding-left: 7rem;
        padding-right: 7rem;
      }
      
      width: 85%;
      height: 100%;
      position: relative;
      border-radius: 30px;
      //box-shadow: 0 0 5px 5px rgba(255, 250, 242, 0.85);
      //background-image: url('../assets/img/haibao1.png');
      //background-repeat: no-repeat;
      //background-size: cover;
    }
  }
  
  
  .login-left {
    height: 100%;
    
    .nav {
      position: absolute;
      width: 65%;
      @media screen and (orientation: portrait) {
        width: 90%;
        margin-left: 20px;
      }
      height: 60px;
      top: 0;
      left: -50px;
      font-size: 1.25rem;
      font-weight: bold;
      color: #333333;
      
      .hover-text {
        z-index: 3;
        visibility: hidden;
        position: relative;
        top: 95px;
        left: -110px;
        min-width: 80px;
        border-radius: 10px;
        padding: 10px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        background-color: #05aefc;
        border: 1px #ffffff solid;
        color: #ffffff;
        font-weight: normal;
        font-size: 1rem;
        
        div {
          margin-bottom: 5px;
        }
        
        div:last-child {
          margin-bottom: 0;
        }
      }
      
      .hover-from {
        .inner {
          display: none;
          z-index: 2;
        }
      }
      
      .hover-from:hover {
        color: transparent;
        
        .inner {
          z-index: 2;
          height: 50%;
          display: block;
          position: absolute;
          bottom: 20px;
          
          background-color: #fff;
        }
      }
      
      .hover-from:hover ~ .hover-text {
        visibility: visible;
      }
      
      .hover-text:hover {
        visibility: visible;
      }
      
    }
    
    .card {
      z-index: 1000;
      background-color: #05aefc;
      box-shadow: 0 0 2px 3px rgba(220, 220, 220, 0.85);
      
      @media screen and (max-height: 500px) {
        padding-top: 1rem;
        padding-bottom: 1.25rem;
      }
      @media screen and (min-height: 500px) {
        padding-top: 1.15rem;
        padding-bottom: 1.5rem;
      }
      @media screen and (min-height: 625px) {
        padding-top: 1.3rem;
        padding-bottom: 1.75rem;
      }
      @media screen and (min-height: 750px) {
        padding-top: 1.5rem;
        padding-bottom: 2rem;
      }
      
      .card-bottom {
        background-color: #fff;
        padding: 0.5rem 1rem 1rem;
        border-radius: 20px;
      }
      
    }
  }
  
  #xieyi1 {
    position: absolute;
    top: 2px;
    left: 5px;
    font-size: 0.75rem;
    color: red;
    text-indent: 2.5ch;
  }
  
  .bottom-link {
    margin-top: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .forget:hover {
    color: red;
  }
  
  /*抖动动画*/
  .shake {
    animation: shake 800ms ease-in-out;
  }
  
  @keyframes shake {
    10%, 90% {
      transform: translate3d(-2px, 0, 0);
    }
    20%, 80% {
      transform: translate3d(+2px, 0, 0);
    }
    30%, 70% {
      transform: translate3d(-3px, 0, 0);
    }
    40%, 60% {
      transform: translate3d(+3px, 0, 0);
    }
    50% {
      transform: translate3d(-4px, 0, 0);
    }
  }
  
  .login-right {
    display: none;
    
    @media screen and (min-width: 1000px) {
      display: block;
      font-size: 1.8rem;
    }
    @media screen and (min-width: 1100px) {
      display: block;
      font-size: 2rem;
    }
    @media screen and (min-width: 1200px) {
      display: block;
      font-size: 2.25rem;
    }
    @media screen and (min-width: 1300px) {
      display: block;
      font-size: 2.5rem;
    }
    @media screen and (min-width: 1400px) {
      display: block;
      font-size: 2.75rem;
    }
    @media screen and (min-width: 1900px) {
      display: block;
      font-size: 3.5rem;
    }
    
    color: rgba(0, 0, 0);
    font-weight: 900;
    
    p {
      margin-bottom: 5vh;
      
      z-index: 3;
      font-family: '微软雅黑';
      letter-spacing: 0.35rch;
      
      /*文字渐变色*/
      background-image: -webkit-linear-gradient(left, #1f1fc5, #c4169a, #7b35d3);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
    
    p:last-child {
      margin-bottom: 0;
    }
    
    /* keyframe名称 动画完成时间 速度曲线 开始前的延迟 播放次数(infinite表示无限循环) 是否反向播放(normal表示不反向播放)*/
    .line1 {
      animation: line11 6s ease-out 0s normal;
    }
    
    .line2 {
      animation: line22 6s ease-out 0s normal;
    }
    
    .line3 {
      animation: line33 6s ease-out 0s normal;
    }
    
    .line4 {
      animation: line44 6s ease-out 0s normal;
    }
    
    /*逐行显示动画*/
    @keyframes line11 {
      0% {
        opacity: 0
      }
      10% {
        opacity: 0
      }
      /*缓冲点，与初始状态一样，透明度都是0*/
      20% {
        opacity: 20%
      }
      40% {
        opacity: 40%
      }
      60% {
        opacity: 60%
      }
      80% {
        opacity: 80%
      }
      100% {
        opacity: 100%;
      }
    }
    @keyframes line22 {
      0% {
        opacity: 0
      }
      10% {
        opacity: 0
      }
      20% {
        opacity: 0
      }
      40% {
        opacity: 20%
      }
      60% {
        opacity: 60%
      }
      80% {
        opacity: 80%
      }
      100% {
        opacity: 100%;
      }
    }
    @keyframes line33 {
      0% {
        opacity: 0
      }
      10% {
        opacity: 0
      }
      20% {
        opacity: 0
      }
      40% {
        opacity: 0
      }
      60% {
        opacity: 60%
      }
      80% {
        opacity: 80%
      }
      100% {
        opacity: 100%;
      }
    }
    @keyframes line44 {
      0% {
        opacity: 0
      }
      10% {
        opacity: 0
      }
      20% {
        opacity: 0
      }
      40% {
        opacity: 0
      }
      60% {
        opacity: 0
      }
      80% {
        opacity: 80%
      }
      100% {
        opacity: 100%;
      }
    }
    
  }
  
  .username-password {
    //以下是与输入框相关的样式
    .input-container {
      height: 50px;
      position: relative;
      width: 100%;
    }
    
    .ic1 {
      margin-top: 15px;
    }
    
    .ic2 {
      margin-top: 20px;
      margin-bottom: 10px;
    }
    
    .input {
      /*background-color: #303245;*/
      border-radius: 12px;
      font-size: 1rem;
      height: 100%;
      outline: 0;
      padding: 0 8px 0 12px;
      width: 100%;
    }
    
    .cut {
      /* background-color: #15172b;*/
      border-radius: 15px;
      height: 20px;
      left: 20px;
      position: absolute;
      top: -20px;
      transform: translateY(0);
      transition: transform 200ms;
    }
    
    .cut-short {
      width: 50px;
    }
    
    .iLabel {
      color: #fff;
      font-family: sans-serif;
      left: 20px;
      line-height: 10px;
      pointer-events: none;
      position: absolute;
      transform-origin: 0 50%;
      transition: transform 200ms, color 200ms;
      top: 20px;
    }
    
    .input:focus ~ .cut {
      transform: translateY(8px);
      background-color: #05aefc;
    }
    
    .input:focus ~ .iLabel {
      transform: translateY(-30px) translateX(10px) scale(0.75);
      color: #ffffff !important;
    }
    
    .input:not(:focus) ~ .iLabel {
      color: #666;
    }
    
    //到此截止，以上是输入框相关的样式
  }


</style>
