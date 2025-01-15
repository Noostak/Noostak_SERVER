package org.noostak.server.appointment.common;

import org.noostak.server.global.error.core.BaseException;

public class AppointmentException extends BaseException {
    public AppointmentException(AppointmentErrorCode errorCode) {
        super(errorCode);
    }
}
