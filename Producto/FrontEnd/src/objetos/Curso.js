import { apiUrlFromPath } from "../utils/api";

export function createCursoHorario({
  diaDeSemana = "Lunes",
  horaDesde = "00:00:00",
  horaHasta = "00:00:00",
}) {
  const dateHoraDesde = new Date("1970-01-01T" + horaDesde);
  const dateHoraHasta = new Date("1970-01-01T" + horaHasta);
  const options = { hour: "2-digit", minute: "2-digit", hour12: true };
  return {
    diaDeSemana,
    horaDesde: dateHoraDesde.toLocaleTimeString("es-CL", options),
    horaHasta: dateHoraHasta.toLocaleTimeString("es-CL", options),
  };
}

export function createCursoCard({
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

export function createCursoView({
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
  const mappedHorarios = horarios.map((h) => createCursoHorario(h));
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
