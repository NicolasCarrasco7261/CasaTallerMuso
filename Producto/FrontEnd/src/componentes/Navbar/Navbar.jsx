import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "../../utils/AuthProvider";

export default function Navbar() {
  return (
    <nav className="navbar navbar-expand-lg py-4 bg-transparent">
      <div className="container" style={{ maxWidth: "1100px" }}>
        <div className="container d-flex align-items-center justify-content-between">
          <Link className="navbar-brand fw-bold fs-4 text-dark p-0 m-0" to="/" style={{ letterSpacing: "-1px" }}>
            Mus<span className="text-brand">o</span>
          </Link>

          <div className="d-flex align-items-center gap-4">
            <NavbarContent />
          </div>
        </div>
      </div>
    </nav>
  )
}

function NavbarContent() {
  const { user, isLoggedIn, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  if (!isLoggedIn()) {
    return ( 
      <>
        <Link to="/auth/login" className="btn btn-link text-decoration-none text-dark fw-medium p-0 small">
          Ingresar
        </Link>
        <Link to="/auth/signup" className="btn btn-brand-outline rounded-pill px-4">
          Crear Cuenta
        </Link>
      </>
    )
  }

  if (!user) {
    return <></>
  }

  return <>
    <span className="text-dark fw-medium small">
      Hola, <span className="text-brand">{user.nombre}</span>
    </span>


    {user.rol?.tipoRol === "Administrador" ? (
      <Link to="/me/dashboard" className="btn btn-brand-outline rounded-pill px-4">
        Dashboard
      </Link>
    ) : (
      <Link to="/me" className="btn btn-brand-outline rounded-pill px-4">
        Tú actividad
      </Link>
    )}
    
    <button onClick={handleLogout} className="btn btn-brand rounded-pill px-4 shadow-sm">
      Cerrar Sesión
    </button>
  </>
};