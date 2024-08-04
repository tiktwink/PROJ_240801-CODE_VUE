//1.导入必需文件
import Dashboard from "@/view/layout/Dashboard.vue";
import {createRouter, createWebHistory} from 'vue-router';

//2.实例化路由，并配置路由对象(这一步可以拆分为两步)
const router = createRouter({
  // history: createWebHashHistory(),
  history: createWebHistory(),
  
  routes: [
    { //路由1
      path: '/',
      name: 'home',
      // component: AppMain,
      redirect: '/login',
      children: [
        {
          path: '/home',
          name: '首页',
          component: Dashboard
        },
        {
          path: '/product',
          children: [{
            path: '/product/input',
            // name:'产品管理 > 录入与修改',
            name: '产品管理',
            component: () => import('@/view/pages/ProductInput.vue')
          }, {
            path: '/product/query',
            name: '产品管理 > 库存查询',
            component: () => import('@/view/pages/utility/ProductQuery.vue')
          }]
        },
        {
          path: '/operation',
          children: [
            {
              path: '/operation/import',
              name: '出入库 > 入库',
              component: () => import('@/view/pages/OperationImport.vue')
            },
            {
              path: '/operation/export',
              name: '出入库 > 出库',
              component: () => import('@/view/pages/OperationExport.vue')
            }, {
              path: '/operation/oplog',
              name: '出入库 > 明细',
              component: () => import('@/view/pages/OperationLog.vue')
            }
          ]
        },
        {
          path: '/org',
          name: '单位管理',
          component: () => import('@/view/pages/Org.vue')
        },
        {
          path: '/set',
          name: '设置',
          component: () => import('@/view/pages/Set.vue')
        },
        {
          path: '/user',
          name: '个人信息',
          component: () => import('@/view/pages/utility/User.vue')
        }
      ]
    },
    
    {
      path: '/login',
      name: 'login',
      component: () => import('@/view/layout/AppLogin.vue')
    },
  ]
})


//2.2全局路由守卫
// router.beforeEach((to, from) => {
//   // if(!to.name.search('article')&&!to.name.search('editor')){
//   //   return false
//   // }
// })
// router.beforeEach((to, from) => {
// //页面哈希定位
// if (to.hash) {
//   console.log(to.hash)
//   const element = document.querySelector(to.hash);
//   if (element) {
//     element.scrollIntoView({behavior: 'smooth'});
//   }
// }
//外部路径控制
// if (!routeList.some(item => item === to.path)) {
//   console.log('外部路径:' + to.path)
//   alert('暂不支持跳转外链')
//   return false
// } else {
//   console.log('内部路径:' + to.path)
// }
// })

//3.导出路由实例
export default router;
