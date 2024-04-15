import {defineStore} from 'pinia'

const useStudentStore = defineStore('studentstore', {
  persist: { //自定义数据持久化
    key: 'studentstore',
    storage: localStorage,
    paths: ['studentChoosen'],
    
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      studentsChoosen: []
      
      
    }
  },
  
  
})

export default useStudentStore //默认导出(导入时不必解构，且导入名随意)
