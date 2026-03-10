import { useState } from "react";
import Sidebar from "./components/layout/Sidebar";
import OpenLayersMap from "./components/map/OpenLayersMap";
import LocationCreateModal from "./components/modals/LocationCreateModal";
import MeasurementRegisterModal from "./components/modals/MeasurementRegisterModal";
import RadiusSearchModal from "./components/modals/RadiusSearchModal";
import {
  createLocation,
  searchAroundByAddress,
  searchAroundByCoordinate,
} from "./api/locationApi";
import { createMeasurement } from "./api/measurementApi";
import { toNumber } from "./utils/geo";

export default function App() {
  const [openModal, setOpenModal] = useState(null);
  const [mapLocations, setMapLocations] = useState([]);
  const [focusTarget, setFocusTarget] = useState(null);

  const closeModal = () => setOpenModal(null);

  const moveMapTo = ({ x, y, systemId = null }) => {
    if (x == null || y == null) return;

    setFocusTarget({
      key: Date.now(),
      x,
      y,
      systemId,
    });
  };

  const handleCreateLocation = async (payload) => {
    try {
      if (payload.mode === "address") {
        const address = String(payload.address ?? "").trim();

        if (!address) {
          alert("주소를 입력해주세요.");
          return;
        }

        await createLocation({ address });
      } else {
        const x = toNumber(payload.x);
        const y = toNumber(payload.y);

        if (x === null || y === null) {
          alert("경도(x), 위도(y)를 올바르게 입력해주세요.");
          return;
        }

        await createLocation({ x, y });
      }

      closeModal();
      alert("위치가 생성되었습니다.");
    } catch (error) {
      console.error(error);
      alert("위치 생성에 실패했습니다.");
    }
  };

  const handleCreateMeasurement = async (payload) => {
    try {
      const locationSystemId = toNumber(payload.locationSystemId);
      const maxVal = toNumber(payload.maxVal);
      const minVal = toNumber(payload.minVal);
      const avgVal = toNumber(payload.avgVal);

      if (
        locationSystemId === null ||
        maxVal === null ||
        minVal === null ||
        avgVal === null
      ) {
        alert("모든 값을 올바르게 입력해주세요.");
        return;
      }

      await createMeasurement({
        locationSystemId,
        maxVal,
        minVal,
        avgVal,
      });

      closeModal();
      alert("측정값이 등록되었습니다.");
    } catch (error) {
      console.error(error);
      alert("측정값 등록에 실패했습니다.");
    }
  };

  const handleSearchAround = async (payload) => {
    try {
      let response;

      if (payload.mode === "address") {
        const address = String(payload.address ?? "").trim();

        if (!address) {
          alert("주소를 입력해주세요.");
          return;
        }

        response = await searchAroundByAddress(address);
      } else {
        const x = toNumber(payload.x);
        const y = toNumber(payload.y);

        if (x === null || y === null) {
          alert("경도(x), 위도(y)를 올바르게 입력해주세요.");
          return;
        }

        response = await searchAroundByCoordinate(x, y);
      }

      const features = response?.features ?? [];
      setMapLocations(features);
      closeModal();

      if (features.length > 0) {
        const first = features[0];
        const [x, y] = first?.geometry?.coordinates ?? [];

        moveMapTo({
          x,
          y,
          systemId: first?.properties?.systemId ?? null,
        });
      }

      alert("반경 조회가 완료되었습니다.");
    } catch (error) {
      console.error(error);
      alert("반경 조회에 실패했습니다.");
    }
  };

  const handleClearRadius = () => {
    setMapLocations([]);
  };

  return (
    <>
      <div className="app-page">
        <Sidebar
          activeMenu={openModal}
          onOpenLocation={() => setOpenModal("location")}
          onOpenMeasurement={() => setOpenModal("measurement")}
          onOpenRadius={() => setOpenModal("radius")}
          onClearRadius={handleClearRadius}
          hasRadiusQuery={mapLocations.length > 0}
        />

        <main className="app-content">
          <OpenLayersMap
            locations={mapLocations}
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
          onSubmit={handleCreateMeasurement}
        />
      )}

      {openModal === "radius" && (
        <RadiusSearchModal
          onClose={closeModal}
          onSubmit={handleSearchAround}
        />
      )}
    </>
  );
}