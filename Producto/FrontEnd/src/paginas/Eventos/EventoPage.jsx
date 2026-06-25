import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createEventoView } from "../../objetos/Evento";
import { useAuth } from "../../utils/AuthProvider";
import ActividadPage from "../../componentes/Actividad/ActividadPage";
import ActividadAdminCard from "../../componentes/Actividad/ActividadAdminCard";
import ActividadErrorPage from "../../componentes/Actividad/ActividadErrorPage";
import ActividadLoadingPage from "../../componentes/Actividad/ActividadLoadingPage";
import ActividadNotFoundPage from "../../componentes/Actividad/ActividadNotFoundPage";
import EventoHorarioCard from "../../componentes/Evento/EventoHorarioCard";

export default function EventoPage() {
  const { id } = useParams();
  const { user, isLoggedIn } = useAuth();
  const navigate = useNavigate();
  const [evento, setEvento] = useState(null);
  const [inscrito, setInscrito] = useState(false);
  const [activo, setActivo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const isAdmin = user?.rol?.tipoRol === "Administrador";

  useEffect(() => {
    let cancelled = false;

    Promise.resolve().then(async () => {
      if (cancelled) return;

      setEvento(null);
      setError(null);
      setLoading(true);

      try {
        const res = await fetch(
          isAdmin ? `/api/eventos/${id}/a` : `/api/eventos/${id}`,
        );
        if (res.ok && !cancelled) {
          const data = await res.json();
          const eventoView = createEventoView(data);
          setEvento(eventoView);
          if (isAdmin) {
            setActivo(data.activo);
          }
        }
      } catch (err) {
        if (!cancelled) {
          setError(err.message);
        }
      }

      if (!cancelled) {
        setLoading(false);
      }
    });

    return () => {
      cancelled = true;
    };
  }, [id, isAdmin]);

  useEffect(() => {
    const checkInscrito = async () => {
      try {
        const res = await fetch(`/api/eventos/${id}/i`);
        if (res.ok) {
          const data = await res.json();
          const inscrito = data.inscrito;
          setInscrito(inscrito);
        }
      } catch (err) {
        console.error(`Error al verificar inscripción: ${err}`);
      }
    };

    if (isLoggedIn()) {
      checkInscrito();
    }
  }, [id, isLoggedIn]);

  const handleInscripcion = async () => {
    if (!isLoggedIn()) {
      navigate("/auth/login");
      return;
    }
    if (inscrito) return;
    setInscrito(true); // predicción
    try {
      const res = await fetch(`/api/eventos/${id}/i`, { method: "POST" });
      if (res.ok) {
        setInscrito(true);
      } else {
        setInscrito(false);
      }
    } catch (err) {
      console.error(`Error al inscribir evento: ${err}`);
      setInscrito(false);
    }
  };

  if (error) {
    return <ActividadErrorPage error={error} />;
  }

  if (loading) {
    return <ActividadLoadingPage />;
  }

  if (!evento) {
    return <ActividadNotFoundPage />;
  }

  return (
    <div className="d-flex flex-column gap-4">
      <ActividadPage
        backLink="/eventos"
        backLabel="Eventos"
        actividad={evento}
        inscrito={inscrito}
        handleInscripcion={handleInscripcion}
      >
        {evento.horarios.map((horario, i) => (
          <EventoHorarioCard
            key={i}
            fecha={horario.fecha}
            hora={horario.hora}
          />
        ))}
      </ActividadPage>
      <ActividadAdminCard
        apiBasePath="/api/eventos"
        actividadId={id}
        tipoLabel="evento"
        activo={activo}
        setActivo={setActivo}
      />
    </div>
  );
}
