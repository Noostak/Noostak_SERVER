package org.noostak.server.appointment.dto.response;

import java.util.List;

public record AppointmentCreateResponse(
        String appointmentName,
        String category,
        Integer duration,
        List<AppointmentDateTimeResponse> appointmentDateTimes
) {
    public static AppointmentCreateResponse of(String appointmentName, String category, Integer duration, List<AppointmentDateTimeResponse> appointmentDateTimes) {
        return new AppointmentCreateResponse(appointmentName, category, duration, appointmentDateTimes);
    }
}
