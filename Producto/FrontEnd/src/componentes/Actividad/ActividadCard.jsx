import { Link, useNavigate } from "react-router-dom"

export default function ActividadCard({ href, actividad: { id, nombre, imagenUrl, precio, cupos, cuposRestantes } }) {
  const navigate = useNavigate();
  return (
    <div className="col-12 col-md-6 col-lg-4" key={id}>
      <div className="curso-card bg-white shadow-sm h-100 border-0 shadow-hover">
      <div className="curso-image-container position-relative overflow-hidden">
        <img
        src={imagenUrl}
        alt={nombre}
        className="curso-img w-100"
        />
        {
          cuposRestantes <= Math.ceil(cupos * 0.34) &&
          <span
            className="badge shadow-sm bg-danger text-light p-1 ps-3 pb-2 fs-6"
            style={{
              position: "absolute",
              top: "0",
              right: "0",
              borderBottomLeftRadius: "var(--bs-border-radius-pill)"
            }}
          >
            { cupos > 0 ? "¡Pocos cupos!" : "Agotado" }
          </span>
        }
      </div>

      <div className="p-4 text-center">
        <h4 className="fw-bold mb-3">{nombre}</h4>
        <div className="d-flex justify-content-between align-items-center pt-3 border-top mt-auto">
        <span className="fw-bold text-brand fs-5">
          ${Number(precio).toLocaleString("es-CL")}
        </span>
        <Link to={href}
          className={`btn ${cuposRestantes > 0 ? 'btn-brand' : 'btn-secondary'} rounded-2 px-3 py-2 fw-bold small`}
          disabled={cuposRestantes <= 0}
        >
          {cuposRestantes > 0 ? "Inscribirse" : "Sin Cupos"}
        </Link>
        </div>
        </div>
      </div>
    </div>
  )
}