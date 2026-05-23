export default function PageNav({ page, setPage, numPages }) {
  const handleSetPage = (p) => {
    if (p >= 0 && p < numPages) {
      setPage(p);
    } else {
      console.warn(`Se intentó buscar en página inválida (${p}/${numPages})`);
    }
  };

  const numPagBtns = Math.min(5, numPages);
  const distToEnd = numPages - page;
  const pagBtnVals = Array.from(
    { length: numPagBtns },
    (_, i) => (i + page) - Math.min(page, 1) - Math.max(-distToEnd + numPagBtns - 1, 0)
  );
  pagBtnVals.splice(5);

  return (
    <div style={{ width: "fit-content" }} className="d-flex gap-4 m-auto">
      <button
        className="btn btn-brand rounded-2"
        onClick={() => handleSetPage(page - 1)}
        disabled={page <= 0}
      >
        Anterior
      </button>
      <div className="d-flex gap-3">
        {
        pagBtnVals
        .map((v, index) => (
          <button
          key={index}
          className={`btn rounded-2 ${v == page ? "btn-brand" : "btn-brand-outline"}`}
          onClick={() => handleSetPage(v)}
          >
          {v + 1}
          </button>
        ))
        }
      </div>
      <button
        className="btn btn-brand rounded-2"
        onClick={() => handleSetPage(page + 1)}
        disabled={page >= numPages - 1}
      >
        Siguiente
      </button>
    </div>
  )
}