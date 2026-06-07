import { ref, onMounted, onUnmounted } from 'vue'

export function useScrollAnimation(threshold = 0.1) {
  const elementRef = ref<HTMLElement | null>(null)
  const isVisible = ref(false)

  let observer: IntersectionObserver | null = null

  onMounted(() => {
    observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            isVisible.value = true
          }
        })
      },
      { threshold }
    )

    if (elementRef.value) {
      observer.observe(elementRef.value)
    }
  })

  onUnmounted(() => {
    if (observer) {
      observer.disconnect()
    }
  })

  return {
    elementRef,
    isVisible,
  }
}
