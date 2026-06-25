export default function ActividadErrorPage({ error }) {
    return <div className="col-12 text-center py-5">
      <h3 className="text-danger">{error}</h3>
    </div>
}