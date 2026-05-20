import { useState, useEffect, useCallback } from "react"; // Añadimos useCallback
import { useNavigate } from "react-router-dom";
import "./Cursos.css";
import Navbar from "../../componentes/Navbar";

const Cursos = () => {
  const [listaCursos, setListaCursos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  // 1. Memorizamos la función con useCallback para evitar el error de renders en cascada
  const obtenerCursos = useCallback(async () => {
    try {
      const response = await fetch("http://localhost:8080/api/cursos");
      if (!response.ok) throw new Error("Error al obtener los datos");
      const data = await response.json();
      
      // Filtrar activos y actualizar el estado
      setListaCursos(data.filter(c => c.activo !== false));
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []); // Dependencias vacías: solo se crea una vez

  // 2. El useEffect ahora es "seguro" porque obtenerCursos es estable
  useEffect(() => {
    obtenerCursos();
  }, [obtenerCursos]);

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

  return (
    <div className="min-vh-100 bg-custom-cream text-start">
      <Navbar />

      <header className="py-5 text-center bg-white shadow-sm border-bottom mb-5">
        <span className="text-uppercase tracking-widest text-brand fw-bold">
          Nuestros Cursos
        </span>
      </header>

      <main className="container pb-5">
        <div className="row g-4">
          {listaCursos.length > 0 ? (
            listaCursos.map((curso) => (
              <div className="col-12 col-md-6 col-lg-4" key={curso.id}>
                <div className="curso-card bg-white shadow-sm h-100 border-0 shadow-hover">
                  <div className="curso-image-container position-relative overflow-hidden">
                    <img
                      src={curso.img || "https://via.placeholder.com/400x250?text=Muso+Arte"}
                      alt={curso.titulo}
                      className="curso-img w-100"
                    />
                    <span className="category-badge-overlay shadow-sm">
                      {curso.categoriaA?.nombre || "General"}
                    </span>
                  </div>

                  <div className="p-4 text-center">
                    <h4 className="fw-bold mb-2">{curso.titulo}</h4>
                    <p className="text-muted small mb-3">{curso.descripcion}</p>

                    <div className="d-flex flex-column align-items-center gap-1 mb-4">
                      <span className="style-horario fw-semibold text-secondary">
                        <i className="bi bi-clock me-1"></i> {curso.horario}
                      </span>
                      <div className="mt-1">
                        {curso.cupos > 0 ? (
                          <span className="badge rounded-pill bg-light text-success border border-success-subtle">
                            {curso.cupos} cupos disponibles
                          </span>
                        ) : (
                          <span className="badge rounded-pill bg-light text-danger border border-danger-subtle">
                            Agotado
                          </span>
                        )}
                      </div>
                    </div>

                    <div className="d-flex justify-content-between align-items-center pt-3 border-top mt-auto">
                      <span className="fw-bold text-brand fs-5">
                        ${Number(curso.precio).toLocaleString("es-CL")}
                      </span>
                      <button
                        className={`btn ${curso.cupos > 0 ? 'btn-brand' : 'btn-secondary'} rounded-0 px-3 py-2 fw-bold small`}
                        disabled={curso.cupos <= 0}
                        onClick={() => manejarInscripcion(curso.id)}
                      >
                        {curso.cupos > 0 ? "Inscribirse" : "Sin Cupos"}
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className="col-12 text-center py-5">
              <h3 className="text-muted">No hay registros disponibles</h3>
            </div>
          )}
        </div>
      </main>
    </div>
  );
};

export default Cursos;