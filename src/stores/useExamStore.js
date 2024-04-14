import {defineStore} from 'pinia'

const useExamStore = defineStore('examstore', {
  persist: { //自定义数据持久化
    key: 'examstore',
    storage: localStorage,
    paths: [],
    
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      //考试管理
      examChoosen: '',
      //AI阅卷
      examsChoosen: [],
      
      
    }
  },
  
  
})

export default useExamStore //默认导出(导入时不必解构，且导入名随意)
