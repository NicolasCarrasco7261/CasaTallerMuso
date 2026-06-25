import { useState } from "react";
import { ChevronLeft } from "lucide-react";
import { Link } from "react-router-dom";
import { apiUrlFromPath } from "../../utils/api";

const DIAS_SEMANA = [
  "Lunes",
  "Martes",
  "Miércoles",
  "Jueves",
  "Viernes",
  "Sábado",
  "Domingo",
];

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

export default function ActividadEditorForm({
  tipo,
  form,
  setForm,
  onSubmit,
  submitting,
  error,
  success,
  backLink,
  backLabel,
}) {
  const [uploadingImage, setUploadingImage] = useState(false);
  const [imageMessage, setImageMessage] = useState("");
  const [imageError, setImageError] = useState("");

  const previewUrl = form.imagenStorageKey
    ? apiUrlFromPath(`/api/files/img/${form.imagenStorageKey}`)
    : null;

  const handleActividadChange = (event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleHorarioChange = (index, field, value) => {
    setForm((prev) => ({
      ...prev,
      horarios: prev.horarios.map((horario, horarioIndex) =>
        horarioIndex === index ? { ...horario, [field]: value } : horario,
      ),
    }));
  };

  const addHorario = () => {
    setForm((prev) => ({
      ...prev,
      horarios: [
        ...prev.horarios,
        tipo === "curso"
          ? { diaDeSemana: "Lunes", horaDesde: "10:00", horaHasta: "12:00" }
          : { fecha: "", hora: "19:00" },
      ],
    }));
  };

  const removeHorario = (index) => {
    setForm((prev) => ({
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
    setImageError("");
    setImageMessage("");

    try {
      const key = await uploadImage(file);
      setForm((prev) => ({ ...prev, imagenStorageKey: key }));
      setImageMessage("Imagen subida correctamente.");
    } catch (err) {
      console.error(err);
      setImageError("No se pudo subir la imagen.");
    }

    event.target.value = "";
    setUploadingImage(false);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    onSubmit(form);
  };

  return (
    <div className="d-flex flex-column gap-3">
      <Link
        to={backLink}
        className="d-flex m-0 p-1 btn btn-brand-outline rounded-pill align-items-center"
        style={{ width: "fit-content", height: "fit-content" }}
      >
        <ChevronLeft size={24} />
        <span className="fw-semibold ps-1 pe-2">{backLabel}</span>
      </Link>

      {error && <div className="alert alert-danger mb-0">{error}</div>}
      {success && <div className="alert alert-success mb-0">{success}</div>}

      <div className="bg-white rounded-4 shadow-sm border p-4 text-start">
        <div className="mb-4">
          <h2 className="fw-bold mb-1" style={{ letterSpacing: "-1px" }}>
            Editar {tipo}
          </h2>
          <p className="text-muted mb-0">
            Modifica los datos principales, la imagen y los horarios del {tipo}.
          </p>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="row g-3">
            <div className="col-12">
              <label className="form-label fw-semibold">Nombre</label>
              <input
                type="text"
                name="nombre"
                className="form-control"
                value={form.nombre}
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
                value={form.precio}
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
                value={form.cupos}
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
                value={form.descripcion}
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
                value={form.contenido}
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
              />
              <div className="form-text">
                {uploadingImage
                  ? "Subiendo imagen..."
                  : form.imagenStorageKey
                    ? `Storage key actual: ${form.imagenStorageKey}`
                    : "Sube una imagen para obtener el storage key requerido por la API."}
              </div>
              {imageMessage && (
                <div className="text-success small mt-2">{imageMessage}</div>
              )}
              {imageError && (
                <div className="text-danger small mt-2">{imageError}</div>
              )}
            </div>

            {previewUrl && (
              <div className="col-12">
                <img
                  src={previewUrl}
                  alt={form.nombre}
                  className="w-100 rounded-4 border"
                  style={{ maxHeight: "18rem", objectFit: "cover" }}
                />
              </div>
            )}

            <div className="col-12">
              <div className="d-flex align-items-center justify-content-between mb-2">
                <label className="form-label fw-semibold mb-0">Horarios</label>
                <button
                  type="button"
                  className="btn btn-sm btn-outline-secondary rounded-pill"
                  onClick={addHorario}
                >
                  Agregar horario
                </button>
              </div>

              {tipo === "curso" ? (
                <CursoHorariosEditor
                  horarios={form.horarios}
                  onChange={handleHorarioChange}
                  onRemove={removeHorario}
                />
              ) : (
                <EventoHorariosEditor
                  horarios={form.horarios}
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
                {submitting ? "Guardando..." : "Guardar cambios"}
              </button>
            </div>
          </div>
        </form>
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
