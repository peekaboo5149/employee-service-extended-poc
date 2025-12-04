import { useEffect, useState } from 'react'

interface ToastProps {
  message: string
  duration?: number // in ms
  onClose?: () => void
}

export function Toast({ message, duration = 3000, onClose }: ToastProps) {
  const [progress, setProgress] = useState(100)
  const [visible, setVisible] = useState(true)

  useEffect(() => {
    let interval: number

    interval = setInterval(() => {
      setProgress((prev) => {
        const next = prev - 100 / (duration / 100)
        return next <= 0 ? 0 : next
      })
    }, 100)

    const timeout = setTimeout(() => {
      setVisible(false)
      onClose?.()
    }, duration)

    return () => {
      clearInterval(interval)
      clearTimeout(timeout)
    }
  }, [duration, onClose])

  if (!visible) return null

  return (
    <div className='fixed right-4 top-4 z-50 animate-slide-in'>
      <div className='bg-gray-800 text-white px-4 py-3 rounded shadow-lg w-64'>
        {message}

        {/* PROGRESS BAR */}
        <div className='h-1 bg-gray-700 mt-3 rounded overflow-hidden'>
          <div
            className='h-full bg-green-500 transition-all duration-100'
            style={{ width: `${progress}%` }}
          />
        </div>
      </div>
    </div>
  )
}
