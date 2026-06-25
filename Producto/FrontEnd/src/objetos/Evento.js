import { apiUrlFromPath } from "../utils/api";

export function createEventoHorario({
  fecha = "1970-01-01",
  hora = "00:00:00",
}) {
  const dateFecha = new Date(fecha);
  const dateHora = new Date("1970-01-01T" + hora);
  const fechaOpts = { day: "numeric", month: "long", year: "numeric" };
  const horaOpts = { hour: "2-digit", minute: "2-digit", hour12: true };
  return {
    fecha: dateFecha.toLocaleDateString("es-CL", fechaOpts),
    hora: dateHora.toLocaleTimeString("es-CL", horaOpts),
  };
}

export function createEventoCard({
  id = "",
  nombre = "",
  imagenStorageKey = "",
  precio = -1,
  cupos = -1,
  cuposRestantes = -1,
}) {
  const imagenUrl = apiUrlFromPath(`/api/files/img/${imagenStorageKey}`);
  return { id, nombre, imagenUrl, precio, cupos, cuposRestantes };
}

export function createEventoView({
  nombre = "",
  descripcion = "",
  contenido = "",
  imagenStorageKey = "",
  precio = -1,
  cupos = -1,
  cuposRestantes = -1,
  horarios = [],
}) {
  const imagenUrl = apiUrlFromPath(`/api/files/img/${imagenStorageKey}`);
  const mappedHorarios = horarios.map((h) => createEventoHorario(h));
  return {
    nombre,
    descripcion,
    contenido,
    imagenUrl,
    precio,
    cupos,
    cuposRestantes,
    horarios: mappedHorarios,
  };
}
