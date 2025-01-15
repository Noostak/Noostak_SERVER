package org.noostak.server.appointment.dto.request;

import org.noostak.server.appointment.domain.AppointmentDateTime;

import java.util.List;

public record AppointmentCreateRequest(
        String appointmentName,
        String category,
        Integer duration,
        List<AppointmentDateTime> appointmentDateTimes
) {
    public static AppointmentCreateRequest of(String appointmentName, String category, Integer duration, List<AppointmentDateTime> appointmentDateTimes) {
        return new AppointmentCreateRequest(appointmentName, category, duration, appointmentDateTimes);
    }
}
