package org.noostak.server.appointment.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.server.global.success.handler.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppointmentSuccessCode implements SuccessCode {
    SUCCESS_CREATE_APPOINTMENT(HttpStatus.CREATED, "약속이 성공적으로 생성되었습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
