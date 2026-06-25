import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { useAuth } from "../../utils/AuthProvider";
import ActividadEditorForm from "../../componentes/Actividad/ActividadEditorForm";
import ActividadErrorPage from "../../componentes/Actividad/ActividadErrorPage";
import ActividadLoadingPage from "../../componentes/Actividad/ActividadLoadingPage";
import ActividadNotFoundPage from "../../componentes/Actividad/ActividadNotFoundPage";

function contenidoToTextarea(value) {
  return (value ?? "").replaceAll("\\n", "\n");
}

function contenidoToApi(value) {
  return (value ?? "").replace(/\r\n/g, "\n").replace(/\n/g, "\\n");
}

function mapEventoAdminToForm(data) {
  return {
    nombre: data.nombre ?? "",
    descripcion: data.descripcion ?? "",
    contenido: contenidoToTextarea(data.contenido),
    imagenStorageKey: data.imagenStorageKey ?? "",
    precio: String(data.precio ?? ""),
    cupos: String(data.cupos ?? ""),
    activo: data.activo ?? true,
    horarios:
      data.horarios?.length > 0
        ? data.horarios.map((horario) => ({
            fecha: (horario.fecha ?? "").slice(0, 10),
            hora: (horario.hora ?? "19:00:00").slice(0, 5),
          }))
        : [{ fecha: "", hora: "19:00" }],
  };
}

export default function EventoEditPage() {
  const { id } = useParams();
  const { user } = useAuth();
  const [form, setForm] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const isAdmin = user?.rol?.tipoRol === "Administrador";

  useEffect(() => {
    if (!isAdmin) return;

    let cancelled = false;

    Promise.resolve().then(async () => {
      if (cancelled) return;

      setLoading(true);
      setError("");
      setSuccess("");
      setForm(null);

      try {
        const res = await fetch(`/api/eventos/${id}/a`);
        if (res.status === 404) {
          if (!cancelled) {
            setForm(null);
            setLoading(false);
          }
          return;
        }
        if (!res.ok) {
          throw new Error("No se pudo cargar el evento.");
        }

        const data = await res.json();
        if (!cancelled) {
          setForm(mapEventoAdminToForm(data));
        }
      } catch (err) {
        console.error(err);
        if (!cancelled) {
          setError("No se pudo cargar el evento para editar.");
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

  const handleSubmit = async (currentForm) => {
    setSubmitting(true);
    setError("");
    setSuccess("");

    try {
      const response = await fetch(`/api/eventos/${id}`, {
        method: "PATCH",
        body: JSON.stringify({
          ...currentForm,
          contenido: contenidoToApi(currentForm.contenido),
          precio: Number(currentForm.precio),
          cupos: Number(currentForm.cupos),
        }),
      });

      if (!response.ok) {
        throw new Error("No se pudo actualizar el evento.");
      }

      const data = await response.json();
      setForm(mapEventoAdminToForm(data));
      setSuccess("Evento actualizado correctamente.");
    } catch (err) {
      console.error(err);
      setError("No se pudo actualizar el evento.");
    }

    setSubmitting(false);
  };

  if (!user) {
    return <ActividadLoadingPage />;
  }

  if (!isAdmin) {
    return (
      <div className="container py-5">
        <div className="bg-white rounded-4 shadow-sm border p-5 text-center">
          <h2 className="fw-bold mb-3">Acceso restringido</h2>
          <p className="text-muted mb-4">
            Solo administradores pueden editar eventos.
          </p>
          <Link to={`/eventos/${id}`} className="btn btn-brand px-4">
            Volver al evento
          </Link>
        </div>
      </div>
    );
  }

  if (loading) {
    return <ActividadLoadingPage />;
  }

  if (error && !form) {
    return <ActividadErrorPage error={error} />;
  }

  if (!form) {
    return <ActividadNotFoundPage />;
  }

  return (
    <ActividadEditorForm
      tipo="evento"
      form={form}
      setForm={setForm}
      onSubmit={handleSubmit}
      submitting={submitting}
      error={error}
      success={success}
      backLink={`/eventos/${id}`}
      backLabel="Volver al evento"
    />
  );
}
