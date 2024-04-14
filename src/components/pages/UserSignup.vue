<script>
  import axios from "axios";
  import md5 from 'js-md5';
  // js中MD5加密
  // var key = md5('hello');
  // console.log("加密后："key) //加密后：5d41402abc4b2a76b9719d911017c592
  
  export default {
    name: 'UserSignup',
    data() {
      return {
        username: '',
        password: '',
        usernick: '',
      }
    },
    methods: {
      toSignup() {
        if (!this.validUsername()) {
          alert('用户名不合法')
          return
        }
        if (!this.validPassword()) {
          alert('密码不合法')
          return
        }
        
        if (this.usernick.length > 7) {
          alert('昵称长度不正确')
          return
        }
        
        let md5password = md5(this.password)
        // console.log('md5pw: ')
        // console.log(md5password)
        axios.post('/api/user/signin', {
          username: this.username,
          password: md5password,
          usernick: this.usernick,
          // keyword: this.keyword
        })
        .then(res => {
          if (res.data.code === 0) {
            alert(`注册成功，您的用户名是: ${this.username}`)
          } else if (res.data.code === 1) {
            console.log(res)
            alert('用户名已存在')
          } else {
            console.log(res)
            alert('注册失败，未知原因')
          }
        })
        .catch(err => {
          console.log(err)
          alert('注册错误')
        })
      },
      validUsername() {
        // 长度限制，4-16 个字符
        if (this.username.length < 6 || this.username.length > 30) {
          return false;
        }
        // 字符限制，只允许字母、数字
        var reg = /^[a-zA-Z0-9]+$/;
        if (!reg.test(this.username)) {
          return false;
        }
        // 空格限制(不允许空格)
        if (/\s/.test(this.username)) {
          return false;
        }
        // 敏感词限制，这里假设不允许包含 admin、root 等词汇
        if (this.username.indexOf('tiktwink') >= 0 || this.username.indexOf('lijp') >= 0 || this.username.indexOf('admin') >= 0 || this.username.indexOf('root') >= 0) {
          return false;
        }
        return true;
      },
      validPassword() {
        //必须包含字母(不分大小)、数字，长度在8-20
        var pwdRegex = new RegExp('(?=.*[0-9])(?=.*[a-zA-Z]).{8,20}');
        if (!pwdRegex.test(this.password)) {
          return false
        }
        // 空格限制(不允许空格)
        if (/\s/.test(this.password)) {
          return false;
        }
        return true;
      },
    },
  }
</script>

<template>
  <div class="bg-[#05aefc] h-[100vh] flex flex-col items-center p-4 " style="width:100%;">
    <div class="font-bold text-2xl mt-4 mb-2 flex items-center gap-x-2 text-white pl-6" style=" width:200px; ">
      <div>注册学生账号</div>
      
      <span class="wenhao" style="width:40px;">
        <svg class="icon" height="16" p-id="2122" t="1710082565519" version="1.1"
             viewBox="0 0 1024 1024" width="16" xmlns="http://www.w3.org/2000/svg">
          <path
              d="M512 0a512 512 0 1 0 512 512A512.5632 512.5632 0 0 0 512 0z m0 819.2a51.2 51.2 0 1 1 0-102.4512A51.2 51.2 0 0 1 512 819.2z m51.2-232.1408V614.4a51.2 51.2 0 0 1-102.4 0v-72.6016a51.2 51.2 0 0 1 53.1456-51.1488 73.5232 73.5232 0 0 0 76.1856-73.7792 76.8512 76.8512 0 1 0-153.6-5.9392 50.4832 50.4832 0 0 1-53.0944 49.2032 51.2 51.2 0 0 1-49.152-53.0944A179.2 179.2 0 1 1 563.2 587.0592z"
              fill="#ffffff" p-id="2123"></path>
        </svg>
        <span class="wenhaotip"
              style="font-size:0.8rem; color:#fff; font-weight:normal;">教师账号由管理员授权颁发，不支持自行注册！</span>
      </span>
    
    </div>
    
    <div class="flex flex-col items-center px-2 gap-y-3 my-4" style="width:400px; ">
      <div><input v-model.trim="username"
                  class="w-[320px] h-[40px] outline-none px-2 py-1 rounded-lg bg-white text-black text-base"
                  maxlength="31" placeholder="学号(由学校颁发，6-20位)" type="text"></div>
      <div><input v-model.trim="password"
                  class="w-[320px] h-[40px] outline-none px-2 py-1 rounded-lg bg-white text-black text-base"
                  maxlength="127" placeholder="密码(必须包含字母、数字，8-20字)" type="password"></div>
      <div><input v-model.trim="usernick"
                  class="w-[320px] h-[40px] outline-none px-2 py-1 rounded-lg bg-white text-black text-base"
                  maxlength="8" placeholder="实名(必选，最多7字)" type="text"></div>
    </div>
    
    <div class="my-2 flex justify-between gap-x-2" style="width:320px;">
      <div class="w-[150px] btn btn-sm btn-ghost bg-base-100 hover:bg-secondary" @click="$router.push('/login')">
        返回登录
      </div>
      <div class="w-[150px] btn btn-sm btn-ghost bg-base-100 hover:bg-secondary" @click="toSignup"
      >提交注册
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
  .wenhao {
    position: relative;
    left: 0;
    width: 400px;
    height: 1rem;
  }
  
  .wenhaotip {
    display: none;
    margin-left: 0.5rem;
    position: absolute;
    width: 600px;
    top: -0.1rem;
    left: 0.8rem;
  }
  
  .wenhao:hover > .wenhaotip {
    display: block;
  }
</style>
