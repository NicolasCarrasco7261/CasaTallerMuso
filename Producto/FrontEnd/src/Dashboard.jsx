import { useState, useEffect } from "react";
import Navbar from "./Navbar";

const Dashboard = () => {
  const [cursosActivos, setCursosActivos] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [curso, setCurso] = useState({
    titulo: "",
    descripcion: "",
    precio: "",
    cupos: "",
    img: "",
    horario: "",
    categoriaA: { id: "" },
  });

  const obtenerCursos = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/cursos");
      const data = await res.json();
      setCursosActivos(data);
    } catch (err) {
      console.error(err);
    }
  };

  const obtenerCategorias = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/categoriasA");
      const data = await res.json();
      setCategorias(data);
      if (data.length > 0)
        setCurso((prev) => ({ ...prev, categoriaA: { id: data[0].id } }));
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    obtenerCursos();
    obtenerCategorias();
  }, []);

  // --- NUEVA FUNCIÓN PARA DESACTIVAR/ACTIVAR ---
  const alternarEstadoCurso = async (id) => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/cursos/${id}/desactivar`,
        {
          method: "PATCH", // Coincide con tu @PatchMapping
          headers: {
            "Content-Type": "application/json",
          },
        },
      );

      if (response.ok) {
        // Si el backend responde bien, recargamos la lista para ver el cambio de color/estado
        obtenerCursos();
      } else {
        console.error("Error al intentar cambiar el estado");
        alert("Hubo un problema al actualizar el curso.");
      }
    } catch (err) {
      console.error("Error de conexión:", err);
    }
  };

  const eliminarCurso = async (id) => {
    if (window.confirm("¿Estás seguro de eliminar este taller?")) {
      try {
        await fetch(`http://localhost:8080/api/cursos/${id}`, {
          method: "DELETE",
        });
        obtenerCursos();
      } catch {
        alert("Error al eliminar");
      }
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "categoriaA") {
      setCurso({ ...curso, categoriaA: { id: parseInt(value) } });
    } else {
      setCurso({ ...curso, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("http://localhost:8080/api/cursos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(curso),
      });
      if (response.ok) {
        alert("¡Curso publicado!");
        setCurso({
          titulo: "",
          descripcion: "",
          precio: "",
          cupos: "",
          img: "",
          horario: "",
          categoriaA: { id: categorias[0]?.id || "" },
        });
        obtenerCursos();
      }
    } catch {
      alert("Error de conexión");
    }
  };

  return (
    <div className="min-vh-100 bg-white">
      <Navbar />

      <main className="container py-5" style={{ maxWidth: "850px" }}>
        <header className="mb-5 border-bottom pb-3 bg-transparent">
          <h2
            className="fw-bold text-dark m-0"
            style={{ letterSpacing: "-1px" }}
          >
            Administrador
          </h2>
          <p className="text-muted small mb-0">
            Panel de control de contenidos
          </p>
        </header>

        {/* Formulario */}
        <section className="mb-5">
          <h6 className="fw-bold mb-3 text-uppercase small text-secondary">
            Nuevo Curso
          </h6>
          <div className="p-4 border rounded-3 bg-white shadow-sm">
            <form onSubmit={handleSubmit}>
              <div className="row g-3">
                <div className="col-md-8">
                  <label className="form-label mb-1 small fw-bold">
                    TÍTULO
                  </label>
                  <input
                    type="text"
                    name="titulo"
                    className="form-control form-control-sm border-light-subtle"
                    value={curso.titulo}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="col-md-4">
                  <label className="form-label mb-1 small fw-bold">
                    CATEGORÍA
                  </label>
                  <select
                    name="categoriaA"
                    className="form-select form-select-sm border-light-subtle"
                    value={curso.categoriaA.id}
                    onChange={handleChange}
                  >
                    {categorias.map((cat) => (
                      <option key={cat.id} value={cat.id}>
                        {cat.nombre}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="col-12">
                  <label className="form-label mb-1 small fw-bold">
                    DESCRIPCIÓN
                  </label>
                  <textarea
                    name="descripcion"
                    className="form-control form-control-sm border-light-subtle"
                    rows="2"
                    value={curso.descripcion}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="col-md-4">
                  <label className="form-label mb-1 small fw-bold">
                    HORARIO
                  </label>
                  <input
                    type="text"
                    name="horario"
                    className="form-control form-control-sm border-light-subtle"
                    value={curso.horario}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="col-md-4">
                  <label className="form-label mb-1 small fw-bold">
                    PRECIO
                  </label>
                  <input
                    type="number"
                    name="precio"
                    className="form-control form-control-sm border-light-subtle"
                    value={curso.precio}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="col-md-4">
                  <label className="form-label mb-1 small fw-bold">CUPOS</label>
                  <input
                    type="number"
                    name="cupos"
                    className="form-control form-control-sm border-light-subtle"
                    value={curso.cupos}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="col-12">
                  <label className="form-label mb-1 small fw-bold">
                    URL IMAGEN
                  </label>
                  <input
                    type="text"
                    name="img"
                    className="form-control form-control-sm border-light-subtle"
                    value={curso.img}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="col-12 text-end mt-4">
                  <button
                    type="submit"
                    className="btn btn-brand btn-sm px-4 fw-bold shadow-sm py-2"
                  >
                    Publicar curso
                  </button>
                </div>
              </div>
            </form>
          </div>
        </section>

        {/* Tabla */}
        <section>
          <h6 className="fw-bold mb-3 text-uppercase small text-secondary">
            Cursos Activos
          </h6>
          <div className="border rounded-3 overflow-hidden shadow-sm">
            <table
              className="table table-hover mb-0 align-middle"
              style={{ fontSize: "0.85rem" }}
            >
              <thead
                className="bg-light text-muted"
                style={{ fontSize: "0.75rem" }}
              >
                <tr>
                  <th className="ps-3 py-3">TALLER / CATEGORÍA</th>
                  <th>PRECIO</th>
                  <th>CUPOS</th>
                  <th className="text-end pe-3">ACCIONES</th>
                </tr>
              </thead>
              <tbody>
                {cursosActivos.map((c) => (
                  <tr
                    key={c.id}
                    className={c.activo === false ? "opacity-50 bg-light" : ""}
                  >
                    <td className="ps-3">
                      <div className="fw-bold text-dark">{c.titulo}</div>
                      <div
                        className="text-brand x-small fw-bold text-uppercase"
                        style={{ fontSize: "0.65rem" }}
                      >
                        {c.categoriaA?.nombre || "General"}
                      </div>
                    </td>
                    <td>${Number(c.precio).toLocaleString()}</td>
                    <td>{c.cupos}</td>
                    <td className="text-end pe-3">
                      <button
                        onClick={() => alternarEstadoCurso(c.id)}
                        className="btn btn-link text-dark text-decoration-none p-0 me-3"
                        style={{ fontSize: "0.75rem" }}
                      >
                        {/* Cambiamos el icono y texto según el atributo 'activo' del curso */}
                        <i
                          className={`bi ${c.activo === false ? "bi-play-fill" : "bi-pause-fill"} me-1`}
                        ></i>
                        {c.activo === false ? "Activar" : "Desactivar"}
                      </button>

                      <button
                        onClick={() => eliminarCurso(c.id)}
                        className="btn btn-link text-danger text-decoration-none p-0"
                        style={{ fontSize: "0.75rem" }}
                      >
                        <i className="bi bi-trash3-fill me-1"></i> Eliminar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </main>
    </div>
  );
};

export default Dashboard;
