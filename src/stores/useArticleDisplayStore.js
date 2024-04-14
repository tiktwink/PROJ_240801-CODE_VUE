import {defineStore} from 'pinia'

const useArticleDisplayStore = defineStore('articledisplaystore', {
  persist: { //部分数据持久化
    key: 'articledisplaystore',
    storage: localStorage,
    paths: ['bookId', 'selectedVolumeIndex', 'selectedChapterIndex']
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      bookId: '',
      selectedVolumeIndex: -1,
      selectedChapterIndex: -1,
    }
  },
  
  getters: { // getters是属性式写法，数据的核算器
  
  },
  
  actions: {},
})

export default useArticleDisplayStore//默认导出(导入时不必解构，且导入名随意)
