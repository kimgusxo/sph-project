package com.sph.project.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common Error
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Common-001", "입력값 오류"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Common-002", "서버 오류"),

    // Geocoding Error
    GEOCODING_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY, "Geocode-001", "지오코딩 결과 없음"),
    REVERSE_GEOCODING_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY, "Geocode-002", "리버스-지오코딩 결과 없음"),
    EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "Geocode-003", "외부 API 오류"),
    EXTERNAL_API_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "Geocode-004", "외부 API 타임아웃"),

    // Location Error
    LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Location-001", "위치가 존재하지 않음"),
    INVALID_COORDINATE(HttpStatus.UNPROCESSABLE_ENTITY, "Location-002", "좌표 범위 오류"),
    INVALID_LOCATION_INPUT(HttpStatus.BAD_REQUEST, "Location-003", "주소 또는 좌표 입력값 오류"),

    // Measurement Error
    MEASUREMENT_LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Measurement-001", "측정값을 등록할 위치가 존재하지 않음");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
