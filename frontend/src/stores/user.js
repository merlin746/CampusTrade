import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'

// 从 localStorage 安全恢复数据（检测多标签串号）
function safeRestore() {
  const token = localStorage.getItem('token') || ''
  const userStr = localStorage.getItem('user')
  if (!token || !userStr) return { token: '', user: null }

  let user = null
  try { user = JSON.parse(userStr) } catch { return { token: '', user: null } }

  // 从JWT中解码userId，与localStorage中user的userId比对
  // 防止标签B登录覆盖localStorage后，标签A刷新时拿到错误身份
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    const tokenUserId = Number(payload.sub || payload.userId)
    if (tokenUserId && user.userId && tokenUserId !== user.userId) {
      // token 和 user 不匹配 → 多标签串号，清除有问题的数据
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      return { token: '', user: null }
    }
  } catch {
    // 解码失败，用 user 中的 userId 做简单校验
  }

  return { token, user }
}

export const useUserStore = defineStore('user', () => {
  const saved = safeRestore()
  const token = ref(saved.token)
  const user = ref(saved.user)

  // 是否已登录
  const isLoggedIn = computed(() => !!token.value)

  // 是否是管理员
  const isAdmin = computed(() => user.value?.role === 1)

  // 登录
  async function login(username, password) {
    const res = await authApi.login({ username, password })
    token.value = res.data.token
    user.value = {
      userId: res.data.userId,
      username: res.data.username,
      avatar: res.data.avatar,
      role: res.data.role,
    }
    localStorage.setItem('token', token.value)
    localStorage.setItem('user', JSON.stringify(user.value))
    return res
  }

  // 注册
  async function register(data) {
    return await authApi.register(data)
  }

  // 退出登录
  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  // 刷新用户信息
  async function refreshUser() {
    try {
      const res = await authApi.me()
      user.value = {
        userId: res.data.id,
        username: res.data.username,
        avatar: res.data.avatar,
        role: res.data.role,
      }
      localStorage.setItem('user', JSON.stringify(user.value))
    } catch { /* ignore */ }
  }

  return { token, user, isLoggedIn, isAdmin, login, register, logout, refreshUser }
})
