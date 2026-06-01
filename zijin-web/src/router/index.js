import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue'), meta: { noAuth: true } },
  { path: '/register', name: 'Register', component: () => import('../views/Register.vue'), meta: { noAuth: true } },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/home',
    children: [
      { path: 'home', name: 'Home', component: () => import('../views/Home.vue'), meta: { title: '首页' } },
      { path: 'department', name: 'Department', component: () => import('../views/Department.vue'), meta: { title: '部门管理', roles: ['admin'] } },
      { path: 'major', name: 'Major', component: () => import('../views/Major.vue'), meta: { title: '专业管理', roles: ['admin'] } },
      { path: 'class', name: 'Class', component: () => import('../views/Class.vue'), meta: { title: '班级管理', roles: ['admin', 'teacher'] } },
      { path: 'account', name: 'Account', component: () => import('../views/Account.vue'), meta: { title: '账户维护', roles: ['admin'] } },
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const user = localStorage.getItem('zijin_user')
  if (to.meta.noAuth) {
    next()
  } else if (!user) {
    next('/login')
  } else {
    next()
  }
})

export default router
