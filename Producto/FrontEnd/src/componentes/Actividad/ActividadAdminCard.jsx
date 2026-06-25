import { useEffect, useState } from "react";
import { useAuth } from "../../utils/AuthProvider";
import PageNav from "../PageNav/PageNav";
import { User2 } from "lucide-react";

export default function ActividadAdminCard({
  apiBasePath,
  actividadId,
  tipoLabel,
  activo,
  setActivo,
}) {
  const { user } = useAuth();
  const [usuarios, setUsuarios] = useState([]);
  const [pagina, setPagina] = useState(0);
  const [numPaginas, setNumPaginas] = useState(0);
  const [loadingUsuarios, setLoadingUsuarios] = useState(true);
  const [togglingActivo, setTogglingActivo] = useState(false);
  const [error, setError] = useState("");

  const isAdmin = user?.rol?.tipoRol === "Administrador";

  useEffect(() => {
    if (!isAdmin) return;

    let cancelled = false;

    const fetchUsuarios = async () => {
      setLoadingUsuarios(true);
      setError("");

      try {
        const res = await fetch(
          `${apiBasePath}/${actividadId}/i/a?page=${pagina}`,
        );
        if (!res.ok) {
          throw new Error("No se pudieron cargar los inscritos.");
        }

        const data = await res.json();
        if (!cancelled) {
          setUsuarios(data.content);
          setPagina(data.number);
          setNumPaginas(data.totalPages);
        }
      } catch (err) {
        console.error(err);
        if (!cancelled) {
          setUsuarios([]);
          setNumPaginas(0);
          setError("No se pudieron cargar los usuarios inscritos.");
        }
      }

      if (!cancelled) {
        setLoadingUsuarios(false);
      }
    };

    fetchUsuarios();

    return () => {
      cancelled = true;
    };
  }, [actividadId, apiBasePath, isAdmin, pagina]);

  if (!isAdmin) {
    return null;
  }

  const handleToggleActivo = async () => {
    if (activo == null) return;

    setTogglingActivo(true);
    setError("");

    try {
      const res = await fetch(`${apiBasePath}/${actividadId}`, {
        method: "PATCH",
        body: JSON.stringify({ activo: !activo }),
      });

      if (!res.ok) {
        throw new Error("No se pudo actualizar el estado.");
      }

      const data = await res.json();
      setActivo(data.activo);
    } catch (err) {
      console.error(err);
      setError(`No se pudo actualizar el estado del ${tipoLabel}.`);
    }

    setTogglingActivo(false);
  };

  return (
    <div className="bg-white rounded-3 shadow-sm border p-4 mt-4">
      <div className="d-flex flex-column flex-lg-row justify-content-between align-items-lg-center gap-3 mb-4 text-start">
        <div>
          <h4 className="mb-1 fw-semibold">Panel administrador</h4>
          <p className="text-muted mb-0">
            Gestiona el estado del {tipoLabel} y revisa los usuarios inscritos.
          </p>
        </div>
        <div className="d-flex flex-wrap gap-2 align-items-center">
          <span
            className={`badge rounded-pill px-3 py-2 fs-6 ${activo ? "bg-success-subtle text-success" : "bg-secondary-subtle text-secondary"}`}
          >
            {activo ? "Activo" : "Inactivo"}
          </span>
          <button
            type="button"
            className={`btn ${activo ? "btn-outline-secondary" : "btn-brand"} rounded-pill px-3`}
            onClick={handleToggleActivo}
            disabled={togglingActivo || activo == null}
          >
            {togglingActivo
              ? "Actualizando..."
              : activo
                ? `Desactivar ${tipoLabel}`
                : `Activar ${tipoLabel}`}
          </button>
        </div>
      </div>

      <div className="border-top pt-4">
        <h5 className="fw-semibold mb-3">Usuarios inscritos</h5>

        {error && (
          <div className="alert alert-danger py-2 px-3 mb-3" role="alert">
            {error}
          </div>
        )}

        {loadingUsuarios ? (
          <div className="d-flex justify-content-center py-4">
            <div className="spinner-border text-brand" />
          </div>
        ) : usuarios.length === 0 ? (
          <p className="text-muted mb-0">Todavía no hay usuarios inscritos.</p>
        ) : (
          <>
            <div className="d-flex flex-column gap-2">
              {usuarios.map((usuario) => (
                <div
                  key={usuario.correo}
                  className="d-flex justify-content-between align-items-center border rounded-3 px-3 py-2 bg-light-subtle"
                >
                  <div className="d-flex align-items-center gap-2">
                    <User2 size={48} />
                    <div>
                      <div className="fw-semibold">
                        {usuario.nombre} {usuario.apellido}
                      </div>
                      <div className="small text-muted">
                        {usuario.rol?.tipoRol ?? "Usuario"}
                      </div>
                    </div>
                  </div>
                  <div className="d-flex flex-column fs-6 text-muted text-end">
                    <small>{usuario.correo}</small>
                    {usuario.detalle?.ubicacionUsuario?.region && (
                      <small>{usuario.detalle.ubicacionUsuario.region}</small>
                    )}
                  </div>
                </div>
              ))}
            </div>
            {numPaginas > 1 && (
              <div className="mt-3">
                <PageNav
                  page={pagina}
                  setPage={setPagina}
                  numPages={numPaginas}
                />
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}
