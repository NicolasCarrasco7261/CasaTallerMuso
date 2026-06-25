import { useState, useEffect } from "react"; // Añadimos useCallback
import { createEventoCard } from "../../objetos/Evento";
import PageNav from "../../componentes/PageNav/PageNav";
import ActividadCard from "../../componentes/Actividad/ActividadCard";
import "./Eventos.css";
import { useAuth } from "../../utils/AuthProvider";

export default function Eventos() {
  const { user } = useAuth();
  const [eventos, setEventos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pagina, setPagina] = useState(0);
  const [numPaginas, setNumPaginas] = useState(0);
  const [showHidden, setShowHidden] = useState(false);
  const isAdmin = user?.rol?.tipoRol === "Administrador";

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setEventos(null);

      const hiddenParam = showHidden ? "&hidden=true" : "";
      const res = await fetch(
        `/api/eventos?page=${pagina}&size=6${hiddenParam}`,
      );
      if (!res.ok) {
        setError(`«${res.status}»`);
        return;
      }
      const data = await res.json();
      const eventoCards = data.content.map((c) => createEventoCard(c));

      setEventos(eventoCards);
      setNumPaginas(data.totalPages);
      setLoading(false);
    };
    fetchData();
  }, [pagina, showHidden]);

  if (loading)
    return <div className="text-center py-5">Cargando catálogo...</div>;
  if (error)
    return <div className="text-center py-5 text-danger">Error: {error}</div>;

  const numPagBtns = Math.min(5, numPaginas);
  const distToEnd = numPaginas - pagina;
  const pagBtnVals = Array.from(
    { length: numPagBtns },
    (_, i) =>
      i +
      pagina -
      Math.min(pagina, 1) -
      Math.max(-distToEnd + numPagBtns - 1, 0),
  );
  pagBtnVals.splice(5);

  return (
    <>
      <header className="py-5 text-center bg-white shadow-sm border-bottom mb-5">
        <div className="d-flex flex-column align-items-center gap-3">
          <span className="text-uppercase tracking-widest text-brand fw-bold">
            Nuestros Eventos
          </span>
          {isAdmin && (
            <button
              type="button"
              className={`btn rounded-pill px-4 ${showHidden ? "btn-brand" : "btn-outline-secondary"}`}
              onClick={() => {
                setPagina(0);
                setShowHidden((prev) => !prev);
              }}
            >
              {showHidden ? "Ocultar inactivos" : "Mostrar ocultos"}
            </button>
          )}
        </div>
      </header>

      <div className="row g-4 mb-5">
        {eventos.length > 0 ? (
          eventos.map((evento, i) => (
            <ActividadCard
              key={i}
              href={`/eventos/${evento.id}`}
              actividad={evento}
            />
          ))
        ) : (
          <div className="col-12 text-center py-5">
            <h3 className="text-muted">No hay registros disponibles</h3>
          </div>
        )}
      </div>

      <PageNav page={pagina} numPages={numPaginas} setPage={setPagina} />
    </>
  );
}
