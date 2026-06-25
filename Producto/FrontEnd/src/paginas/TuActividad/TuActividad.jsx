import { useEffect, useReducer, useState } from "react";
import "../Cursos/Cursos.css";
import { useAuth } from "../../utils/AuthProvider";
import { Link } from "react-router-dom";
import { createCursoCard } from "../../objetos/Curso";
import { createEventoCard } from "../../objetos/Evento";
import PageNav from "../../componentes/PageNav/PageNav";

export default function TuActividad() {
  const { user } = useAuth();
  const [vistaActiva, setVistaActiva] = useState("cursos");

  if (!user) {
    return (
      <div
        style={{ width: "5rem", height: "5rem", borderWidth: "0.5rem" }}
        className="m-auto spinner-border text-brand"
      />
    );
  }

  return (
    <div className="container pb-5">
      <header className="py-4 bg-white shadow-sm mb-4 rounded-3">
        <div className="container text-center">
          <p className="text-muted mb-0 fw-semibold">
            Gestiona tus cursos y eventos desde un solo lugar.
          </p>
        </div>
      </header>
      <div className="d-flex flex-row gap-4 justify-content-center">
        <UsuarioCard user={user} />
        <div
          className="flex-grow-1 bg-white shadow-sm rounded-3"
          style={{ height: "fit-content" }}
        >
          <div className="px-3 pt-3">
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

          {vistaActiva === "cursos" ? (
            <CursosInscritos />
          ) : (
            <EventosInscritos />
          )}
        </div>
      </div>
    </div>
  );
}

function CursosInscritos() {
  return (
    <ActividadInscritaPanel
      tipo="curso"
      titulo="Cursos inscritos"
      emptyText="Aún no te has inscrito en ningún curso."
      emptyPath="/cursos"
      emptyCta="Explorar Catálogo"
      detalleBasePath="/cursos"
      apiBasePath="/api/cursos"
      createCard={createCursoCard}
    />
  );
}

function EventosInscritos() {
  return (
    <ActividadInscritaPanel
      tipo="evento"
      titulo="Eventos inscritos"
      emptyText="Aún no te has inscrito en ningún evento."
      emptyPath="/eventos"
      emptyCta="Explorar Eventos"
      detalleBasePath="/eventos"
      apiBasePath="/api/eventos"
      createCard={createEventoCard}
    />
  );
}

function ActividadInscritaPanel({
  tipo,
  titulo,
  emptyText,
  emptyPath,
  emptyCta,
  detalleBasePath,
  apiBasePath,
  createCard,
}) {
  const [inscripciones, setInscripciones] = useState([]);
  const [pagina, setPagina] = useState(0);
  const [numPaginas, setNumPaginas] = useState(0);
  const [loading, setLoading] = useState(true);
  const [refreshKey, forceUpdate] = useReducer((x) => x + 1, 0);

  useEffect(() => {
    let ignore = false;

    const fetchInscripciones = async () => {
      setLoading(true);

      try {
        const res = await fetch(`${apiBasePath}/me/i?page=${pagina}`);

        if (!res.ok) {
          if (!ignore) {
            setInscripciones([]);
            setNumPaginas(0);
          }
          return;
        }

        const data = await res.json();

        if (!ignore) {
          setPagina(data.number);
          setNumPaginas(data.totalPages);
          setInscripciones(data.content.map((item) => createCard(item)));
        }
      } catch (err) {
        console.error("Error cargando actividad:", err);
        if (!ignore) {
          setInscripciones([]);
          setNumPaginas(0);
        }
      }

      if (!ignore) {
        setLoading(false);
      }
    };

    fetchInscripciones();

    return () => {
      ignore = true;
    };
  }, [apiBasePath, createCard, pagina, refreshKey]);

  const desinscribir = async ({ id, nombre }) => {
    const userInput = prompt(
      `Escriba "Eliminar" para eliminar su inscripción al ${tipo} "${nombre}".`,
    );
    if (userInput === null) return;
    if (userInput.toLowerCase() !== "eliminar") {
      alert("Operación cancelada.");
      return;
    }

    try {
      setLoading(true);
      const res = await fetch(`${apiBasePath}/${id}/i`, { method: "DELETE" });

      if (res.ok) {
        forceUpdate();
      } else {
        setLoading(false);
      }
    } catch (err) {
      console.error("Error al eliminar:", err);
      setLoading(false);
    }
  };

  return (
    <>
      {loading ? (
        <div className="d-flex justify-content-center py-5">
          <div
            style={{ width: "4rem", height: "4rem", borderWidth: "0.4rem" }}
            className="spinner-border text-brand"
          />
        </div>
      ) : inscripciones.length === 0 ? (
        <div className="text-center m-3 py-5 bg-white rounded-3 border">
          <p className="text-muted mb-4">{emptyText}</p>
          <Link to={emptyPath} className="btn btn-brand fw-bold px-4 py-2">
            {emptyCta}
          </Link>
        </div>
      ) : (
        <div className="d-flex flex-column gap-3 p-3 m-3 bg-white rounded-3 border overflow-hidden">
          {inscripciones.map((inscripcion) => (
            <div
              className="col-12 d-flex rounded overflow-hidden border shadow-sm lift-on-hover"
              key={inscripcion.id}
            >
              <img
                src={inscripcion.imagenUrl}
                className="border-end"
                style={{ width: "10rem", height: "6rem", objectFit: "cover" }}
              />
              <div className="d-flex flex-column ps-3 m-auto ms-0 align-items-start gap-2">
                <h1 className="m-0 fs-3 text-muted">{inscripcion.nombre}</h1>
                <div className="d-flex flex-row gap-2">
                  <Link
                    to={`${detalleBasePath}/${inscripcion.id}`}
                    className="btn btn-brand-outline py-0 px-3 m-0 rounded-pill"
                  >
                    » Ir a {tipo}
                  </Link>
                  <button
                    onClick={() => desinscribir(inscripcion)}
                    className="btn btn-outline-danger py-0 px-3 m-0 rounded-pill"
                  >
                    Eliminar
                  </button>
                </div>
              </div>
            </div>
          ))}
          {numPaginas > 1 && (
            <PageNav page={pagina} setPage={setPagina} numPages={numPaginas} />
          )}
        </div>
      )}
    </>
  );
}

function UsuarioCard({ user }) {
  const nombre = user.nombre;
  const apellido = user.apellido;
  const correo = user.correo;

  if (!nombre | !apellido | !correo) {
    console.error("UsuarioCard: Datos requeridos no presentes.");
    return null;
  }

  const telefono = user.detalle?.numeroTelefonico;
  const genero = user.detalle?.genero;
  const region = user.detalle?.ubicacionUsuario?.region;

  let color;
  switch (user.rol?.tipoRol) {
    case "Administrador":
      color = "#592da0";
      break;
    default:
      color = "#a0522d";
  }

  return (
    <div
      className="d-none d-xl-block p-0 card border-0 shadow-sm overflow-hidden text-center rounded-3"
      style={{ width: "22rem", height: "fit-content" }}
    >
      <div className="py-5" style={{ backgroundColor: color }} />
      <div
        className="px-4 pb-4 d-flex flex-column"
        style={{ marginTop: "-40px" }}
      >
        <div
          className="bg-white rounded-circle me-auto mb-3 d-flex align-items-center justify-content-center shadow-sm fs-1 fw-bold"
          style={{ width: "90px", height: "90px", color: color }}
        >
          {nombre.charAt(0)}
        </div>
        <h5 className="fw-bold mb-1 me-auto">
          {nombre} {apellido}
        </h5>
        <p className="me-auto">
          <span
            className="badge shadow-sm sb-3 px-3 rounded-pill me-1"
            style={{ backgroundColor: color }}
          >
            {user.rol?.tipoRol}
          </span>
          <span
            className="text-muted fw-semibold"
            style={{ fontSize: "0.9rem" }}
          >
            {correo}
          </span>
        </p>
        <div className="border-top my-3" />
        <div style={{ display: "contents", fontSize: "0.85rem" }}>
          {telefono && (
            <p className="text-muted me-auto fw-semibold">
              <span
                className="badge shadow-sm px-2 me-1 rounded-pill"
                style={{ backgroundColor: color, minWidth: "6.5em" }}
              >
                Fono
              </span>
              {telefono}
            </p>
          )}
          {genero && (
            <p className="text-muted me-auto fw-semibold">
              <span
                className="badge shadow-sm px-2 me-1 rounded-pill"
                style={{ backgroundColor: color, minWidth: "6.5em" }}
              >
                Género
              </span>
              {genero}
            </p>
          )}
          {region && (
            <p className="text-muted me-auto fw-semibold">
              <span
                className="badge shadow-sm px-2 me-1 rounded-pill"
                style={{ backgroundColor: color, minWidth: "6.5em" }}
              >
                Región
              </span>
              {region}
            </p>
          )}
        </div>
      </div>
    </div>
  );
}
