import { ChevronLeft } from "lucide-react";
import { Link } from "react-router-dom";

export default function ActividadPage({
  backLink,
  backLabel,
  actividad: {
    imagenUrl,
    nombre,
    descripcion,
    cuposRestantes,
    precio,
  },
  inscrito,
  handleInscripcion,
  children
}) {
  return (
    <div className="d-flex flex-column gap-2">
      <Link
        to={backLink}
        className="d-flex m-0 p-1 btn btn-brand-outline rounded-pill align-items-center"
        style={{
          width: "fit-content",
          height: "fit-content"
        }}
      >
        <ChevronLeft size={24} />
        <span className="fw-semibold ps-1 pe-2">{backLabel}</span>
      </Link>
      <div className="d-flex flex-column col-12 h-100 bg-light border shadow-sm rounded-3 overflow-hidden">
        <img className="border-bottom" src={imagenUrl} style={{ width: "100%", height: "20em", objectFit: "cover" }} />
        <section className="p-4 d-flex flex-column gap-4">
          <div className="d-flex flex-row justify-content-between align-items-start">
            <div className="d-flex flex-column text-start gap-2">
              <h1 className="text-dark text-start m-0 fs-1 fw-semibold">{nombre}</h1>
            </div>
            <div className="d-flex gap-3 align-items-center">
              <h3 className="m-0 text-brand fs-4 fw-semibold">${Number(precio).toLocaleString("es-CL")}</h3>
              <button onClick={handleInscripcion} className="btn btn-brand fs-5 px-4" disabled={inscrito}>{inscrito ? "Ya inscrito" : "Inscribir"}</button>
            </div>
          </div>
          <div className="py-4 d-flex flex-column text-start gap-2 border-top border-bottom">
            <p className="text-muted m-0 fs-5">{descripcion}</p>
            <h5 className="m-0 text-muted fs-6">» {cuposRestantes} cupos disponibles.</h5>
          </div>
          <div className="d-flex flex-column gap-3 align-items-center">
            <div className="d-flex border-dark border-top border-bottom py-2 px-5">
              <h3 className="text-dark m-0 fs-3 fw-semibold">Horarios</h3>
            </div>
            <div className="d-flex gap-3">
              {children}
            </div>
          </div>
        </section>
      </div>
    </div>
  )
}