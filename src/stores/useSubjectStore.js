import {defineStore} from 'pinia'

const useSubjectStore = defineStore('subjectstore', {
  persist: { //自定义数据持久化
    key: 'subjectstore',
    storage: localStorage,
    paths: ['subjects'],
    
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      subjects: [
        {
          title: '计算机',
          id: '50701', type: '工科类', sugsGrade: '7', num: 673,
        },
        {
          title: '计算机',
          id: '50801', type: '工科类', sugsGrade: '8', num: 553,
        },
        {
          title: '计算机',
          id: '50901', type: '工科类', sugsGrade: '9', num: 679,
        },
        {
          title: '语文', num: 701,
          id: '50702', type: '文科类', sugsGrade: '7',
        },
        {
          title: '语文', num: 673,
          id: '50802', type: '文科类', sugsGrade: '8',
        },
        {
          title: '语文', num: 542,
          id: '50902', type: '文科类', sugsGrade: '9',
        },
        {
          title: '数学', num: 773,
          id: '50703', type: '理科类', sugsGrade: '7',
        },
        {
          title: '数学', num: 913,
          id: '50803', type: '理科类', sugsGrade: '8',
        },
        {
          title: '数学', num: 873,
          id: '50903', type: '理科类', sugsGrade: '9',
        },
        {
          title: '英语', num: 675,
          id: '50704', type: '文科类', sugsGrade: '7',
        },
        {
          title: '英语', num: 722,
          id: '50804', type: '文科', sugsGrade: '8',
        },
      ], //校设学科，以ID区分
      subjectsChoosen: [],
      //虚拟的相关考试
      exams: [
        {
          check: false, //已阅卷
          doubleCheck: false, //已复阅
          id: 101,
          examTitle: '山羊市第一中学2024高三一模',
          subject: '数学',
          title: '山羊市第一中学2024高三一模-数学',
          num: 199,
          outCounts: 17, timeText: '2024-01-01 10:10:10',
          classes: [
            {
              id: 201,
              title: '2024届高三一班',
              num: 47,
              outCounts: 3,
            },
            {
              id: 202,
              title: '2024届高三二班',
              num: 48,
              outCounts: 8,
            },
            {
              id: 203,
              title: '2024届高三一班',
              num: 53,
              outCounts: 1
            },
            {
              id: 204,
              title: '2024届高三三班',
              num: 51,
              outCounts: 5,
            }
          ]
        },
        {
          check: false,
          doubleCheck: false,
          id: 102,
          examTitle: '山羊市第一中学2024高三一模',
          subject: '语文',
          title: '山羊市第一中学2024高三一模-语文',
          num: 199,
          outCounts: 17, timeText: '2024-01-02 10:10:10',
          classes: [
            {
              id: 201,
              title: '2024届高三一班',
              num: 47,
              outCounts: 3,
            },
            {
              id: 202,
              title: '2024届高三二班',
              num: 48,
              outCounts: 8,
            },
            {
              id: 203,
              title: '2024届高三一班',
              num: 53,
              outCounts: 1
            },
            {
              id: 204,
              title: '2024届高三三班',
              num: 51,
              outCounts: 5,
            }
          ]
        },
        {
          check: false,
          doubleCheck: false,
          id: 103,
          examTitle: '山羊市第一中学2024高三一模',
          subject: '计算机',
          title: '山羊市第一中学2024高三一模-计算机',
          num: 199,
          outCounts: 17, timeText: '2024-01-03 10:10:10',
          classes: [
            {
              id: 201,
              title: '2024届高三一班',
              num: 47,
              outCounts: 3,
            },
            {
              id: 202,
              title: '2024届高三二班',
              num: 48,
              outCounts: 8,
            },
            {
              id: 203,
              title: '2024届高三一班',
              num: 53,
              outCounts: 1
            },
            {
              id: 204,
              title: '2024届高三三班',
              num: 51,
              outCounts: 5,
            }
          ]
        },
        {
          check: false,
          doubleCheck: false,
          id: 104,
          examTitle: '山羊市第一中学2024高三一模',
          subject: '英语',
          title: '山羊市第一中学2024高三一模-英语',
          num: 199,
          outCounts: 17, timeText: '2024-01-04 10:10:10',
          classes: [
            {
              id: 201,
              title: '2024届高三一班',
              num: 47,
              outCounts: 3,
            },
            {
              id: 202,
              title: '2024届高三二班',
              num: 48,
              outCounts: 8,
            },
            {
              id: 203,
              title: '2024届高三一班',
              num: 53,
              outCounts: 1
            },
            {
              id: 204,
              title: '2024届高三三班',
              num: 51,
              outCounts: 5,
            }
          ]
        },
        {
          check: false,
          doubleCheck: false,
          id: 105,
          examTitle: '山羊市第一中学2024高三二模',
          subject: '数学',
          title: '山羊市第一中学2024高三二模-数学',
          num: 199,
          outCounts: 17, timeText: '2024-01-05 10:10:10',
          classes: [
            {
              id: 201,
              title: '2024届高三一班',
              num: 47,
              outCounts: 3,
            },
            {
              id: 202,
              title: '2024届高三二班',
              num: 48,
              outCounts: 8,
            },
            {
              id: 203,
              title: '2024届高三一班',
              num: 53,
              outCounts: 1
            },
            {
              id: 204,
              title: '2024届高三三班',
              num: 51,
              outCounts: 5,
            }
          ]
        },
        {
          check: false,
          doubleCheck: false,
          id: 106,
          examTitle: '山羊市第一中学2024高三二模',
          subject: '计算机',
          title: '山羊市第一中学2024高三二模-计算机',
          num: 199,
          outCounts: 17, timeText: '2024-01-06 10:10:10',
          classes: [
            {
              id: 201,
              title: '2024届高三一班',
              num: 47,
              outCounts: 3,
            },
            {
              id: 202,
              title: '2024届高三二班',
              num: 48,
              outCounts: 8,
            },
            {
              id: 203,
              title: '2024届高三一班',
              num: 53,
              outCounts: 1
            },
            {
              id: 204,
              title: '2024届高三三班',
              num: 51,
              outCounts: 5,
            }
          ]
        },
        {
          check: false,
          doubleCheck: false,
          id: 106,
          examTitle: '山羊市第一中学2024高三三模',
          subject: '计算机',
          title: '山羊市第一中学2024高三三模-计算机',
          num: 199,
          outCounts: 17, timeText: '2024-01-12 10:10:10',
          classes: [
            {
              id: 201,
              title: '2024届高三一班',
              num: 47,
              outCounts: 3,
            },
            {
              id: 202,
              title: '2024届高三二班',
              num: 48,
              outCounts: 8,
            },
            {
              id: 203,
              title: '2024届高三一班',
              num: 53,
              outCounts: 1
            },
            {
              id: 204,
              title: '2024届高三三班',
              num: 51,
              outCounts: 5,
            }
          ]
        },
        {
          check: false,
          doubleCheck: false,
          id: 107,
          examTitle: '山羊市第一中学2024高三二模',
          subject: '英语',
          title: '山羊市第一中学2024高三二模-英语',
          num: 199,
          outCounts: 17, timeText: '2024-01-07 10:10:10',
          classes: [
            {
              id: 201,
              title: '2024届高三一班',
              num: 47,
              outCounts: 3,
            },
            {
              id: 202,
              title: '2024届高三二班',
              num: 48,
              outCounts: 8,
            },
            {
              id: 203,
              title: '2024届高三一班',
              num: 53,
              outCounts: 1
            },
            {
              id: 204,
              title: '2024届高三三班',
              num: 51,
              outCounts: 5,
            }
          ]
        },
      
      ],
    }
  },
  
  /* getters: { // getters是属性式写法，数据的核算器
     doubleCount(state) { // getter器首参是state
       return state.count * 2 // getter访问仓库数据是state.数据名，而不是this.数据名
     } // getter访问其它getter，直接this.函数名，不加()，除非其需要参数
   },
   actions: { // actions是属性式写法，修改数据值
     toggleSideOn() {
       this.sideOn = !this.sideOn // action访问仓库数据是this.数据名，action器可访问this，指向仓库
     },
   },*/
  
})

export default useSubjectStore //默认导出(导入时不必解构，且导入名随意)
