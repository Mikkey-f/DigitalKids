import { ElNotification } from 'element-plus'

// 一定记得导出显式对象！
export const showNotification = {
  // 成功
  success(message: string, title = '成功') {
    ElNotification({
      title,
      message,
      type: 'success',
      duration: 3000, 
      position: 'top-right',
      customClass: 'custom-notification'
    })
  },

  // 错误
  error(message: string, title = '错误') {
    ElNotification({
      title,
      message,
      type: 'error',
      duration: 4000, 
      position: 'top-right',
      customClass: 'custom-notification'
    })
  },

  // 警告
  warning(message: string, title = '警告') {
    ElNotification({
      title,
      message,
      type: 'warning',
      duration: 4000, 
      position: 'top-right',
      customClass: 'custom-notification'
    })
  },

  // 信息
  info(message: string, title = '提示') {
    ElNotification({
      title,
      message,
      type: 'info',
      duration: 3000, 
      position: 'top-right',
      customClass: 'custom-notification'
    })
  }
} 