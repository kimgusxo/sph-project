import { useState } from "react";
import Sidebar from "./components/layout/Sidebar";
import OpenLayersMap from "./components/map/OpenLayersMap";
import LocationCreateModal from "./components/modals/LocationCreateModal";
import MeasurementRegisterModal from "./components/modals/MeasurementRegisterModal";
import RadiusSearchModal from "./components/modals/RadiusSearchModal";
import {
  DEFAULT_RADIUS_METERS,
  INITIAL_LOCATIONS,
  resolveMockAddress,
} from "./data/mockData";
import { toNumber } from "./utils/geo";

export default function App() {
  const [locations, setLocations] = useState(INITIAL_LOCATIONS);
  const [openModal, setOpenModal] = useState(null);
  const [radiusQuery, setRadiusQuery] = useState(null);
  const [focusTarget, setFocusTarget] = useState(null);

  const closeModal = () => setOpenModal(null);

  const moveMapTo = ({ lon, lat, locationId = null }) => {
    setFocusTarget({
      key: Date.now(),
      lon,
      lat,
      locationId,
    });
  };

  const handleCreateLocation = (payload) => {
    if (payload.mode === "coordinate") {
      const lon = toNumber(payload.lon);
      const lat = toNumber(payload.lat);

      if (lon === null || lat === null) {
        alert("경도와 위도를 올바르게 입력해주세요.");
        return;
      }

      const nextId =
        locations.length > 0
          ? Math.max(...locations.map((item) => item.id)) + 1
          : 1;

      const newLocation = {
        id: nextId,
        name: `측정 지점 ${nextId}`,
        address: `좌표 생성 (${lon}, ${lat})`,
        lon,
        lat,
        measurement: null,
      };

      setLocations((prev) => [...prev, newLocation]);
      closeModal();
      moveMapTo({ lon, lat, locationId: nextId });
      return;
    }

    const resolved = resolveMockAddress(payload.address);

    if (!resolved) {
      alert(
        "현재 주소 생성은 mock 데이터 기반입니다.\n예: 서울특별시 중구 세종대로 110"
      );
      return;
    }

    const nextId =
      locations.length > 0
        ? Math.max(...locations.map((item) => item.id)) + 1
        : 1;

    const newLocation = {
      id: nextId,
      name: resolved.name ?? `측정 지점 ${nextId}`,
      address: resolved.address,
      lon: resolved.lon,
      lat: resolved.lat,
      measurement: null,
    };

    setLocations((prev) => [...prev, newLocation]);
    closeModal();
    moveMapTo({
      lon: resolved.lon,
      lat: resolved.lat,
      locationId: nextId,
    });
  };

  const handleRegisterMeasurement = (payload) => {
    const locationId = toNumber(payload.locationId);
    const maxVal = toNumber(payload.maxVal);
    const minVal = toNumber(payload.minVal);
    const avgVal = toNumber(payload.avgVal);

    if (
      locationId === null ||
      maxVal === null ||
      minVal === null ||
      avgVal === null
    ) {
      alert("모든 값을 숫자로 입력해주세요.");
      return;
    }

    const target = locations.find((item) => item.id === locationId);

    if (!target) {
      alert("해당 위치 ID가 존재하지 않습니다.");
      return;
    }

    setLocations((prev) =>
      prev.map((item) =>
        item.id === locationId
          ? {
              ...item,
              measurement: {
                maxVal,
                minVal,
                avgVal,
              },
            }
          : item
      )
    );

    closeModal();
    moveMapTo({
      lon: target.lon,
      lat: target.lat,
      locationId: target.id,
    });
  };

  const handleRadiusSearch = (payload) => {
    if (payload.mode === "coordinate") {
      const lon = toNumber(payload.lon);
      const lat = toNumber(payload.lat);

      if (lon === null || lat === null) {
        alert("경도와 위도를 올바르게 입력해주세요.");
        return;
      }

      const nextQuery = {
        lon,
        lat,
        label: `좌표 조회 (${lon}, ${lat})`,
        radiusMeters: DEFAULT_RADIUS_METERS,
      };

      setRadiusQuery(nextQuery);
      closeModal();
      moveMapTo({ lon, lat });
      return;
    }

    const resolved = resolveMockAddress(payload.address);

    if (!resolved) {
      alert(
        "현재 주소 조회는 mock 데이터 기반입니다.\n예: 서울특별시 중구 세종대로 110"
      );
      return;
    }

    const nextQuery = {
      lon: resolved.lon,
      lat: resolved.lat,
      label: resolved.address,
      radiusMeters: DEFAULT_RADIUS_METERS,
    };

    setRadiusQuery(nextQuery);
    closeModal();
    moveMapTo({ lon: resolved.lon, lat: resolved.lat });
  };

  return (
    <>
      <div className="app-page">
        <Sidebar
          activeMenu={openModal}
          onOpenLocation={() => setOpenModal("location")}
          onOpenMeasurement={() => setOpenModal("measurement")}
          onOpenRadius={() => setOpenModal("radius")}
          onClearRadius={() => setRadiusQuery(null)}
          hasRadiusQuery={Boolean(radiusQuery)}
        />

        <main className="app-content">
          <OpenLayersMap
            locations={locations}
            radiusQuery={radiusQuery}
            focusTarget={focusTarget}
          />
        </main>
      </div>

      {openModal === "location" && (
        <LocationCreateModal
          onClose={closeModal}
          onSubmit={handleCreateLocation}
        />
      )}

      {openModal === "measurement" && (
        <MeasurementRegisterModal
          onClose={closeModal}
          onSubmit={handleRegisterMeasurement}
        />
      )}

      {openModal === "radius" && (
        <RadiusSearchModal
          onClose={closeModal}
          onSubmit={handleRadiusSearch}
        />
      )}
    </>
  );
}