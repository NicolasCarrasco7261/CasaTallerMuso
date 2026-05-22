import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom'; // Importamos useNavigate
import './Login.css';
import Navbar from "../../componentes/Navbar/Navbar";
import { useAuth } from '../../utils/AuthProvider';

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [credentials, setCredentials] = useState({
    correo: '',
    password: ''
  });

  const handleChange = (e) => {
    setCredentials({
      ...credentials,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const loginData = {
      correo: credentials.correo,
      clave: credentials.password 
    };

    try {
      const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(loginData)
      });

      if (response.ok) {
        const res = await response.json();
        login(res.token);
        navigate("/"); 
      } else {
        alert("Correo o contraseña incorrectos. Inténtalo de nuevo.");
      }
    } catch (error) {
      console.error("Error al conectar:", error);
      alert("No se pudo conectar con el servidor.");
    }
  };

  return (
    <div className="col-12 row-12 row justify-content-center">
      <div className="col-12 col-md-10 col-lg-8 bg-white shadow-sm d-flex flex-column flex-md-row p-0 login-card">
        
        <div className="col-md-5 bg-brand p-5 text-white d-flex flex-column justify-content-center">
          <p className="fw-light opacity-75">
            Ingresa a tu cuenta para continuar con tus cursos y proyectos en la comunidad.
          </p>
        </div>

        <div className="col-md-7 p-5">
          <h3 className="fw-bold mb-4 text-dark">Iniciar Sesión</h3>
          
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="login-label">Correo electrónico</label>
              <input 
                type="email" 
                name="correo" 
                className="form-control register-input" 
                placeholder="correo@ejemplo.com" 
                onChange={handleChange}
                required 
              />
            </div>

            <div className="mb-4">
              <div className="d-flex justify-content-between">
                <label className="login-label">Contraseña</label>
                <a href="#" className="text-brand small text-decoration-none fw-bold">¿Olvidaste tu contraseña?</a>
              </div>
              <input 
                type="password" 
                name="password" 
                className="form-control register-input" 
                placeholder="••••••••" 
                onChange={handleChange}
                required 
              />
            </div>

            <button type="submit" className="btn btn-brand w-100 py-3 fw-bold mb-4">
              Ingresar
            </button>
          </form>

          <Link to="/" className="text-decoration-none text-muted small d-flex align-items-center gap-2 mt-3">
            ← Volver al inicio
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Login;