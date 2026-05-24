import { useState, useEffect, useCallback, useReducer, use } from 'react';
import Navbar from '../../componentes/Navbar/Navbar';
import '../Cursos/Cursos.css'; 
import { useAuth } from '../../utils/AuthProvider';
import { Link, useNavigate } from 'react-router-dom';
import { createCursoCard } from '../../objetos/Curso';
import PageNav from '../../componentes/PageNav/PageNav';

export default function TuActividad() {
  const { user } = useAuth();
  const [inscripciones, setInscripciones] = useState([]);
  const [pagina, setPagina] = useState(0);
  const [numPaginas, setNumPaginas] = useState(0);
  const [loading, setLoading] = useState(true);
  const [u, forceUpdate] = useReducer(x => x + 1, 0);
  const navigate = useNavigate();

  useEffect(() => {
    setLoading(true);
    const fetchInscripciones = async () => {
      try {
        const res = await fetch(`/api/cursos/me/i?page=${pagina}`);
        if (res.ok) {
          const data = await res.json();
          console.log(data);
          setPagina(data.number);
          setNumPaginas(data.totalPages);
          setInscripciones(data.content.map(
            i => createCursoCard(i)
          ));
        }
      } catch (err) {
        console.error("Error cargando actividad:", err);
      }
      setLoading(false);
    };
    fetchInscripciones();
  }, [u, pagina]);

  const desinscribir = async ({ id, nombre }) => {
    const userInput = prompt(`Escriba "Eliminar" para eliminar su inscripción al curso "${nombre}".`);
    if (userInput === null) return;
    if (userInput.toLowerCase() != "eliminar") {
      alert("Operación cancelada.");
      return;
    }
    try {
      setLoading(true);
      const res = await fetch(`/api/cursos/${id}/i`, { method: 'DELETE' });
      if (res.ok) {
        forceUpdate();
      } else {
        setLoading(false);
      }
    } catch (err) {
      console.error("Error al eliminar:", err);
      setLoading(false);
    }
  }

  if (!user | loading) {
    return (
      <div style={{ width: "5rem", height: "5rem", borderWidth: "0.5rem" }} className="m-auto spinner-border text-brand" />
    )
  }

  console.log(inscripciones);

  return <div className='container pb-5'>
    <header className="py-4 bg-white shadow-sm mb-4 rounded-3">
      <div className="container text-center">
        <p className="text-muted mb-0 fw-semibold">Gestiona tus cursos y eventos desde un solo lugar.</p>
      </div>
    </header>
    <div className='d-flex flex-row gap-4 justify-content-center'>
      <UsuarioCard user={user} />
      <div className='flex-grow-1 bg-white shadow-sm rounded-3' style={{ height: 'fit-content' }}>
        <span className="badge bg-light text-success shadow-sm px-3 py-1 mt-3 border border-success-subtle rounded-pill fs-6 fw-semibold">
          Cursos inscritos
        </span>
        {
          inscripciones.length == 0 ? (
            <div className="text-center m-3 py-5 bg-white rounded-3 border">
              <p className="text-muted mb-4">Aún no te has inscrito en ningún curso.</p>
              <Link to="/cursos" className="btn btn-brand fw-bold px-4 py-2">Explorar Catálogo</Link>
            </div>
          ) : (
            <div className='d-flex flex-column gap-3 p-3 m-3 bg-white rounded-3 border overflow-hidden'>
            {inscripciones.map((ins, i) => (
              <div className='col-12 d-flex rounded overflow-hidden border shadow-sm lift-on-hover' key={i}>
                <img src={ins.imagenUrl} className='border-end' style={{ width: '10rem', height: '6rem', objectFit: 'cover' }} />
                <div className='d-flex flex-column ps-3 m-auto ms-0 align-items-start gap-2'>
                  <h1 className='m-0 fs-3 text-muted'>{ins.nombre}</h1>
                  <div className='d-flex flex-row gap-2'>
                    <Link to={`/cursos/${ins.id}`} className='btn btn-brand-outline py-0 px-3 m-0 rounded-pill'>» Ir a curso</Link>
                    <button onClick={() => desinscribir(ins)} className='btn btn-outline-danger py-0 px-3 m-0 rounded-pill'>Eliminar</button>
                  </div>
                </div>
              </div>
            ))}
            {numPaginas > 1 &&
              <PageNav page={pagina} setPage={setPagina} numPages={numPaginas} />
            }
            </div>
          )
        }
      </div>
    </div>
  </div>

  return (
    <div className="min-vh-100 bg-custom-cream">
      <Navbar />
      
      <div className="container pb-5">
        {/* Usamos justify-content-center para que todo el contenido esté más al centro */}
        <div className="row g-4 justify-content-center">
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

function UsuarioCard({ user }) {
  // datos que siempre están
  const nombre = user.nombre;
  const apellido = user.apellido;
  const correo = user.correo;

  if (!nombre | !apellido | !correo) {
    console.error("UsuarioCard: Datos requeridos no presentes.");
    return null;
  }
  // datos que no siempre están
  const telefono = user.detalle?.numeroTelefonico;
  const genero = user.detalle?.genero;
  const region = user.detalle?.ubicacionUsuario?.region;

  let color; // #a0522d
  switch (user.rol?.tipoRol) {
    case "Administrador":
      color = "#592da0"
      break;
    default:
      color= "#a0522d"
  }

  return <div className='d-none d-xl-block p-0 card border-0 shadow-sm overflow-hidden text-center rounded-3' style={{ width: '22rem', height: 'fit-content' }}>
    <div className='py-5' style={{ backgroundColor: color }} />
    <div className='px-4 pb-4 d-flex flex-column' style={{ marginTop: '-40px' }}>
      <div
        className='bg-white rounded-circle me-auto mb-3 d-flex align-items-center justify-content-center shadow-sm fs-1 fw-bold'
        style={{ width: '90px', height: '90px', color: color }}
      >
        {nombre.charAt(0)}
      </div>
      <h5 className='fw-bold mb-1 me-auto'>{nombre} {apellido}</h5>
      <p className='me-auto'>
        <span
          className='badge shadow-sm sb-3 px-3 rounded-pill me-1'
          style={{ backgroundColor: color }}
        >
          {user.rol?.tipoRol}
        </span>
        <span className='text-muted fw-semibold' style={{ fontSize: '0.9rem' }}>
          {correo}
        </span>
      </p>
      <div className='border-top my-3' />
      <div style={{ display: 'contents', fontSize: '0.85rem' }}>
        { telefono && (
          <p className='text-muted me-auto fw-semibold'>
            <span
              className='badge shadow-sm px-2 me-1 rounded-pill'
              style={{ backgroundColor: color, minWidth: '6.5em' }}
            >
              Fono
            </span>
            { telefono }
          </p>
        ) }
        { genero && (
          <p className='text-muted me-auto fw-semibold'>
            <span
              className='badge shadow-sm px-2 me-1 rounded-pill'
              style={{ backgroundColor: color, minWidth: '6.5em' }}
            >
              Género
            </span>
            { genero }
          </p>
        ) }
        { region && (
          <p className='text-muted me-auto fw-semibold'>
            <span
              className='badge shadow-sm px-2 me-1 rounded-pill'
              style={{ backgroundColor: color, minWidth: '6.5em' }}
            >
              Región
            </span>
            { region }
          </p>
        ) }
      </div>
    </div>
  </div>
}