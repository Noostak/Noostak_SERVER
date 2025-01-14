package org.noostak.server.infra.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.server.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3UploadErrorCode implements ErrorCode {
    INVALID_EXTENSION(HttpStatus.BAD_REQUEST, "이미지 확장자는 jpg, png, webp만 가능합니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지 사이즈는 5MB를 넘을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public int getStatusValue() {
        return status.value();
    }
}