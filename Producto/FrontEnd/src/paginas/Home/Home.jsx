import { Link } from "react-router-dom";
import { useState, useEffect } from "react"; // 1. Importamos hooks
import potteryImg from "../../assets/Pottery.jpg";
import violinImg from "../../assets/Violin.jpg";
import workshopImg from "../../assets/Workshop.jpg";
import "./Home.css";
import Navbar from "../../componentes/Navbar";

const Home = () => {
  // 2. Estado para almacenar el total
  const [totalCursos, setTotalCursos] = useState(0);

  // 3. Efecto para llamar a la API al cargar la página
  useEffect(() => {
    const obtenerEstadisticas = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/cursos");
        if (response.ok) {
          const data = await response.json();
          setTotalCursos(data.length); // Guardamos el conteo total
        }
      } catch (error) {
        console.error("Error al cargar estadísticas:", error);
      }
    };
    obtenerEstadisticas();
  }, []);

  return (
    <div className="min-vh-100 bg-custom-cream">
      {/* NAVBAR */}
      <Navbar />

      {/* HERO SECTION */}
      <main className="container py-5 mt-4">
        <div className="row align-items-center g-5">
          <div className="col-lg-6 pe-lg-5 text-lg-end">
            
            {/* --- ESTADÍSTICA DINÁMICA --- */}
            <div className="d-flex justify-content-lg-end mb-3">
              <div className="bg-white px-3 py-1 rounded-pill shadow-sm border d-inline-flex align-items-center">
                <span className="dot-online me-2"></span>
                <span className="text-brand fw-bold">{totalCursos}</span>
                <span className="text-muted small ms-2">Cursos disponibles</span>
              </div>
            </div>

            <span className="text-uppercase tracking-widest text-brand fw-bold mb-3 d-block small">
              Artes & Oficios
            </span>
            <h1
              className="fw-bold mb-4 text-dark"
              style={{
                letterSpacing: "-1px",
                lineHeight: "1.1",
                fontSize: "2.8rem",
              }}
            >
              Un lugar para <br /> crear con las manos.
            </h1>
            <p
              className="text-secondary mb-5 fw-light ms-lg-auto"
              style={{ maxWidth: "450px", fontSize: "1rem" }}
            >
              Muso es un espacio dedicado a preservar el valor de lo hecho a
              mano. Talleres, eventos y comunidad en torno a la cultura.
            </p>
            
            <div className="d-flex flex-wrap align-items-center justify-content-lg-end gap-3">
              
              {/* ENLACE A BIBLIOTECA */}
              <Link to="/biblioteca" className="text-decoration-none text-dark">
                <div className="d-flex align-items-center gap-3 cursor-pointer group-hover-effect">
                  <div className="play-circle shadow-sm">
                    <span className="play-triangle"></span>
                  </div>
                  <div className="d-flex flex-column text-start">
                    <span className="fw-bold small text-uppercase tracking-wider">
                      Biblioteca
                    </span>
                    <span
                      className="text-muted small"
                      style={{ fontSize: "0.6rem" }}
                    >
                      Recursos digitales
                    </span>
                  </div>
                </div>
              </Link>

              {/* ENLACE A EVENTOS */}
              <Link 
                to="/eventos" 
                className="btn btn-brand-outline rounded-pill px-4 fw-bold text-decoration-none"
              >
                Eventos
              </Link>

              {/* ENLACE A CURSOS */}
              <Link
                to="/cursos"
                className="btn btn-brand rounded-pill px-4 fw-bold shadow-sm"
              >
                Cursos
              </Link>
            </div>
          </div>

          {/* MOSAICO */}
          <div className="col-lg-6">
            <div className="image-grid position-relative">
              <div className="grid-bg-decorator"></div>
              <div className="grid-item item-1 shadow-xl">
                <img src={potteryImg} alt="Cerámica" className="hover-zoom" />
              </div>
              <div className="grid-item item-2 shadow-lg">
                <img src={violinImg} alt="Lutería" className="hover-zoom" />
              </div>
              <div className="grid-item item-3 shadow-lg">
                <img src={workshopImg} alt="Taller" className="hover-zoom" />
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Home;