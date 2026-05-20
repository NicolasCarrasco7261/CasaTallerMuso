import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom'; // Importamos useNavigate
import './Login.css';
import Navbar from "../../componentes/Navbar";

const Login = () => {
  const navigate = useNavigate();
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
    
    // 1. Preparar el objeto para el Backend
    // Importante: 'contrasenia' debe llamarse igual que en tu entidad Usuario de Java
    const loginData = {
      correo: credentials.correo,
      contrasenia: credentials.password 
    };

    try {
      // 2. Petición al endpoint de login
      const response = await fetch("http://localhost:8080/api/usuarios/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(loginData)
      });

      if (response.ok) {
        const usuarioLogueado = await response.json();
        
        // 3. Guardar la sesión en el navegador (opcional pero recomendado)
        localStorage.setItem("usuario", JSON.stringify(usuarioLogueado));
        
        alert(`¡Bienvenido de nuevo, ${usuarioLogueado.nombre}!`);
        
        // 4. Redirigir al inicio o al catálogo de cursos
        navigate("/"); 
        // OPCIONAL: Si el Navbar no se actualiza inmediatamente al llegar al Home,
        // puedes usar esto para forzar la actualización:
        window.location.reload();
      } else {
        // Si el backend devuelve 401 (Unauthorized)
        alert("Correo o contraseña incorrectos. Inténtalo de nuevo.");
      }
    } catch (error) {
      console.error("Error al conectar:", error);
      alert("No se pudo conectar con el servidor.");
    }
  };

  return (
    <div className="min-vh-100 bg-custom-cream">
      {/* NAVBAR (Igual a la anterior) */}
      <Navbar />

      {/* SECCIÓN DE LOGIN */}
      <main className="container py-5">
        <div className="row justify-content-center">
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
      </main>
    </div>
  );
};

export default Login;