import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { createCursoHorario, createCursoView } from "../../objetos/Curso";
import { useAuth } from "../../utils/AuthProvider";
import { ChevronLeft } from "lucide-react";

export default function CursoPage() {
  const { id } = useParams();
  const { isLoggedIn } = useAuth();
  const navigate = useNavigate();
  const [curso, setCurso] = useState(null);
  const [inscrito, setInscrito] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setCurso(null);
    setError(null);
    setLoading(true);

    const fetchCurso = async () => {
      try {
        const res = await fetch(`/api/cursos/${id}`);
        if (res.ok) {
          const data = await res.json();
          const cursoView = createCursoView(data);
          setCurso(cursoView);
        }
      } catch (err) {
        setError(err.message);
      }
      setLoading(false);
    };

    fetchCurso();
  }, [id]);

  useEffect(() => {
    setInscrito(false);

    const checkInscrito = async() => {
      try {
        const res = await fetch(`/api/cursos/${id}/i`);
        if (res.ok) {
          const data = await res.json();
          const inscrito = data.inscrito;
          setInscrito(inscrito);
        }
      } catch (err) {
        console.error(`Error al verificar inscripción: ${err}`);
      }
    }

    if (isLoggedIn) {
      checkInscrito();
    }
  }, [id]);

  const handleInscripcion = async () => {
    if (!isLoggedIn()) {
      navigate("/auth/login");
    }
    if (inscrito) return;
    setInscrito(true); // predicción
    try {
      const res = await fetch (`/api/cursos/${id}/i`, { method: "POST" });
      if (res.ok) {
        setInscrito(true);
      } else {
        setInscrito(false);
      }
    } catch (err) {
      console.error(`Error al inscribir curso: ${err}`);
      setInscrito(false);
    }
  };

  if (error) {
    return <div className="col-12 text-center py-5">
      <h3 className="text-danger">{error}</h3>
    </div>
  }

  if (loading) {
    return <div className="col-12 text-center py-5">
      <h3 className="text-muted">Cargando...</h3>
    </div>
  }

  if (!curso) {
    return <div className="col-12 text-center py-5">
      <h3 className="text-muted">Nada que mostrar 😞</h3>
    </div>
  }

  return (
    <div className="d-flex flex-column gap-2">
      <Link
        to="/cursos"
        className="d-flex m-0 p-1 btn btn-brand-outline rounded-pill align-items-center"
        style={{
          width: "fit-content",
          height: "fit-content"
        }}
      >
        <ChevronLeft size={24} />
        <span className="fw-semibold ps-1 pe-2">Cursos</span>
      </Link>
      <div className="d-flex flex-column col-12 h-100 bg-light border shadow-sm rounded-3 overflow-hidden">
        <img className="border-bottom" src={curso.imagenUrl} style={{ width: "100%", height: "20em", objectFit: "cover" }} />
        <section className="p-4 d-flex flex-column gap-4">
          <div className="d-flex flex-row justify-content-between align-items-start">
            <div className="d-flex flex-column text-start gap-2">
              <h1 className="text-dark text-start m-0 fs-1 fw-semibold">{curso.nombre}</h1>
            </div>
            <div className="d-flex gap-3 align-items-center">
              <h3 className="m-0 text-brand fs-4 fw-semibold">${Number(curso.precio).toLocaleString("es-CL")}</h3>
              <button onClick={handleInscripcion} className="btn btn-brand fs-5 px-4" disabled={inscrito}>{inscrito ? "Ya inscrito" : "Inscribir"}</button>
            </div>
          </div>
          <div className="py-4 d-flex flex-column text-start gap-2 border-top border-bottom">
            <p className="text-muted m-0 fs-5">{curso.descripcion}</p>
            <h5 className="m-0 text-muted fs-6">» {curso.cuposRestantes} cupos disponibles.</h5>
          </div>
          <div className="d-flex flex-column gap-3 align-items-center">
            <div className="d-flex border-dark border-top border-bottom py-2 px-5">
              <h3 className="text-dark m-0 fs-3 fw-semibold">Horarios</h3>
            </div>
            <div className="d-flex gap-3">
              {
                curso.horarios.map((horario, i) => (
                  <div key={i} className="bg-light border rounded-2 p-2 shadow-sm lift-on-hover">
                    <h3 className="fw-semibold fs-5">
                      {horario.diaDeSemana}
                    </h3>
                    <div style={{ minWidth: "7em" }}>
                      <p className="text-muted fs-6">{horario.horaDesde}</p>
                      <div className="w-100 border-bottom" />
                      <p className="text-muted fs-6">{horario.horaHasta}</p>
                    </div>
                  </div>
                ))
              }
            </div>
          </div>
        </section>
      </div>
    </div>
  )
}