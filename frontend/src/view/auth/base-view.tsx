import { AuthContextAwareView, AuthToggleProvider } from './context'

const BaseAuthView = () => {
  return (
    <AuthToggleProvider>
      <div className='flex min-h-svh w-full items-center justify-center p-6 md:p-10'>
        <div className='w-full max-w-sm'>
          <AuthContextAwareView />
        </div>
      </div>
    </AuthToggleProvider>
  )
}

export default BaseAuthView
