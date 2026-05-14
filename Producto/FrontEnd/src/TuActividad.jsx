import { useState, useEffect, useCallback } from 'react';
import Navbar from './Navbar';
import './Cursos.css'; 

const TuActividad = () => {
  const usuario = JSON.parse(localStorage.getItem("usuario"));
  const [inscripciones, setInscripciones] = useState([]);
  const [loading, setLoading] = useState(true);

  const obtenerInscripciones = useCallback(async () => {
    if (!usuario?.id) return;
    try {
      const response = await fetch(`http://localhost:8080/api/inscripciones/usuario/${usuario.id}`);
      if (response.ok) {
        const data = await response.json();
        setInscripciones(data);
      }
    } catch (err) {
      console.error("Error cargando actividad:", err);
    } finally {
      setLoading(false);
    }
  }, [usuario?.id]);

  useEffect(() => {
    obtenerInscripciones();
  }, [obtenerInscripciones]);

  const eliminarInscripcion = async (idInscripcion, tituloCurso) => {
    if (!window.confirm(`¿Estás seguro de que deseas darte de baja del curso "${tituloCurso}"?`)) return;

    try {
      const response = await fetch(`http://localhost:8080/api/inscripciones/${idInscripcion}`, {
        method: 'DELETE'
      });
      if (response.ok) {
        setInscripciones(prev => prev.filter(insc => insc.id !== idInscripcion));
      }
    } catch (err) {
      console.error("Error al eliminar:", err);
    }
  };

  return (
    <div className="min-vh-100 bg-custom-cream">
      <Navbar />
      
      {/* HEADER CENTRADO */}
      <header className="py-5 bg-white border-bottom shadow-sm mb-5">
        <div className="container text-center">
          <p className="text-muted mb-0">Gestiona tus cursos y eventos desde un solo lugar.</p>
        </div>
      </header>

      <div className="container pb-5">
        {/* Usamos justify-content-center para que todo el contenido esté más al centro */}
        <div className="row g-4 justify-content-center">
          
          {/* PERFIL DEL USUARIO */}
          <div className="col-12 col-lg-3">
            <div className="card border-0 shadow-sm overflow-hidden text-center sticky-top" style={{ top: '110px', borderRadius: '15px' }}>
              <div className="bg-brand py-4"></div>
              <div className="px-4 pb-4" style={{ marginTop: '-40px' }}>
                <div className="bg-white rounded-circle mx-auto mb-3 d-flex align-items-center justify-content-center shadow-sm" 
                     style={{ width: '90px', height: '90px', fontSize: '2.2rem', color: '#7b2cbf', fontWeight: 'bold', border: '4px solid #fff' }}>
                  {usuario?.nombre?.charAt(0)}
                </div>
                <h5 className="fw-bold mb-1">{usuario?.nombre} {usuario?.apellido}</h5>
                
                {/* CORRECCIÓN: Rol con color visible y fondo suave */}
                <span className="badge bg-brand-light text-brand border border-brand-subtle mb-3 px-3 rounded-pill" 
                      style={{ fontSize: '0.75rem', backgroundColor: '#724e99' }}>
                  {usuario?.categoriaU?.nombre || "Estudiante"}
                </span>
                
                <div className="text-start mt-3 pt-3 border-top" style={{ fontSize: '0.85rem' }}>
                  <div className="mb-2 text-truncate">
                    <i className="bi bi-envelope text-muted me-2"></i> {usuario?.correo}
                  </div>
                  <div className="mb-2">
                    <i className="bi bi-person-vcard text-muted me-2"></i> {usuario?.rut}
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* MIS CURSOS INSCRITOS */}
          <div className="col-12 col-lg-8">
            <div className="d-flex align-items-center justify-content-between mb-4">
              <h4 className="fw-bold m-0" style={{ letterSpacing: '-1px' }}>Mis cursos</h4>
              <span className="badge bg-white text-dark shadow-sm px-3 py-2 border rounded-pill">
                {inscripciones.length} Activos
              </span>
            </div>
            
            {loading ? (
              <div className="text-center py-5"><div className="spinner-border text-brand"></div></div>
            ) : inscripciones.length > 0 ? (
              <div className="row g-4">
                {inscripciones.map((insc) => (
                  <div className="col-12 col-md-6" key={insc.id}>
                    <div className="curso-card bg-white shadow-sm h-100 border-0 overflow-hidden" style={{ borderRadius: '12px' }}>
                      <div className="curso-image-container position-relative">
                        <img 
                          src={insc.curso?.img || "https://via.placeholder.com/400x200?text=Muso+Arte"} 
                          alt={insc.curso?.titulo} 
                          className="w-100"
                          style={{ height: '160px', objectFit: 'cover' }}
                        />
                        <span className="category-badge-overlay shadow-sm">
                          {insc.curso?.categoriaA?.nombre || "Taller"}
                        </span>
                      </div>

                      <div className="p-4 text-start">
                        <h5 className="fw-bold mb-2">{insc.curso?.titulo}</h5>
                        <div className="text-muted mb-4" style={{ fontSize: '0.85rem' }}>
                          <div className="mb-1"><i className="bi bi-clock me-2"></i> {insc.curso?.horario}</div>
                          <div><i className="bi bi-calendar-check me-2"></i> Inscrito el {new Date(insc.fecha).toLocaleDateString()}</div>
                        </div>

                        <div className="d-flex gap-2 pt-3 border-top">
                          <button className="btn btn-brand flex-grow-1 rounded-0 fw-bold py-2">
                            Material clases
                          </button>
                          {/* BOTÓN DARSE DE BAJA REINTEGRADO */}
                          <button 
                            className="btn btn-outline-danger rounded-0 px-3"
                            onClick={() => eliminarInscripcion(insc.id, insc.curso?.titulo)}
                            title="Abandonar curso"
                          >
                            <i className="bi bi-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-5 bg-white rounded shadow-sm border">
                <p className="text-muted mb-4">Aún no te has inscrito en ningún taller.</p>
                <a href="/cursos" className="btn btn-brand fw-bold px-4 py-2">Explorar Catálogo</a>
              </div>
            )}
          </div>

        </div>
      </div>
    </div>
  );
};

export default TuActividad;