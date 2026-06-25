import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createEventoView } from "../../objetos/Evento";
import { useAuth } from "../../utils/AuthProvider";
import ActividadPage from "../../componentes/Actividad/ActividadPage";
import ActividadErrorPage from "../../componentes/Actividad/ActividadErrorPage";
import ActividadLoadingPage from "../../componentes/Actividad/ActividadLoadingPage";
import ActividadNotFoundPage from "../../componentes/Actividad/ActividadNotFoundPage";
import EventoHorarioCard from "../../componentes/Evento/EventoHorarioCard";

export default function EventoPage() {
  const { id } = useParams();
  const { isLoggedIn } = useAuth();
  const navigate = useNavigate();
  const [evento, setEvento] = useState(null);
  const [inscrito, setInscrito] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setEvento(null);
    setError(null);
    setLoading(true);

    const fetchEvento = async () => {
      try {
        const res = await fetch(`/api/eventos/${id}`);
        if (res.ok) {
          const data = await res.json();
          const eventoView = createEventoView(data);
          setEvento(eventoView);
        }
      } catch (err) {
        setError(err.message);
      }
      setLoading(false);
    };

    fetchEvento();
  }, [id]);

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
    <ActividadPage
      backLink="/eventos"
      backLabel="Eventos"
      actividad={evento}
      inscrito={inscrito}
      handleInscripcion={handleInscripcion}
    >
      {evento.horarios.map((horario, i) => (
        <EventoHorarioCard key={i} fecha={horario.fecha} hora={horario.hora} />
      ))}
    </ActividadPage>
  );
}
