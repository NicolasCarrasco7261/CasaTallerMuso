import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Register.css';
import Navbar from "../../componentes/Navbar/Navbar";
import { useAuth } from '../../utils/AuthProvider';

export default function Register() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    correo: '',
    clave: '',
    genero: null,
    fechaNacimiento: null,
    numeroTelefonico: null,
    direccion: null,
    region: null
  });
  const [fields, setFields] = useState({
    genero: [],
    region: []
  });

  useEffect(() => {
    const fetchFields = async () => {
      const res = await fetch("/api/auth/signup");
      if (res.ok) {
        const data = await res.json();
        setFields(data);
      } else {
        alert("Error del servidor. Intente de nuevo más tarde.");
        navigate("/");
      }
    };
    fetchFields();
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { nombre, apellido, correo, clave, genero, fechaNacimiento, direccion, region } = formData;

    if (!nombre || !apellido || !correo || !clave) {
      alert("Por favor, completa todos los campos requeridos.");
      return;
    }

    if (direccion || region) {
      if (!direccion) {
        alert("Por favor ingrese su dirección o elimine su región.");
        return;
      }
      
      if (!region) {
        alert("Por favor ingrese su región o elimine su dirección.")
        return;
      }
    }

    const detalle = {
      fechaNacimiento,
      genero
    };

    if (direccion && region) {
      detalle.ubicacionUsuario = {
        direccion,
        region
      };
    }

    const nuevoUsuario = {
      credenciales: {
        correo,
        clave
      },
      perfil: {
        nombre,
        apellido,
        detalle
      }
    };

    try {
      const response = await fetch("/api/auth/signup", {
        method: "POST",
        body: JSON.stringify(nuevoUsuario)
      });

      if (response.ok) {
        const data = await response.json();
        login(data.token);
        alert("¡Registro exitoso! Bienvenidx a tu comunidad Muso.");
        navigate("/");
      } else {
        console.error("Error del servidor:", response.status);
        alert("Hubo un problema al crear la cuenta. Revisa los datos.");
      }
    } catch (error) {
      console.error("Error de conexión:", error);
      alert("No se pudo establecer conexión con el servidor.");
    }
  };

  return (
    <div className="row justify-content-center">
      <div className="col-12 col-md-10 col-lg-8 bg-white shadow-sm d-flex flex-column flex-md-row p-0 register-card">
        
        <div className="col-md-5 bg-brand p-5 text-white d-flex flex-column justify-content-center">
          <span className="text-uppercase tracking-widest small mb-2 d-block opacity-75">Comunidad Muso</span>
          <p className="fw-light opacity-75">
            Crea tu perfil para inscribirte en talleres, acceder a la biblioteca digital y conectar con otros artesanos.
          </p>
        </div>

        <div className="col-md-7 p-5">
          <h3 className="fw-bold mb-4 text-dark">Crear cuenta</h3>
          
          <form className="register-form" onSubmit={handleSubmit}>
            <div className="row g-3">
              <div className="col-md-6 mb-3">
                <label className="register-label">Nombre<span className='text-danger'>*</span></label>
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
                <label className="register-label">Apellido<span className='text-danger'>*</span></label>
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
              <label className="register-label">Correo electrónico<span className='text-danger'>*</span></label>
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
              <label className="register-label">Contraseña<span className='text-danger'>*</span></label>
              <input 
                type="password" 
                name="clave" 
                className="form-control register-input" 
                placeholder="Mín. 8 caracteres, letras y números" 
                pattern="^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9!@#$%&*\(\)_+=\{\}:;<>,.?~ \[\]^.\-]{8,64}$"
                onChange={handleChange} 
                required 
              />
            </div>

            <div className="mb-4">
              <label className="register-label">Género</label>
              <select name='genero' className='form-control register-input' onChange={handleChange}>
                <option value="" hidden>«Seleccionar Género»</option>
                {
                  fields.genero.map((g, i) => (
                    <option key={i} value={g}>{g}</option>
                  ))
                }
              </select>
            </div>

            <div className="row g-3">
              <div className="col-md-6 mb-3">
                <label className="register-label">Fecha de Nacimiento</label>
                <input 
                  type="date" 
                  name="fechaNacimiento" 
                  className="form-control register-input" 
                  onChange={handleChange} 
                />
              </div>
              <div className="col-md-6 mb-3">
                <label className="register-label">Número Telefónico</label>
                <input 
                  type="tel" 
                  name="numeroTelefonico" 
                  className="form-control register-input" 
                  placeholder="+56912345678" 
                  pattern='\+?[1-9]\d{1,14}'
                  onChange={handleChange} 
                />
              </div>
            </div>

            <div className="row g-3">
              <div className="col-md-6 mb-3">
                <label className="register-label">Dirección</label>
                <input 
                  type="text" 
                  name="direccion" 
                  className="form-control register-input" 
                  onChange={handleChange} 
                />
              </div>
              <div className="col-md-6 mb-3">
                <label className="register-label">Región</label>
                <select name='region' className='form-control register-input' onChange={handleChange}>
                  <option value="" hidden>«Seleccionar Región»</option>
                  {
                    fields.region.map((r, i) => (
                      <option key={i} value={r}>{r}</option>
                    ))
                  }
                </select>
              </div>
            </div>

            <button type="submit" className="btn btn-brand w-100 py-3 fw-bold my-4">
              Finalizar registro
            </button>
          </form>

          <Link to="/" className="text-decoration-none text-muted small">
            ← Volver al inicio
          </Link>
        </div>

      </div>
    </div>
  );
};