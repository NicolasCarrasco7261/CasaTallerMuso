import "./StatLabelBtn.css";

export default function StatLabelBtn({ number, text, onClick, forceShow=false }) {
    if (!forceShow && number <= 0) return <></>;
    return (
        <button onClick={onClick} className="stat-label-btn d-inline-flex rounded-pill align-items-center">
            <span className="dot-online me-2"></span>
            <span className="fw-bold">{number}</span>
            <span className="small ms-2">{text}</span>
        </button>
    )
}