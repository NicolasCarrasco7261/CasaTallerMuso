import { Link } from "react-router-dom";

export default function Footer() {
  return (
    <footer className="navbar navbar-expand-lg pt-4 pb-5 bg-transparent border-top mt-auto">
      <div className="container" style={{ maxWidth: "1100px" }}>
        <div className="container d-flex align-items-center justify-content-between">
          <Link
            className="navbar-brand fw-bold fs-4 text-dark p-0 m-0"
            to="/"
            style={{ letterSpacing: "-1px" }}
          >
            <span className="fw-semibold text-muted">Casa taller</span> Mus
            <span className="text-brand">o</span>
          </Link>

          <div className="d-flex flex-column gap-2 align-items-end">
            <div className="d-flex gap-4">
              <Link to="/cursos" className="text-brand">
                Cursos
              </Link>
              <Link to="/eventos" className="text-brand">
                Eventos
              </Link>
            </div>
            <p className="fs-6 text-muted fw-semibold">
              <small>contacto@casatallermuso.cl — Santiago, Chile</small>
            </p>
          </div>
        </div>
      </div>
    </footer>
  );
}
