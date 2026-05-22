import { Routes, Route, RouterProvider } from 'react-router-dom'
import Home from './paginas/Home/Home'
import Register from './paginas/Register/Register'
import Login from './paginas/Login/Login'
import Cursos from './paginas/Cursos/Cursos'
// Importamos los nuevos componentes
import Dashboard from './paginas/Dashboard/Dashboard'
import TuActividad from './paginas/TuActividad/TuActividad'
import ProtectedRoute from './componentes/ProtectedRoute/ProtectedRoute' // Asegúrate de haber creado este archivo

import "./App.css"
import { AuthProvider } from './utils/AuthProvider'
import router from './utils/router'

import './utils/api'

function App() {
  return (
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  )
}

export default App;