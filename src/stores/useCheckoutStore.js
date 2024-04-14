import {defineStore} from 'pinia'

const useCheckoutStore = defineStore('checkoutstore', {
  persist: { //自定义数据持久化
    key: 'checkoutstore',
    storage: localStorage,
    paths: [],
    
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      mode:'paper',
      papers: [],
      exams:[],
      paperChoosen: '',
    }
  },
  
  
})

export default useCheckoutStore //默认导出(导入时不必解构，且导入名随意)
