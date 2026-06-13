import request from '@/utils/request'

export const uploadApi = {
  // 上传单张图片，返回URL
  image: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/upload/image', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },
}
