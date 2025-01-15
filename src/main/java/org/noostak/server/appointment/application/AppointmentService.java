package org.noostak.server.appointment.application;

import org.noostak.server.appointment.common.AppointmentErrorCode;
import org.noostak.server.appointment.common.AppointmentException;

public interface AppointmentService {

    default void createAppointment() {
        throw new AppointmentException(AppointmentErrorCode.CREATE_APPOINTMENT_NOT_SUPPORTED);
    }
}
