package org.noostak.server.appointment.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.server.global.error.core.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppointmentErrorCode implements ErrorCode {
    // 약속 소요 시간
    APPOINTMENT_DURATION_NEGATIVE(HttpStatus.BAD_REQUEST, "약속 소요 시간은 음수가 될 수 없습니다."),
    APPOINTMENT_DURATION_MAX(HttpStatus.BAD_REQUEST, "약속 소요 시간은 최대 1440(24시간)분을 초과할 수 없습니다."),
    APPOINTMENT_DURATION_INVALID_UNIT(HttpStatus.BAD_REQUEST, "약속 소요 시간은 60분 단위로 입력해야 합니다."),

    // 약속 생성
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."),
    APPOINTMENT_CATEGORY_NULL_OR_BLANK(HttpStatus.BAD_REQUEST, "약속 카테고리는 null이거나 공백일 수 없습니다."),
    APPOINTMENT_CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리입니다."),
    CREATE_APPOINTMENT_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "약속 생성을 위한 기본 구현이 제공되지 않습니다."),
    ;

    public static final String PREFIX = "[APPOINTMENT ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }

    @Override
    public int getStatusValue() {
        return status.value();
    }
}
