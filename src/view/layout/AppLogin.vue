<script>
  import {axiosUtil} from "@/apis/axios/axiosUtil.js";
  import useUserStore from "@/stores/useUserStore.js";
  import {useDebounceFn as useDebounceFn} from "@vueuse/core";
  
  export default {
	data() {
	  return {
		userX: useUserStore()
	  }
	},
	watch: {
	  'userX.username'(newValue, oldValue) {
		if (String(newValue).trim() !== '') {
		  this.tryIdentify(newValue)
		}
	  }
	},
	methods: {
	  tryLogin() {
		console.log('login...')
		
		axiosUtil.post('/api/user/login', {
		  username: this.userX.username,
		  password: this.userX.password
		})
			.then(res => {
			  console.log(res)
			  if (res.data.code === 0) {
				this.$toast.add({
				  severity: 'success',
				  summary: '登录成功',
				  life: 1500
				})
				
				this.userX.password = '' //清空密码，但保留用户名
				this.userX.token = res.data.data
				
				setTimeout(() => {
				  this.$router.replace('/home')
				}, 300)
			  } else {
				this.$toast.add({
				  severity: 'error',
				  summary: '登录失败，请校验用户信息',
				  life: 2000
				})
				// this.userX.username=''
				this.userX.password = ''
				this.userX.token = ''
			  }
			})
			.catch(err => {
			  console.log(err)
			  this.$toast.add({
				severity: 'error',
				summary: '登录失败，请校验用户信息',
				life: 2000
			  })
			  this.userX.token = ''
			  this.userX.password = ''
			})
	  },
	},
	// 防抖函数
	setup() {
	  const userX = useUserStore()
	  const tryIdentify = useDebounceFn((username) => {
		axiosUtil(`/api/query/try-identify/${username}`)
			.then(res => {
			  if (res.data.code === 0) {
				userX.usernick = res.data.data
			  } else {
				userX.usernick = ''
			  }
			})
			.catch(err => {
			  userX.usernick = ''
			})
	  }, 500)
	  
	  return {
		tryIdentify
	  }
	}
  }
</script>

<template>
  <div class="mt-24 w-full h-full flex flex-col gap-4 justify-center items-center *:border">
	<p class="border-none mb-2 text-lg font-bold w-full flex justify-center items-center">您好，请登录！</p>
	
	<div class="user-card rounded-xl w-[320px] px-4 py-4 bg-white  flex flex-col items-center justify-between gap-4 *:rounded-lg">
	  <span><text>用户代码：</text><input v-model.trim="userX.username" /></span>
	  <span><text>用户名称：</text><input :class="{'text-red-500':userX.username===''}" :value="!userX.username?'请输入用户代码':!userX.usernick?'未查询到该用户':userX.usernick" class="opacity-60" disabled /></span>
	  <span><text>登录密码：</text><input type="password" v-model.trim="userX.password" /></span>
	</div>
	
	<div class="bg-white rounded-xl w-[320px] px-4 py-4 flex justify-between gap-2 ">
	  <span @click="tryLogin" class="w-[120px] h-[35px] cursor-pointer hover:bg-amber-200 bg-gray-100 rounded-2xl flex justify-center items-center font-bold">登录</span>
	  <span @click="userX.username='';userX.password='';userX.usernick=''" class="w-[120px] h-[35px] cursor-pointer hover:bg-amber-200 bg-gray-100 rounded-2xl flex justify-center items-center font-bold">取消</span>
	</div>
  </div>
</template>

<style lang="scss" scoped>
  .user-card {
	> span {
	  display: flex;
	  width: 100%;
	  gap: 0.5rem;
	  align-items: center;
	  
	  > text {
		flex: none;
	  }
	  
	  > input {
		flex: 1 1 auto;
		border: none;
		outline: none;
		background-color: var(--gray-100);
		border-radius: 0.5rem;
		text-indent: 1ch;
		padding: 0.25rem 0.5rem;
	  }
	}
  }
</style>
