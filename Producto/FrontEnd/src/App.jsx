import { Routes, Route } from 'react-router-dom'
import Home from './Home'
import Register from './Register'
import Login from './Login'
import Cursos from './Cursos'
// Importamos los nuevos componentes
import Dashboard from './Dashboard'
import TuActividad from './TuActividad'
import ProtectedRoute from './ProtectedRoute' // Asegúrate de haber creado este archivo

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