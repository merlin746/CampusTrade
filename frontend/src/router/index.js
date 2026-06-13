import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  // ===== 用户端页面 =====
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { title: '校园二手交易平台' },
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册' },
  },
  {
    path: '/goods/:id',
    name: 'GoodsDetail',
    component: () => import('@/views/GoodsDetail.vue'),
    meta: { title: '商品详情' },
  },
  {
    path: '/publish',
    name: 'Publish',
    component: () => import('@/views/Publish.vue'),
    meta: { title: '发布商品', requireAuth: true },
  },
  {
    path: '/edit-goods/:id',
    name: 'EditGoods',
    component: () => import('@/views/Publish.vue'),
    meta: { title: '编辑商品', requireAuth: true },
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile.vue'),
    meta: { title: '个人中心', requireAuth: true },
  },
  {
    path: '/my-goods',
    name: 'MyGoods',
    component: () => import('@/views/MyGoods.vue'),
    meta: { title: '我的发布', requireAuth: true },
  },
  {
    path: '/my-orders',
    name: 'MyOrders',
    component: () => import('@/views/MyOrders.vue'),
    meta: { title: '我的订单', requireAuth: true },
  },
  {
    path: '/my-favorites',
    name: 'MyFavorites',
    component: () => import('@/views/MyFavorites.vue'),
    meta: { title: '我的收藏', requireAuth: true },
  },
  {
    path: '/pay/:goodsId',
    name: 'Payment',
    component: () => import('@/views/Payment.vue'),
    meta: { title: '确认支付', requireAuth: true },
  },

  // ===== 管理员端页面 =====
  {
    path: '/admin',
    name: 'AdminLayout',
    component: () => import('@/views/admin/AdminLayout.vue'),
    meta: { title: '管理后台', requireAuth: true, requireAdmin: true },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '仪表盘' },
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/Users.vue'),
        meta: { title: '用户管理' },
      },
      {
        path: 'goods-pending',
        name: 'AdminGoodsPending',
        component: () => import('@/views/admin/GoodsPending.vue'),
        meta: { title: '商品审核' },
      },
      {
        path: 'goods',
        name: 'AdminGoods',
        component: () => import('@/views/admin/GoodsManage.vue'),
        meta: { title: '商品管理' },
      },
      {
        path: 'categories',
        name: 'AdminCategories',
        component: () => import('@/views/admin/Categories.vue'),
        meta: { title: '分类管理' },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 }),
})

// 路由守卫：检查需要登录和管理员权限的页面
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || '校园二手交易平台'
  const token = localStorage.getItem('token')
  const user = JSON.parse(localStorage.getItem('user') || 'null')

  if (to.meta.requireAuth && !token) {
    ElMessage.warning('请先登录')
    next('/login')
    return
  }
  if (to.meta.requireAdmin && (!user || user.role !== 1)) {
    ElMessage.error('无权访问管理后台')
    next('/')
    return
  }
  next()
})

export default router
