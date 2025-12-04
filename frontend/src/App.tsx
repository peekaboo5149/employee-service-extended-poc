import { Navigate, Route, Routes } from 'react-router-dom'
import BaseAuthView from './view/auth/base-view'

const App = () => {
  return (
    <Routes>
      <Route path='/' element={<Navigate to='/auth' replace />} />
      <Route path='/auth' element={<BaseAuthView />} />
    </Routes>
  )
}

export default App
