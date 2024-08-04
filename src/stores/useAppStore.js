import {defineStore} from 'pinia'

const useAppStore = defineStore('appstore', {
  persist: { //自定义数据持久化
    key: 'appstore',
    storage: localStorage,
    paths: ['defaultData'],
    
  },
  
  state: () => { // state是函数式写法，声明数据及初值
    return {
      title: '首页',
      defaultData: {
        org: {
          orgId: '', //只有id是可定义的，其余属性由id自查
          orgName: '',
          orgType: ''
        },
        cangkuName: '杭州1号仓',
        payWay: '款到发货',
        payWayList: [{
          label: '款到发货',
          value: '款到发货',
          checked: false,
        }, {
          label: '货到付款',
          value: '货到付款'
        }, {
          label: '月结',
          value: '月结'
        }, {
          label: '周结',
          value: '周结'
        }, {
          label: '季结',
          value: '季结'
        }],
        offRate: 5
      }
      
    }
  },
  // state()也可以这样定义：state: () => ({ count: 0 })，但不推荐
  
  getters: { // getters是属性式写法，数据的核算器
  
  },
  actions: { // actions是属性式写法，修改数据值
  
  },
  
  
})

export default useAppStore //默认导出(导入时不必解构，且导入名随意)
