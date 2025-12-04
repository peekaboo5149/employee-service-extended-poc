type ToastType = 'SUCCESS' | 'ERROR' | 'WARN'

export interface ToastOptions {
  message: string
  type?: ToastType
  duration?: number
}

let listeners: ((toast: ToastOptions) => void)[] = []

export const toast = (options: ToastOptions) => {
  listeners.forEach((l) => l(options))
}

export const toastSubscribe = (listener: (toast: ToastOptions) => void) => {
  listeners.push(listener)

  // unsubscriber
  return () => {
    listeners = listeners.filter((l) => l !== listener)
  }
}
