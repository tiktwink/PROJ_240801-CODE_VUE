import {defineStore} from 'pinia'

const useAppStore = defineStore('appstore', {
  persist: { //自定义数据持久化
    key: 'appstore',
    storage: localStorage,
    paths: ['apptopbarControl', 'count', 'editorText', 'selectedDark', 'itemHolder'],
    
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      title: '首页',
      drawerOn: false, //控制左侧抽屉显示
      
      narrowScreen: screen.width <= 991, //窄屏要默认关闭右侧边栏，且边栏即使展开也要用层叠模式
      
      apptopbarControl: 1,//在隐藏AppTopbar的时候，顶部控制条的样式(3表示彩虹风格，2表示简约风格，1表示提示符,0表示透明)
      count: 0,
      editorText: '',
      selectedDark: false,// systemDarkmode: usePreferredColorScheme(),
      
      sideOn: false, //右侧边栏显示, 若替换为sideOn: screen.width >= 992,表示宽屏默认展示右侧边栏
      sidePart: '', //不持久化，用于在确定了路由之后，进一步确定右侧栏的组件内容（在当前界面右侧栏可能根据不同条件展示不同内容）
      sidePaperId: '', //既包括答卷paper表(答卷)，也包括考试exam表(底卷、答案)
      paperfilleduploaddone: false, //每次将要上传答卷前将该标记置为false，每次上传后变为true
      paperreferuploaddone: false, //每次将要上传答案前将该标记置为false，每次上传后变为true
      
      
      
      //阅卷
      choosenExams: [],
      itemHolder: 0, //钉住的项目
      
    }
  },
  // state()也可以这样定义：state: () => ({ count: 0 })，但不推荐
  
  getters: { // getters是属性式写法，数据的核算器
    doubleCount(state) { // getter器首参是state
      return state.count * 2 // getter访问仓库数据是state.数据名，而不是this.数据名
    } // getter访问其它getter，直接this.函数名，不加()，除非其需要参数
  },
  actions: { // actions是属性式写法，修改数据值
    toggleSideOn() {
      this.sideOn = !this.sideOn // action访问仓库数据是this.数据名，action器可访问this，指向仓库
    },
  },
  
  
})

export default useAppStore //默认导出(导入时不必解构，且导入名随意)
