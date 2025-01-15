package org.noostak.server.appointment.dto.response;

import java.time.LocalDateTime;

public record AppointmentDateTimeResponse(
        LocalDateTime date,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static AppointmentDateTimeResponse of(LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        return new AppointmentDateTimeResponse(date, startTime, endTime);
    }
}
