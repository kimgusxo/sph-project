import "./BaseModal.css";

export function ModalIcon({ name, filled = false, className = "" }) {
  return (
    <span
      className={`material-symbols-rounded ${filled ? "is-filled" : ""} ${className}`}
    >
      {name}
    </span>
  );
}

export function ModalTabButton({ active, children, onClick }) {
  return (
    <button
      type="button"
      className={`modal-tab-button ${active ? "is-active" : ""}`}
      onClick={onClick}
    >
      {children}
    </button>
  );
}

export function ModalInputRow({
  label,
  placeholder,
  value,
  onChange,
  inputMode = "text",
}) {
  return (
    <label className="modal-form-row">
      <span className="modal-form-row__label">{label}</span>
      <input
        className="modal-input"
        type="text"
        inputMode={inputMode}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
      />
    </label>
  );
}

export function ModalSearchRow({ placeholder, value, onChange }) {
  return (
    <label className="modal-search-row">
      <span className="material-symbols-rounded modal-search-row__icon">
        search
      </span>
      <input
        className="modal-input modal-input--search"
        type="text"
        placeholder={placeholder}
        value={value}
        onChange={onChange}
      />
    </label>
  );
}

export default function BaseModal({
  title,
  icon,
  tabs,
  children,
  onClose,
  onConfirm,
}) {
  return (
    <div className="modal-overlay" onMouseDown={onClose}>
      <section className="modal-card" onMouseDown={(e) => e.stopPropagation()}>
        <header className="modal-card__header">
          <div className="modal-card__title-row">
            <ModalIcon name={icon} className="modal-card__title-icon" />
            <h2>{title}</h2>
          </div>

          {tabs ? <div className="modal-card__tabs">{tabs}</div> : null}
        </header>

        <div className="modal-card__body">{children}</div>

        <footer className="modal-card__footer">
          <button
            type="button"
            className="modal-action-button modal-action-button--secondary"
            onClick={onClose}
          >
            취소
          </button>

          <button
            type="button"
            className="modal-action-button modal-action-button--primary"
            onClick={onConfirm}
          >
            확인
          </button>
        </footer>
      </section>
    </div>
  );
}