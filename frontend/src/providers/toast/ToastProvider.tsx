import { useEffect, useState } from 'react'
import { toastSubscribe, type ToastOptions } from './toast-store'

let idCounter = 0

export function ToastProvider({ children }: { children: React.ReactNode }) {
  const [toasts, setToasts] = useState<
    (ToastOptions & { id: number; progress: number })[]
  >([])

  useEffect(() => {
    const unsubscribe = toastSubscribe((options) => {
      const id = idCounter++
      const duration = options.duration ?? 2000

      setToasts((prev) => [...prev, { ...options, id, progress: 100 }])

      // animate progress bar
      const interval = setInterval(() => {
        setToasts((prev) =>
          prev.map((t) =>
            t.id === id
              ? { ...t, progress: t.progress - 100 / (duration / 100) }
              : t
          )
        )
      }, 100)

      // remove toast after duration
      setTimeout(() => {
        clearInterval(interval)
        setToasts((prev) => prev.filter((t) => t.id !== id))
      }, duration)
    })

    return unsubscribe
  }, [])

  return (
    <>
      {children}

      {/* Toast container */}
      <div className='fixed top-4 right-4 z-50 flex flex-col gap-3'>
        {toasts.map((t) => (
          <ToastItem key={t.id} toast={t} />
        ))}
      </div>
    </>
  )
}

function ToastItem({
  toast,
}: {
  toast: ToastOptions & { id: number; progress: number }
}) {
  const getColor = () => {
    switch (toast.type) {
      case 'SUCCESS':
        return 'bg-green-600'
      case 'ERROR':
        return 'bg-red-600'
      case 'WARN':
        return 'bg-yellow-500'
      default:
        return 'bg-gray-800'
    }
  }

  return (
    <div className='animate-slide-in w-72 rounded shadow-lg text-white'>
      <div className={`p-4 rounded-t ${getColor()}`}>{toast.message}</div>

      <div className='h-1 bg-neutral-700 rounded-b overflow-hidden'>
        <div
          className='h-full bg-white transition-all duration-100'
          style={{ width: `${toast.progress}%` }}
        />
      </div>
    </div>
  )
}
