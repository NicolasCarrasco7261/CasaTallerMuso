import { useState, useEffect } from "react"; // Añadimos useCallback
import "./Cursos.css";
import { createCursoCard } from "../../objetos/Curso";
import PageNav from "../../componentes/PageNav/PageNav";
import ActividadCard from "../../componentes/Actividad/ActividadCard";

export default function Cursos() {
  const [cursos, setCursos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pagina, setPagina] = useState(0);
  const [numPaginas, setNumPaginas] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setCursos(null);

      const res = await fetch(`/api/cursos?page=${pagina}&size=6`);
      if (!res.ok) {
        setError(`«${res.status}»`);
        return;
      }
      const data = await res.json();
      const cursoCards = data.content.map((c) => createCursoCard(c));

      setCursos(cursoCards);
      setNumPaginas(data.totalPages);
      setLoading(false);
    };
    fetchData();
  }, [pagina]);

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
        <span className="text-uppercase tracking-widest text-brand fw-bold">
          Nuestros Cursos
        </span>
      </header>

      <div className="row g-4 mb-5">
        {cursos.length > 0 ? (
          cursos.map((curso, i) => (
            <ActividadCard
              key={i}
              href={`/cursos/${curso.id}`}
              actividad={curso}
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
