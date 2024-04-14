import {defineStore} from 'pinia'

const useEditorStore = defineStore('editorstore', {
  persist: { //部分数据持久化
    key: 'editorstore',
    storage: localStorage,
    paths: ['mode', 'articletemp', 'bookId', 'volumeId', 'chapterId', 'volumeIndex', 'chapterIndex', 'contentHtml', 'contentText', 'novelInfo']
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      //文章模式（默认小说）
      mode: 'novel',
      
      //文章内容详情
      bookId: '',
      volumeIndex: 0,
      volumeId: '',
      chapterIndex: 0,
      chapterId: '',
      contentHtml: '',
      contentText: '',
      novelInfo: '',
      
      //一些不重要的数据
      chapterTitle: '',
      articletemp: ''
    }
  },
  
  getters: { // getters是属性式写法，数据的核算器
  
  },
  
  actions: {
    // toggleLeftSideOn() {
    //   this.leftSideOn = !this.leftSideOn
    // },
    // toggleRightSideOn() {
    //   this.rightSideOn = !this.rightSideOn
    // },
    
  },
})

export default useEditorStore//默认导出(导入时不必解构，且导入名随意)
