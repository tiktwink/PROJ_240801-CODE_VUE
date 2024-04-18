//1.导入必需文件
import {createRouter, createWebHistory} from 'vue-router';
import AppLayout from "@/components/layout/AppLayout.vue";
import Dashboard from "@/components/pages/Dashboard.vue";

//2.实例化路由，并配置路由对象(这一步可以拆分为两步)
const router = createRouter({
  // history: createWebHashHistory(),
  history: createWebHistory(),
  
  routes: [
    { //路由1
      path: '/',
      name: 'home',
      component: AppLayout,
      children: [
        {
          path: '/',
          name: '首页',
          component: Dashboard
        },
        {
          path: '/aicheck',
          name: '智能阅卷系统',
          component: () => import('@/components/aicheck/AiCheck.vue')
        },
        {
          path: '/checkoutresult',
          name: '智能阅卷系>阅卷结果', //只是评阅结果页，详情在'/checkout'中看
          component: () => import('@/components/aicheck/AiCheckOutResult.vue')
        },
        {
          path: '/exam',
          name: '考试管理',
          component: () => import('@/components/exam/Exam.vue')
        },
        {
          path: '/paper',
          name: '试卷管理',
          meta: {
            keepAlive: true, //需要缓存
          },
          component: () => import('@/components/paper/Paper.vue')
        },
        {
          path: '/teacher',
          name: '教师管理',
          component: () => import('@/components/teacher/Teacher.vue')
        },
        {
          path: '/student',
          name: '学生管理',
          component: () => import('@/components/student/Student.vue')
        },
        {
          path: '/subject',
          name: '学科管理',
          component: () => import('@/components/subject/Subject.vue')
        },
        {
          path:'/history',
          name: '日志管理',
          component:()=> import('@/components/history/History.vue')
        }
      ]
    },
    {
      path: '/login',
      name: '登录页',
      component: () => import('@/components/pages/UserLogin.vue')
    },
    {
      path: '/signup',
      component: () => import('@/components/pages/UserSignup.vue')
    },
    {
      path: '/checkout',
      name: '评阅结果',
      component: () => import('@/components/aicheck/AiCheckOut.vue')
    },
   
    {
      path: '/test',
      component: () => import('@/components/utility/Test.vue')
    },
    {
      path: '/test1',
      component: () => import('@/components/datavisual/DvTest1.vue')
    },
    {
      path: '/tag',
      component: () => import('@/components/utility/Tag_array_oprate_in_springboot.vue')
    },
   
    
  
  ]
});


//2.2全局路由守卫
// router.beforeEach((to, from) => {
//   // if(!to.name.search('article')&&!to.name.search('editor')){
//   //   return false
//   // }
// })

router.beforeEach((to, from) => {
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
})


//3.导出路由实例
export default router;
