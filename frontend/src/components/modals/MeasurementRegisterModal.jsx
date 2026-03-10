import { useState } from "react";
import BaseModal, { ModalInputRow } from "./BaseModal";

export default function MeasurementRegisterModal({ onClose, onSubmit }) {
  const [locationSystemId, setLocationSystemId] = useState("");
  const [maxVal, setMaxVal] = useState("");
  const [minVal, setMinVal] = useState("");
  const [avgVal, setAvgVal] = useState("");

  const handleConfirm = () => {
    onSubmit({
      locationSystemId,
      maxVal,
      minVal,
      avgVal,
    });
  };

  return (
    <BaseModal
      title="측정값 등록"
      icon="add_location_alt"
      onClose={onClose}
      onConfirm={handleConfirm}
    >
      <ModalInputRow
        label="위치 시스템 ID"
        placeholder="위치 시스템 ID를 입력해주세요."
        value={locationSystemId}
        onChange={(e) => setLocationSystemId(e.target.value)}
        inputMode="numeric"
      />
      <ModalInputRow
        label="최댓값(max_val)"
        placeholder="최댓값을 입력해주세요."
        value={maxVal}
        onChange={(e) => setMaxVal(e.target.value)}
        inputMode="decimal"
      />
      <ModalInputRow
        label="최솟값(min_val)"
        placeholder="최솟값을 입력해주세요."
        value={minVal}
        onChange={(e) => setMinVal(e.target.value)}
        inputMode="decimal"
      />
      <ModalInputRow
        label="평균값(avg_val)"
        placeholder="평균값을 입력해주세요."
        value={avgVal}
        onChange={(e) => setAvgVal(e.target.value)}
        inputMode="decimal"
      />
    </BaseModal>
  );
}