import { gsap } from 'gsap'
import { useEffect, useRef } from 'react'

interface LoadingOverlayProps {
  show: boolean
}

export function LoadingOverlay({ show }: LoadingOverlayProps) {
  const overlayRef = useRef<HTMLDivElement>(null)
  const cubeRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (!overlayRef.current) return

    if (show) {
      // Fade in overlay
      gsap.to(overlayRef.current, {
        opacity: 1,
        pointerEvents: 'auto',
        duration: 0.3,
      })

      // Start cube animation
      gsap.to(cubeRef.current, {
        rotateY: 360,
        rotateX: 360,
        duration: 2,
        ease: 'none',
        repeat: -1,
      })
    } else {
      // Fade out overlay
      gsap.to(overlayRef.current, {
        opacity: 0,
        pointerEvents: 'none',
        duration: 0.3,
      })

      // Stop cube animation
      gsap.killTweensOf(cubeRef.current)
    }
  }, [show])

  return (
    <div
      ref={overlayRef}
      className='fixed inset-0 z-9999 flex items-center justify-center bg-black/60 opacity-0 pointer-events-none backdrop-blur-sm'
    >
      <div
        ref={cubeRef}
        className='w-20 h-20 border-4 border-blue-400 rounded-md shadow-lg'
        style={{ transformStyle: 'preserve-3d' }}
      ></div>
    </div>
  )
}
