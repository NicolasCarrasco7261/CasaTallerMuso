import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Register.css';
import Navbar from "../../componentes/Navbar";

const Register = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    rut: '',
    correo: '',
    password: '' // Usamos 'password' en el estado local del form por comodidad
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { nombre, apellido, rut, correo, password } = formData;

    // 1. Validación básica de campos vacíos
    if (!nombre || !apellido || !rut || !correo || !password) {
      alert("Por favor, completa todos los campos.");
      return;
    }

    // 2. Validación de formato de RUT chileno
    const rutRegex = /^[0-9]{1,2}\.[0-9]{3}\.[0-9]{3}-[0-9kK]{1}$/;
    if (!rutRegex.test(rut)) {
      alert("Formato de RUT inválido. Usa el formato: 12.345.678-9");
      return;
    }

    // 3. Mapeo de datos para que coincidan con la Entidad Usuario.java
    const nuevoUsuario = {
      nombre: nombre,
      apellido: apellido,
      rut: rut,
      correo: correo,
      contrasenia: password, // Mapeo de 'password' a 'contrasenia' (como en tu Java)
      activo: true,
      categoriaU: {
        id: 1 // ID 1 corresponde a la categoría 'usuario' en tu base de datos
      }
    };

    try {
      // 4. Petición Fetch al Backend
      // Usamos la URL base definida en tu UsuarioRestControllers
      const response = await fetch("http://localhost:8080/api/usuarios", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(nuevoUsuario)
      });

      if (response.ok) {
        alert("¡Registro exitoso! Ya puedes ingresar a la comunidad Muso.");
        navigate("/login"); // Redirección al login tras éxito
      } else {
        const errorMsg = await response.text();
        console.error("Error del servidor:", errorMsg);
        alert("Hubo un problema al crear la cuenta. Revisa los datos.");
      }
    } catch (error) {
      console.error("Error de conexión:", error);
      alert("No se pudo establecer conexión con el servidor. Verifica que el Backend esté corriendo.");
    }
  };

  return (
    <div className="min-vh-100 bg-custom-cream">
      {/* NAVBAR */}
      <Navbar />

      {/* CONTENIDO PRINCIPAL */}
      <main className="container py-5">
        <div className="row justify-content-center">
          <div className="col-12 col-md-10 col-lg-8 bg-white shadow-sm d-flex flex-column flex-md-row p-0 register-card">
            
            {/* Lateral Izquierdo Informativo */}
            <div className="col-md-5 bg-brand p-5 text-white d-flex flex-column justify-content-center">
              <span className="text-uppercase tracking-widest small mb-2 d-block opacity-75">Comunidad Muso</span>
              <p className="fw-light opacity-75">
                Crea tu perfil para inscribirte en talleres, acceder a la biblioteca digital y conectar con otros artesanos.
              </p>
            </div>

            {/* Formulario de Registro */}
            <div className="col-md-7 p-5">
              <h3 className="fw-bold mb-4 text-dark">Crear cuenta</h3>
              
              <form className="register-form" onSubmit={handleSubmit}>
                <div className="row g-3">
                  <div className="col-md-6 mb-3">
                    <label className="register-label">Nombre</label>
                    <input 
                      type="text" 
                      name="nombre" 
                      className="form-control register-input" 
                      placeholder="Ej: Camilo" 
                      onChange={handleChange} 
                      required 
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label className="register-label">Apellido</label>
                    <input 
                      type="text" 
                      name="apellido" 
                      className="form-control register-input" 
                      placeholder="Ej: Pérez" 
                      onChange={handleChange} 
                      required 
                    />
                  </div>
                </div>

                <div className="mb-3">
                  <label className="register-label">RUT (12.345.678-9)</label>
                  <input 
                    type="text" 
                    name="rut" 
                    className="form-control register-input" 
                    placeholder="12.345.678-9" 
                    onChange={handleChange} 
                    required 
                  />
                </div>

                <div className="mb-3">
                  <label className="register-label">Correo electrónico</label>
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
                  <label className="register-label">Contraseña</label>
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
                  Finalizar registro
                </button>
              </form>

              <Link to="/" className="text-decoration-none text-muted small">
                ← Volver al inicio
              </Link>
            </div>

          </div>
        </div>
      </main>
    </div>
  );
};

export default Register;