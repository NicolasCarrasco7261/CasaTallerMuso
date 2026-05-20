import { Routes, Route } from 'react-router-dom'
import Home from './paginas/Home/Home'
import Register from './paginas/Register/Register'
import Login from './paginas/Login/Login'
import Cursos from './paginas/Cursos/Cursos'
// Importamos los nuevos componentes
import Dashboard from './paginas/Dashboard/Dashboard'
import TuActividad from './paginas/TuActividad/TuActividad'
import ProtectedRoute from './componentes/ProtectedRoute' // Asegúrate de haber creado este archivo

function App() {
  return (
    <Routes>
      {/* Rutas Públicas */}
      <Route path="/" element={<Home />} />
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
      <Route path="/cursos" element={<Cursos />} />

      {/* Rutas Privadas Protegidas */}
      <Route 
        path="/dashboard" 
        element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        } 
      />

      <Route 
        path="/tuActividad" 
        element={
          <ProtectedRoute>
            <TuActividad />
          </ProtectedRoute>
        } 
      />
    </Routes>
  )
}

export default App;