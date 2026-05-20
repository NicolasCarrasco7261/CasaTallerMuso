import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";

const Navbar = () => {
  const navigate = useNavigate();

  const [usuario, setUsuario] = useState(() => {
    const savedUser = localStorage.getItem("usuario");
    if (savedUser) {
      try {
        return JSON.parse(savedUser);
      } catch (e) {
        console.error("Error parseando usuario", e);
        return null;
      }
    }
    return null;
  });

  const handleLogout = () => {
    localStorage.removeItem("usuario");
    setUsuario(null);
    navigate("/");
  };

  return (
    <nav className="navbar navbar-expand-lg py-4 bg-transparent">
      <div className="container" style={{ maxWidth: "1100px" }}>
        <div className="container d-flex align-items-center justify-content-between">
          <Link className="navbar-brand fw-bold fs-4 text-dark p-0 m-0" to="/" style={{ letterSpacing: "-1px" }}>
            Mus<span className="text-brand">o</span>
          </Link>

          <div className="d-flex align-items-center gap-4">
            {usuario ? (
              <>
                

                <span className="text-dark fw-medium small">
                  Hola, <span className="text-brand">{usuario.nombre}</span>
                </span>


                {/* --- ENLACES DINÁMICOS POR ROL --- */}
                {usuario.categoriaU?.nombre === "administrador" ? (
                  <Link to="/dashboard" className="text-decoration-none text-dark fw-bold small">
                    Dashboard
                  </Link>
                ) : (
                  <Link to="/tuActividad" className="text-decoration-none text-dark fw-bold small">
                    Tú actividad
                  </Link>
                )}
                
                <button onClick={handleLogout} className="btn btn-brand rounded-pill px-4 shadow-sm">
                  Cerrar Sesión
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="btn btn-link text-decoration-none text-dark fw-medium p-0 small">
                  Ingresar
                </Link>
                <Link to="/register" className="btn btn-brand-outline rounded-pill px-4">
                  Crear Cuenta
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;