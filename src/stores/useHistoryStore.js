import {defineStore} from 'pinia'

const useHistoryStore = defineStore('historystore', {
  persist: { //自定义数据持久化
    key: 'historystore',
    storage: localStorage,
    paths: ['checkLogs',],
    
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      checkLogs: [],
      
    }
  },
  
  
})

export default useHistoryStore //默认导出(导入时不必解构，且导入名随意)
