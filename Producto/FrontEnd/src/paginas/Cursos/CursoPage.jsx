import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { createCursoHorario, createCursoView } from "../../objetos/Curso";
import { useAuth } from "../../utils/AuthProvider";
import { ChevronLeft } from "lucide-react";
import ActividadPage from "../../componentes/Actividad/ActividadPage";
import ActividadErrorPage from "../../componentes/Actividad/ActividadErrorPage";
import ActividadLoadingPage from "../../componentes/Actividad/ActividadLoadingPage";
import ActividadNotFoundPage from "../../componentes/Actividad/ActividadNotFoundPage";
import CursoHorarioCard from "../../componentes/Curso/CursoHorarioCard";

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
    return <ActividadErrorPage error={error} />
  }

  if (loading) {
    return <ActividadLoadingPage />
  }

  if (!curso) {
    return <ActividadNotFoundPage />
  }

  return (
    <ActividadPage
      backLink="/cursos"
      backLabel="Cursos"
      actividad={curso}
      inscrito={inscrito}
      handleInscripcion={handleInscripcion}
    >
      {
        curso.horarios.map((horario, i) => (
          <CursoHorarioCard key={i} diaDeSemana={horario.diaDeSemana} horaDesde={horario.horaDesde} horaHasta={horario.horaHasta} />
        ))
      }
    </ActividadPage>
  )
}