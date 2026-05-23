import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router";
import potteryImg from "../../assets/Pottery.jpg";
import violinImg from "../../assets/Violin.jpg";
import workshopImg from "../../assets/Workshop.jpg";
import "./Home.css";
import Navbar from "../../componentes/Navbar/Navbar";
import StatLabelBtn from "../../componentes/StatLabelBtn/StatLabelBtn"

export default function Home() {
  const [stats, setStats] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const obtenerEstadisticas = async () => {
      try {
        const response = await fetch("/api/stats");
        if (response.ok) {
          const data = await response.json();
          setStats(data);
        }
      } catch (error) {
        console.error("Error al cargar estadísticas:", error);
      }
    };
    obtenerEstadisticas();
  }, []);

  return (
    <div className="row align-items-start g-5">
      <div className="col-lg-6 pe-lg-5 text-lg-end pt-lg-3">
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
          {
            stats && <>
              {/*
              <StatLabelBtn
                number={stats.eventosDisponibles}
                text={stats.eventosDisponibles == 1 ? 'Evento disponible' : 'Eventos disponibles'}
                onClick={() => { navigate("/eventos") }}
              />
              */}
              <StatLabelBtn
                number={stats.cursosDisponibles}
                text={stats.cursosDisponibles == 1 ? 'Curso disponible' : 'Cursos disponibles'}
                onClick={() => { navigate("/cursos") }}
                forceShow
              />
            </>
          }
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
  );
};