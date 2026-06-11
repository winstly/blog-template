/**
 * Canvas 光影动画 Composable
 * 提供登录页面的背景动画效果
 */

import { onUnmounted, type Ref } from 'vue'

/** 光影粒子类 */
class LightParticle {
  x: number
  y: number
  size: number
  speedX: number
  speedY: number
  opacity: number
  hue: number
  pulseSpeed: number
  pulseOffset: number

  constructor(canvasWidth: number, canvasHeight: number) {
    this.x = Math.random() * canvasWidth
    this.y = Math.random() * canvasHeight
    this.size = Math.random() * 2 + 0.5
    this.speedX = (Math.random() - 0.5) * 0.3
    this.speedY = (Math.random() - 0.5) * 0.3
    this.opacity = Math.random() * 0.5 + 0.2
    this.hue = Math.random() * 40 + 200 // 蓝色到靛蓝色范围
    this.pulseSpeed = Math.random() * 0.02 + 0.01
    this.pulseOffset = Math.random() * Math.PI * 2
  }

  update(canvasWidth: number, canvasHeight: number) {
    this.x += this.speedX
    this.y += this.speedY

    // 边界回弹
    if (this.x < 0 || this.x > canvasWidth) this.speedX *= -1
    if (this.y < 0 || this.y > canvasHeight) this.speedY *= -1

    // 脉冲效果
    this.pulseOffset += this.pulseSpeed
    this.opacity = 0.3 + Math.sin(this.pulseOffset) * 0.2
  }
}

/** 光束类 */
class LightBeam {
  x: number
  y: number
  angle: number
  length: number
  width: number
  opacity: number
  speed: number
  hue: number

  constructor(canvasWidth: number, canvasHeight: number) {
    this.x = Math.random() * canvasWidth
    this.y = Math.random() * canvasHeight
    this.angle = Math.random() * Math.PI * 2
    this.length = Math.random() * 150 + 50
    this.width = Math.random() * 2 + 0.5
    this.opacity = Math.random() * 0.15 + 0.05
    this.speed = (Math.random() - 0.5) * 0.005
    this.hue = Math.random() * 40 + 200
  }

  update() {
    this.angle += this.speed
    this.opacity += Math.sin(Date.now() * 0.001) * 0.001
    this.opacity = Math.max(0.03, Math.min(0.2, this.opacity))
  }
}

export interface UseCanvasAnimationOptions {
  /** 粒子数量，默认 80 */
  particleCount?: number
  /** 光束数量，默认 12 */
  beamCount?: number
  /** 连线最大距离，默认 120 */
  connectDistance?: number
}

/**
 * 使用 Canvas 光影动画
 *
 * @param canvasRef - Canvas 元素引用
 * @param options - 配置选项
 *
 * @example
 * const canvasRef = ref<HTMLCanvasElement>()
 * const { init } = useCanvasAnimation(canvasRef)
 *
 * onMounted(() => {
 *   init()
 * })
 */
export function useCanvasAnimation(
  canvasRef: Ref<HTMLCanvasElement | undefined>,
  options: UseCanvasAnimationOptions = {}
) {
  const {
    particleCount = 80,
    beamCount = 12,
    connectDistance = 120,
  } = options

  let particles: LightParticle[] = []
  let beams: LightBeam[] = []
  let animationId: number
  let ctx: CanvasRenderingContext2D | null = null
  let resizeCleanup: (() => void) | null = null
  let isActive = false

  /**
   * 绘制背景渐变
   */
  function drawBackground(canvas: HTMLCanvasElement) {
    if (!ctx) return

    const gradient = ctx.createRadialGradient(
      canvas.width * 0.3, canvas.height * 0.3, 0,
      canvas.width * 0.5, canvas.height * 0.5, canvas.width * 0.8
    )
    gradient.addColorStop(0, '#1a1f2e')
    gradient.addColorStop(0.5, '#141824')
    gradient.addColorStop(1, '#0d1017')

    ctx.fillStyle = gradient
    ctx.fillRect(0, 0, canvas.width, canvas.height)
  }

  /**
   * 绘制光束
   */
  function drawBeams() {
    if (!ctx) return

    beams.forEach(beam => {
      beam.update()

      const endX = beam.x + Math.cos(beam.angle) * beam.length
      const endY = beam.y + Math.sin(beam.angle) * beam.length

      const beamGradient = ctx!.createLinearGradient(beam.x, beam.y, endX, endY)
      beamGradient.addColorStop(0, `hsla(${beam.hue}, 70%, 60%, 0)`)
      beamGradient.addColorStop(0.5, `hsla(${beam.hue}, 70%, 60%, ${beam.opacity})`)
      beamGradient.addColorStop(1, `hsla(${beam.hue}, 70%, 60%, 0)`)

      ctx!.strokeStyle = beamGradient
      ctx!.lineWidth = beam.width
      ctx!.lineCap = 'round'
      ctx!.beginPath()
      ctx!.moveTo(beam.x, beam.y)
      ctx!.lineTo(endX, endY)
      ctx!.stroke()
    })
  }

  /**
   * 绘制粒子
   */
  function drawParticles(canvas: HTMLCanvasElement) {
    if (!ctx) return

    particles.forEach((particle) => {
      particle.update(canvas.width, canvas.height)

      // 绘制发光效果
      const glowGradient = ctx!.createRadialGradient(
        particle.x, particle.y, 0,
        particle.x, particle.y, particle.size * 4
      )
      glowGradient.addColorStop(0, `hsla(${particle.hue}, 80%, 70%, ${particle.opacity})`)
      glowGradient.addColorStop(0.5, `hsla(${particle.hue}, 80%, 60%, ${particle.opacity * 0.3})`)
      glowGradient.addColorStop(1, 'transparent')

      ctx!.fillStyle = glowGradient
      ctx!.beginPath()
      ctx!.arc(particle.x, particle.y, particle.size * 4, 0, Math.PI * 2)
      ctx!.fill()

      // 绘制核心
      ctx!.fillStyle = `hsla(${particle.hue}, 90%, 80%, ${particle.opacity + 0.3})`
      ctx!.beginPath()
      ctx!.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2)
      ctx!.fill()
    })
  }

  /**
   * 绘制粒子连线
   */
  function drawConnections() {
    if (!ctx) return

    ctx.lineWidth = 0.5
    for (let i = 0; i < particles.length; i++) {
      for (let j = i + 1; j < particles.length; j++) {
        const dx = particles[i].x - particles[j].x
        const dy = particles[i].y - particles[j].y
        const distance = Math.sqrt(dx * dx + dy * dy)

        if (distance < connectDistance) {
          const opacity = (1 - distance / connectDistance) * 0.15
          ctx!.strokeStyle = `rgba(59, 130, 246, ${opacity})`
          ctx!.beginPath()
          ctx!.moveTo(particles[i].x, particles[i].y)
          ctx!.lineTo(particles[j].x, particles[j].y)
          ctx!.stroke()
        }
      }
    }
  }

  /**
   * 动画循环
   */
  function animate() {
    if (!isActive) return

    const canvas = canvasRef.value
    if (!ctx || !canvas) return

    drawBackground(canvas)
    drawBeams()
    drawParticles(canvas)
    drawConnections()

    animationId = requestAnimationFrame(animate)
  }

  /**
   * 初始化 Canvas 动画
   */
  function init() {
    const canvas = canvasRef.value
    if (!canvas) return

    ctx = canvas.getContext('2d')
    if (!ctx) return

    isActive = true

    // 设置 Canvas 尺寸
    const resize = () => {
      const parent = canvas.parentElement
      if (parent) {
        canvas.width = parent.offsetWidth
        canvas.height = parent.offsetHeight
      }
    }
    resize()
    window.addEventListener('resize', resize)
    resizeCleanup = () => window.removeEventListener('resize', resize)

    // 初始化粒子和光束
    particles = Array.from({ length: particleCount }, () => new LightParticle(canvas.width, canvas.height))
    beams = Array.from({ length: beamCount }, () => new LightBeam(canvas.width, canvas.height))

    // 开始动画
    animate()
  }

  /**
   * 停止动画
   */
  function stop() {
    isActive = false
    if (animationId) {
      cancelAnimationFrame(animationId)
    }
    if (resizeCleanup) {
      resizeCleanup()
    }
  }

  // 自动清理
  onUnmounted(() => {
    stop()
  })

  return {
    init,
    stop,
  }
}
