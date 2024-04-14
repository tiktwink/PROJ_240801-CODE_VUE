import {defineStore} from 'pinia'

const useTempStore = defineStore('tempstore', {
  persist: { //部分数据持久化
    key: 'tempstore',
    storage: localStorage,
    paths: []
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      mynovels: [
        {
          book_id: 1001,
        }
      ]
    }
  },
  
  getters: { // getters是属性式写法，数据的核算器
  
  },
  
  actions: {
    // toggleLeftSideOn() {
    //   this.leftSideOn = !this.leftSideOn
    // },
  },
})

export default useTempStore//默认导出(导入时不必解构，且导入名随意)
