export default function EventoHorarioCard({ fecha, hora }) {
  return (
    <div className="bg-light border rounded-2 p-3 shadow-sm lift-on-hover">
      <h3 className="fw-semibold fs-5">{fecha}</h3>
      <p className="text-muted fs-6">{hora}</p>
    </div>
  );
}
