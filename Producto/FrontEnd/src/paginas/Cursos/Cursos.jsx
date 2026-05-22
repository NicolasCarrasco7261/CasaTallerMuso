import { useState, useEffect, useCallback } from "react"; // Añadimos useCallback
import { useNavigate } from "react-router-dom";
import "./Cursos.css";
import Navbar from "../../componentes/Navbar/Navbar";
import { createCursoCard } from "../../objetos/Curso";
import CursoCard from "../../componentes/CursoCard/CursoCard";
import PageNav from "../../componentes/PageNav/PageNav";

export default function Cursos() {
  const [cursos, setCursos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pagina, setPagina] = useState(0);
  const [numPaginas, setNumPaginas] = useState(0);
  const navigate = useNavigate();

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
      const cursoCards = data.content.map(c => createCursoCard(c));

      setCursos(cursoCards);
      setNumPaginas(data.totalPages);
      setLoading(false);
    };
    fetchData();
  }, [pagina]);

  const handleSetPagina = (pag) => {
    if (pag >= 0 && pag < numPaginas) {
      setPagina(pag);
    } else {
      console.warn(`Se intentó buscar en página inválida (${pag}/${numPaginas})`);
    }
  };

  const manejarInscripcion = async (cursoId) => {
    const usuarioLogueado = JSON.parse(localStorage.getItem("usuario"));

    if (!usuarioLogueado) {
      alert("Debes iniciar sesión para inscribirte.");
      navigate("/login");
      return;
    }

    try {
      const nuevaInscripcion = {
        usuario: { id: usuarioLogueado.id },
        curso: { id: cursoId }
      };

      const response = await fetch("http://localhost:8080/api/inscripciones", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(nuevaInscripcion)
      });

      if (response.ok) {
        alert("¡Inscripción exitosa!");
        navigate("/tuActividad");
      } else {
        const msgError = await response.text();
        alert(msgError || "No hay cupos disponibles.");
        obtenerCursos(); // Refrescamos la lista por si los cupos cambiaron
      }
    } catch (err) {
      console.error("Error:", err);
      alert("Error de conexión con el servidor.");
    }
  };

  if (loading) return <div className="text-center py-5">Cargando catálogo...</div>;
  if (error) return <div className="text-center py-5 text-danger">Error: {error}</div>;

  const numPagBtns = Math.min(5, numPaginas);
  const distToEnd = numPaginas - pagina;
  const pagBtnVals = Array.from(
    { length: numPagBtns },
    (_, i) => (i + pagina) - Math.min(pagina, 1) - Math.max(-distToEnd + numPagBtns - 1, 0)
  );
  pagBtnVals.splice(5);

  return (
    <>
      <header className="py-5 text-center bg-white shadow-sm border-bottom mb-5">
        <span className="text-uppercase tracking-widest text-brand fw-bold">
          Nuestros Cursos
        </span>
      </header>

      <div className="row g-4">
        {cursos.length > 0 ? (
          cursos.map((curso, i) => <CursoCard key={i} curso={curso} />)
          ) : (
            <div className="col-12 text-center py-5">
              <h3 className="text-muted">No hay registros disponibles</h3>
            </div>
          )
        }
      </div>

      <PageNav page={pagina} numPages={numPaginas} setPage={setPagina} />
    </>
  );
};