import {defineStore} from 'pinia'

const usePaperStore = defineStore('paperstore', {
  persist: { //自定义数据持久化
    key: 'paperstore',
    storage: localStorage,
    paths: ['sideOn', 'reImportOn'],
    
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      sideOn: false,
      reImportOn: false,
    }
  },
  
  
})

export default usePaperStore //默认导出(导入时不必解构，且导入名随意)
