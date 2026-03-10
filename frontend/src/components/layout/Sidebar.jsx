import "./Sidebar.css";

function SidebarButton({ icon, label, active, onClick, danger = false }) {
  return (
    <button
      type="button"
      className={`sidebar-button ${active ? "is-active" : ""} ${danger ? "is-danger" : ""}`}
      onClick={onClick}
    >
      <span className="material-symbols-rounded sidebar-button__icon">
        {icon}
      </span>
      <span>{label}</span>
    </button>
  );
}

export default function Sidebar({
  activeMenu,
  onOpenLocation,
  onOpenMeasurement,
  onOpenRadius,
  onClearRadius,
  hasRadiusQuery,
}) {
  return (
    <aside className="sidebar">
      <div className="sidebar__inner">
        <h1 className="sidebar__title">측정 관리 시스템</h1>

        <nav className="sidebar__nav">
          <SidebarButton
            icon="add"
            label="위치 생성"
            active={activeMenu === "location"}
            onClick={onOpenLocation}
          />

          <SidebarButton
            icon="add_location_alt"
            label="측정값 등록"
            active={activeMenu === "measurement"}
            onClick={onOpenMeasurement}
          />

          <SidebarButton
            icon="map_search"
            label="반경 조회"
            active={activeMenu === "radius"}
            onClick={onOpenRadius}
          />

          {hasRadiusQuery && (
            <SidebarButton
              icon="close"
              label="반경 표시 해제"
              onClick={onClearRadius}
              danger
            />
          )}
        </nav>
      </div>
    </aside>
  );
}