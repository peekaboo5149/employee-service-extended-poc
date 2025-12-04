import { createContext, useContext, useState } from 'react'
import { LoginView, RegisterView } from '../components'

type AuthMode = 'login' | 'register'

interface AuthToggleContextType {
  mode: AuthMode
  toggle: () => void
  setMode: (m: AuthMode) => void
}
const AuthToggleContext = createContext<AuthToggleContextType | null>(null)

export const AuthToggleProvider = ({
  children,
}: {
  children: React.ReactNode
}) => {
  const [mode, setMode] = useState<AuthMode>('login')

  const toggle = () => {
    setMode((prev) => (prev === 'login' ? 'register' : 'login'))
  }

  return (
    <AuthToggleContext.Provider value={{ mode, toggle, setMode }}>
      {children}
    </AuthToggleContext.Provider>
  )
}

export const useAuthToggle = () => {
  const ctx = useContext(AuthToggleContext)
  if (!ctx)
    throw new Error('useAuthToggle must be used inside AuthToggleProvider')
  return ctx
}

export const AuthContextAwareView = () => {
  const { mode } = useAuthToggle()

  return <>{mode === 'login' ? <LoginView /> : <RegisterView />}</>
}
