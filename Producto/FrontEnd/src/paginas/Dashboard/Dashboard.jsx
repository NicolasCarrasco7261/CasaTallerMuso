import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../../utils/AuthProvider";

import { createCursoCard } from "../../objetos/Curso";
import { createEventoCard } from "../../objetos/Evento";

const DIAS_SEMANA = [
  "Lunes",
  "Martes",
  "Miércoles",
  "Jueves",
  "Viernes",
  "Sábado",
  "Domingo",
];

function createInitialCursoForm() {
  return {
    nombre: "",
    descripcion: "",
    contenido: "",
    imagenStorageKey: "",
    precio: "",
    cupos: "",
    activo: true,
    horarios: [
      {
        diaDeSemana: "Lunes",
        horaDesde: "10:00",
        horaHasta: "12:00",
      },
    ],
  };
}

function createInitialEventoForm() {
  return {
    nombre: "",
    descripcion: "",
    contenido: "",
    imagenStorageKey: "",
    precio: "",
    cupos: "",
    activo: true,
    horarios: [
      {
        fecha: "",
        hora: "19:00",
      },
    ],
  };
}

function formatClp(value) {
  return new Intl.NumberFormat("es-CL", {
    style: "currency",
    currency: "CLP",
    maximumFractionDigits: 0,
  }).format(value);
}

function contenidoToApi(value) {
  return (value ?? "").replace(/\r\n/g, "\n").replace(/\n/g, "\\n");
}

async function uploadImage(file) {
  const formData = new FormData();
  formData.append("file", file);

  const response = await fetch("/api/files/img", {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    throw new Error(`Error al subir imagen: ${response.status}`);
  }

  const data = await response.json();
  return data.key;
}

export default function Dashboard() {
  const { user } = useAuth();
  const [vistaActiva, setVistaActiva] = useState("cursos");
  const [stats, setStats] = useState(null);
  const [cursos, setCursos] = useState([]);
  const [eventos, setEventos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [uploadingImage, setUploadingImage] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [cursoForm, setCursoForm] = useState(createInitialCursoForm);
  const [eventoForm, setEventoForm] = useState(createInitialEventoForm);

  const isAdmin = user?.rol?.tipoRol === "Administrador";

  const fetchDashboardData = async () => {
    setLoading(true);
    setError("");

    try {
      const [statsRes, cursosRes, eventosRes] = await Promise.all([
        fetch("/api/stats"),
        fetch("/api/cursos?page=0&size=3&hidden=true"),
        fetch("/api/eventos?page=0&size=3&hidden=true"),
      ]);

      if (!statsRes.ok || !cursosRes.ok || !eventosRes.ok) {
        throw new Error("No se pudieron cargar los datos del dashboard.");
      }

      const [statsData, cursosData, eventosData] = await Promise.all([
        statsRes.json(),
        cursosRes.json(),
        eventosRes.json(),
      ]);

      setStats(statsData);
      setCursos(cursosData.content.map((curso) => createCursoCard(curso)));
      setEventos(eventosData.content.map((evento) => createEventoCard(evento)));
    } catch (err) {
      console.error(err);
      setError("No se pudo cargar el panel de administración.");
    }

    setLoading(false);
  };

  useEffect(() => {
    if (!isAdmin) return;

    let cancelled = false;
    Promise.resolve().then(() => {
      if (!cancelled) {
        fetchDashboardData();
      }
    });

    return () => {
      cancelled = true;
    };
  }, [isAdmin]);

  const activeForm = vistaActiva === "cursos" ? cursoForm : eventoForm;
  const setActiveForm = vistaActiva === "cursos" ? setCursoForm : setEventoForm;

  const handleActividadChange = (event) => {
    const { name, value } = event.target;
    setActiveForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleHorarioChange = (index, field, value) => {
    setActiveForm((prev) => ({
      ...prev,
      horarios: prev.horarios.map((horario, horarioIndex) =>
        horarioIndex === index ? { ...horario, [field]: value } : horario,
      ),
    }));
  };

  const addHorario = () => {
    setActiveForm((prev) => ({
      ...prev,
      horarios: [
        ...prev.horarios,
        vistaActiva === "cursos"
          ? { diaDeSemana: "Lunes", horaDesde: "10:00", horaHasta: "12:00" }
          : { fecha: "", hora: "19:00" },
      ],
    }));
  };

  const removeHorario = (index) => {
    setActiveForm((prev) => ({
      ...prev,
      horarios: prev.horarios.filter(
        (_, horarioIndex) => horarioIndex !== index,
      ),
    }));
  };

  const handleImageChange = async (event) => {
    const file = event.target.files?.[0];
    if (!file) return;

    setUploadingImage(true);
    setError("");
    setSuccess("");

    try {
      const key = await uploadImage(file);
      setActiveForm((prev) => ({ ...prev, imagenStorageKey: key }));
      setSuccess("Imagen subida correctamente.");
    } catch (err) {
      console.error(err);
      setError("No se pudo subir la imagen.");
    }

    event.target.value = "";
    setUploadingImage(false);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSubmitting(true);
    setError("");
    setSuccess("");

    const payload = {
      ...activeForm,
      contenido: contenidoToApi(activeForm.contenido),
      precio: Number(activeForm.precio),
      cupos: Number(activeForm.cupos),
      activo: true,
    };

    try {
      const endpoint =
        vistaActiva === "cursos" ? "/api/cursos" : "/api/eventos";
      const response = await fetch(endpoint, {
        method: "POST",
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        throw new Error(
          `Error creando ${vistaActiva === "cursos" ? "curso" : "evento"}`,
        );
      }

      setSuccess(
        `${vistaActiva === "cursos" ? "Curso" : "Evento"} creado correctamente.`,
      );
      if (vistaActiva === "cursos") {
        setCursoForm(createInitialCursoForm());
      } else {
        setEventoForm(createInitialEventoForm());
      }
      fetchDashboardData();
    } catch (err) {
      console.error(err);
      setError(
        `No se pudo crear el ${vistaActiva === "cursos" ? "curso" : "evento"}.`,
      );
    }

    setSubmitting(false);
  };

  if (!user) {
    return (
      <div className="d-flex justify-content-center py-5">
        <div
          className="spinner-border text-brand"
          style={{ width: "4rem", height: "4rem", borderWidth: "0.4rem" }}
        />
      </div>
    );
  }

  if (!isAdmin) {
    return (
      <div className="container py-5">
        <div className="bg-white rounded-4 shadow-sm border p-5 text-center">
          <h2 className="fw-bold mb-3">Acceso restringido</h2>
          <p className="text-muted mb-4">
            Este panel está disponible solo para administradores.
          </p>
          <Link to="/" className="btn btn-brand px-4">
            Volver al inicio
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="container pb-4">
      <header className="py-4 bg-white shadow-sm mb-4 rounded-4 px-4">
        <div className="d-flex flex-column flex-lg-row align-items-lg-center justify-content-between gap-3">
          <div className="text-start">
            <h2 className="fw-bold mb-1" style={{ letterSpacing: "-1px" }}>
              Dashboard administrador
            </h2>
            <p className="text-muted mb-0">Crea cursos y eventos.</p>
          </div>
          <div
            className="btn-group rounded-pill overflow-hidden shadow-sm border"
            role="tablist"
            aria-label="Tipo de actividad"
          >
            <button
              type="button"
              className={`btn border-0 ${vistaActiva === "cursos" ? "btn-brand" : "btn-outline-secondary"}`}
              onClick={() => setVistaActiva("cursos")}
            >
              Cursos
            </button>
            <button
              type="button"
              className={`btn border-0 ${vistaActiva === "eventos" ? "btn-brand" : "btn-outline-secondary"}`}
              onClick={() => setVistaActiva("eventos")}
            >
              Eventos
            </button>
          </div>
        </div>
      </header>

      {error && (
        <div className="alert alert-danger rounded-4 shadow-sm" role="alert">
          {error}
        </div>
      )}

      {success && (
        <div className="alert alert-success rounded-4 shadow-sm" role="alert">
          {success}
        </div>
      )}

      <div className="row g-4 mb-4">
        <div className="col-12 col-md-6">
          <StatCard
            title="Cursos disponibles"
            value={stats?.cursosDisponibles}
            loading={loading}
          />
        </div>
        <div className="col-12 col-md-6">
          <StatCard
            title="Eventos disponibles"
            value={stats?.eventosDisponibles}
            loading={loading}
          />
        </div>
      </div>

      <div className="row g-4 align-items-start">
        <div className="col-12 col-xl-7">
          <div className="bg-white rounded-4 shadow-sm border p-4">
            <div className="mb-4">
              <span className="badge bg-light text-success border border-success-subtle rounded-pill px-3 py-2 fw-semibold">
                Nuevo {vistaActiva === "cursos" ? "curso" : "evento"}
              </span>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="row g-3">
                <div className="col-12">
                  <label className="form-label fw-semibold">Nombre</label>
                  <input
                    type="text"
                    name="nombre"
                    className="form-control"
                    value={activeForm.nombre}
                    onChange={handleActividadChange}
                    required
                  />
                </div>

                <div className="col-md-6">
                  <label className="form-label fw-semibold">Precio</label>
                  <input
                    type="number"
                    min="0"
                    name="precio"
                    className="form-control"
                    value={activeForm.precio}
                    onChange={handleActividadChange}
                    required
                  />
                </div>

                <div className="col-md-6">
                  <label className="form-label fw-semibold">Cupos</label>
                  <input
                    type="number"
                    min="1"
                    name="cupos"
                    className="form-control"
                    value={activeForm.cupos}
                    onChange={handleActividadChange}
                    required
                  />
                </div>

                <div className="col-12">
                  <label className="form-label fw-semibold">
                    Descripción breve
                  </label>
                  <textarea
                    name="descripcion"
                    className="form-control"
                    rows="2"
                    value={activeForm.descripcion}
                    onChange={handleActividadChange}
                    required
                  />
                </div>

                <div className="col-12">
                  <label className="form-label fw-semibold">Contenido</label>
                  <textarea
                    name="contenido"
                    className="form-control"
                    rows="7"
                    value={activeForm.contenido}
                    onChange={handleActividadChange}
                    required
                  />
                </div>

                <div className="col-12">
                  <label className="form-label fw-semibold">Imagen</label>
                  <input
                    type="file"
                    accept="image/*"
                    className="form-control"
                    onChange={handleImageChange}
                    disabled={uploadingImage}
                    required={!activeForm.imagenStorageKey}
                  />
                  <div className="form-text">
                    {uploadingImage
                      ? "Subiendo imagen..."
                      : activeForm.imagenStorageKey
                        ? `Imagen cargada: ${activeForm.imagenStorageKey}`
                        : "Sube una imagen para obtener el storage key requerido por la API."}
                  </div>
                </div>

                <div className="col-12">
                  <div className="d-flex align-items-center justify-content-between mb-2">
                    <label className="form-label fw-semibold mb-0">
                      Horarios
                    </label>
                    <button
                      type="button"
                      className="btn btn-sm btn-outline-secondary rounded-pill"
                      onClick={addHorario}
                    >
                      Agregar horario
                    </button>
                  </div>

                  {vistaActiva === "cursos" ? (
                    <CursoHorariosEditor
                      horarios={activeForm.horarios}
                      onChange={handleHorarioChange}
                      onRemove={removeHorario}
                    />
                  ) : (
                    <EventoHorariosEditor
                      horarios={activeForm.horarios}
                      onChange={handleHorarioChange}
                      onRemove={removeHorario}
                    />
                  )}
                </div>

                <div className="col-12 d-flex justify-content-end pt-2">
                  <button
                    type="submit"
                    className="btn btn-brand px-4 py-2 fw-semibold"
                    disabled={submitting || uploadingImage}
                  >
                    {submitting
                      ? "Guardando..."
                      : `Crear ${vistaActiva === "cursos" ? "curso" : "evento"}`}
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>

        <div className="col-12 col-xl-5">
          <div className="bg-white rounded-4 shadow-sm border p-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
              <span className="badge bg-light text-success border border-success-subtle rounded-pill px-3 py-2 fw-semibold">
                {vistaActiva === "cursos"
                  ? "Cursos recientes"
                  : "Eventos recientes"}
              </span>
              <Link
                to={vistaActiva === "cursos" ? "/cursos" : "/eventos"}
                className="btn btn-sm btn-brand-outline rounded-pill px-3"
              >
                Ver catálogo
              </Link>
            </div>

            <ActividadResumenList
              items={vistaActiva === "cursos" ? cursos : eventos}
              detalleBasePath={
                vistaActiva === "cursos" ? "/cursos" : "/eventos"
              }
              loading={loading}
              emptyLabel={
                vistaActiva === "cursos"
                  ? "No hay cursos disponibles."
                  : "No hay eventos disponibles."
              }
            />
          </div>
        </div>
      </div>
    </div>
  );
}

function StatCard({ title, value, loading }) {
  return (
    <div className="bg-white rounded-4 shadow-sm border p-4 h-100">
      <div className="text-muted small text-uppercase fw-semibold mb-2">
        {title}
      </div>
      <div className="display-6 fw-bold mb-0">
        {loading ? "..." : (value ?? 0)}
      </div>
    </div>
  );
}

function CursoHorariosEditor({ horarios, onChange, onRemove }) {
  return (
    <div className="d-flex flex-column gap-3">
      {horarios.map((horario, index) => (
        <div className="border rounded-4 p-3 bg-light-subtle" key={index}>
          <div className="row g-3 align-items-end">
            <div className="col-md-4">
              <label className="form-label small fw-semibold">Día</label>
              <select
                className="form-select"
                value={horario.diaDeSemana}
                onChange={(event) =>
                  onChange(index, "diaDeSemana", event.target.value)
                }
              >
                {DIAS_SEMANA.map((dia) => (
                  <option value={dia} key={dia}>
                    {dia}
                  </option>
                ))}
              </select>
            </div>
            <div className="col-md-3">
              <label className="form-label small fw-semibold">Desde</label>
              <input
                type="time"
                className="form-control"
                value={horario.horaDesde}
                onChange={(event) =>
                  onChange(index, "horaDesde", event.target.value)
                }
                required
              />
            </div>
            <div className="col-md-3">
              <label className="form-label small fw-semibold">Hasta</label>
              <input
                type="time"
                className="form-control"
                value={horario.horaHasta}
                onChange={(event) =>
                  onChange(index, "horaHasta", event.target.value)
                }
                required
              />
            </div>
            <div className="col-md-2 d-grid">
              <button
                type="button"
                className="btn btn-outline-danger"
                onClick={() => onRemove(index)}
                disabled={horarios.length === 1}
              >
                Quitar
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}

function EventoHorariosEditor({ horarios, onChange, onRemove }) {
  return (
    <div className="d-flex flex-column gap-3">
      {horarios.map((horario, index) => (
        <div className="border rounded-4 p-3 bg-light-subtle" key={index}>
          <div className="row g-3 align-items-end">
            <div className="col-md-5">
              <label className="form-label small fw-semibold">Fecha</label>
              <input
                type="date"
                className="form-control"
                value={horario.fecha}
                onChange={(event) =>
                  onChange(index, "fecha", event.target.value)
                }
                required
              />
            </div>
            <div className="col-md-5">
              <label className="form-label small fw-semibold">Hora</label>
              <input
                type="time"
                className="form-control"
                value={horario.hora}
                onChange={(event) =>
                  onChange(index, "hora", event.target.value)
                }
                required
              />
            </div>
            <div className="col-md-2 d-grid">
              <button
                type="button"
                className="btn btn-outline-danger"
                onClick={() => onRemove(index)}
                disabled={horarios.length === 1}
              >
                Quitar
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}

function ActividadResumenList({ items, detalleBasePath, loading, emptyLabel }) {
  if (loading) {
    return (
      <div className="d-flex justify-content-center py-5">
        <div className="spinner-border text-brand" />
      </div>
    );
  }

  if (!items.length) {
    return <p className="text-muted mb-0">{emptyLabel}</p>;
  }

  return (
    <div className="d-flex flex-column gap-3">
      {items.map((item) => (
        <div className="border rounded-4 overflow-hidden" key={item.id}>
          <img
            src={item.imagenUrl}
            alt={item.nombre}
            className="w-100 border-bottom"
            style={{ height: "12rem", objectFit: "cover" }}
          />
          <div className="p-3">
            <h5 className="fw-bold mb-2">{item.nombre}</h5>
            <div className="d-flex flex-wrap gap-2 mb-3 text-muted small">
              <span>{formatClp(item.precio)}</span>
              <span>•</span>
              <span>{item.cuposRestantes ?? item.cupos} cupos disponibles</span>
            </div>
            <Link
              to={`${detalleBasePath}/${item.id}`}
              className="btn btn-brand-outline rounded-pill px-3"
            >
              Ver detalle
            </Link>
          </div>
        </div>
      ))}
    </div>
  );
}
