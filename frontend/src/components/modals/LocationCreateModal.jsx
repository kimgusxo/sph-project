import { useState } from "react";
import BaseModal, {
  ModalInputRow,
  ModalSearchRow,
  ModalTabButton,
} from "./BaseModal";

export default function LocationCreateModal({ onClose, onSubmit }) {
  const [tab, setTab] = useState("coordinate");
  const [address, setAddress] = useState("");
  const [lon, setLon] = useState("");
  const [lat, setLat] = useState("");

  const handleConfirm = () => {
    if (tab === "coordinate") {
      onSubmit({
        mode: "coordinate",
        lon,
        lat,
      });
      return;
    }

    onSubmit({
      mode: "address",
      address,
    });
  };

  return (
    <BaseModal
      title="위치 생성"
      icon="add"
      onClose={onClose}
      onConfirm={handleConfirm}
      tabs={
        <>
          <ModalTabButton
            active={tab === "address"}
            onClick={() => setTab("address")}
          >
            주소로 생성
          </ModalTabButton>

          <ModalTabButton
            active={tab === "coordinate"}
            onClick={() => setTab("coordinate")}
          >
            좌표로 생성
          </ModalTabButton>
        </>
      }
    >
      {tab === "address" ? (
        <ModalSearchRow
          placeholder="도로명 주소를 입력해 주세요."
          value={address}
          onChange={(e) => setAddress(e.target.value)}
        />
      ) : (
        <>
          <ModalInputRow
            label="경도(x)"
            placeholder="경도를 입력해주세요."
            value={lon}
            onChange={(e) => setLon(e.target.value)}
            inputMode="decimal"
          />

          <ModalInputRow
            label="위도(y)"
            placeholder="위도를 입력해주세요."
            value={lat}
            onChange={(e) => setLat(e.target.value)}
            inputMode="decimal"
          />
        </>
      )}
    </BaseModal>
  );
}