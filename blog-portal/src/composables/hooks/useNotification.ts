import { ref, onUnmounted } from 'vue'

export interface UseNotificationOptions {
  duration?: number
}

export interface UseNotificationReturn {
  visible: ReturnType<typeof ref<boolean>>
  message: ReturnType<typeof ref<string>>
  show: (msg: string) => void
  hide: () => void
}

let timeoutId: ReturnType<typeof setTimeout> | null = null

export function useNotification(options: UseNotificationOptions = {}): UseNotificationReturn {
  const { duration = 3000 } = options

  const visible = ref(false)
  const message = ref('')

  function show(msg: string): void {
    message.value = msg
    visible.value = true

    if (timeoutId) {
      clearTimeout(timeoutId)
    }

    timeoutId = setTimeout(() => {
      visible.value = false
    }, duration)
  }

  function hide(): void {
    visible.value = false
    if (timeoutId) {
      clearTimeout(timeoutId)
      timeoutId = null
    }
  }

  onUnmounted(() => {
    if (timeoutId) {
      clearTimeout(timeoutId)
    }
  })

  return {
    visible,
    message,
    show,
    hide,
  }
}
