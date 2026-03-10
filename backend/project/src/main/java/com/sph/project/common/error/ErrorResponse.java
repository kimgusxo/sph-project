package com.sph.project.common.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "공통 에러 응답")
public class ErrorResponse {

    @Schema(description = "HTTP 상태 코드", example = "400")
    private final int status;

    @Schema(description = "에러 코드", example = "BAD_REQUEST")
    private final String code;

    @Schema(description = "에러 메시지", example = "잘못된 요청입니다.")
    private final String message;
}
