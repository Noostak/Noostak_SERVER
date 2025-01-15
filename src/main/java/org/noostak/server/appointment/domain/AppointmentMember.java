package org.noostak.server.appointment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noostak.server.appointment.domain.vo.AppointmentAvailability;
import org.noostak.server.global.entity.BaseTimeEntity;
import org.noostak.server.member.domain.Member;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noostak.server.appointment.domain.vo.AppointmentAvailability;
import org.noostak.server.global.entity.BaseTimeEntity;
import org.noostak.server.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private AppointmentAvailability appointmentAvailability;

    private LocalDateTime memberStartTime;

    private LocalDateTime memberEndTime;

    private boolean liked;

    private AppointmentMember(final Appointment appointment, final Member member, final AppointmentAvailability appointmentAvailability, final LocalDateTime memberStartTime, final LocalDateTime memberEndTime) {
        this.appointment = appointment;
        this.member = member;
        this.appointmentAvailability = appointmentAvailability;
        this.memberStartTime = memberStartTime;
        this.memberEndTime = memberEndTime;
        this.liked = false;
    }

    public static AppointmentMember of(final Appointment appointment, final Member member, final AppointmentAvailability appointmentAvailability, final LocalDateTime memberStartTime, final LocalDateTime memberEndTime) {
        return new AppointmentMember(appointment, member, appointmentAvailability, memberStartTime, memberEndTime);
    }
}
