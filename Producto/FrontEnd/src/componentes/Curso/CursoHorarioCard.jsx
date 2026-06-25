export default function CursoHorarioCard({
    diaDeSemana,
    horaDesde,
    horaHasta
}) {
    return (
        <div className="bg-light border rounded-2 p-2 shadow-sm lift-on-hover">
            <h3 className="fw-semibold fs-5">
                {diaDeSemana}
            </h3>
            <div style={{ minWidth: "7em" }}>
                <p className="text-muted fs-6">{horaDesde}</p>
                <div className="w-100 border-bottom" />
                <p className="text-muted fs-6">{horaHasta}</p>
            </div>
        </div>
    )
}