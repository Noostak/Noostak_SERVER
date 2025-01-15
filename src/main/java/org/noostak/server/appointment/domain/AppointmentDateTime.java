package org.noostak.server.appointment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_date")
    private LocalDateTime date;

    @Column(name = "appointment_start_time")
    private LocalDateTime startTime;

    @Column(name = "appointment_end_time")
    private LocalDateTime endTime;

    private AppointmentDateTime(final LocalDateTime date, final LocalDateTime startTime, final LocalDateTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static AppointmentDateTime of(final LocalDateTime date, final LocalDateTime startTime, final LocalDateTime endTime) {
        return new AppointmentDateTime(date, startTime, endTime);
    }
}
