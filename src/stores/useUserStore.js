import {defineStore} from 'pinia'

const useUserStore = defineStore('userstore', {
  persist: { //部分数据持久化
    key: 'userstore',
    storage: localStorage,
    paths: ['username', 'password', 'token', 'tokenValidDt']
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      username: '1001',
      password: '123456',
      token: '',
      tokenValidDt: '',
    }
  },
  
  getters: { // getters是属性式写法，数据的核算器
  
  },
  
  actions: {},
})

export default useUserStore//默认导出(导入时不必解构，且导入名随意)
