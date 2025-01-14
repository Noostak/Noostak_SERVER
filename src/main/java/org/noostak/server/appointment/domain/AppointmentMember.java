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
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private AppointmentAvailability appointmentAvailability;

    private LocalDateTime memberStartTime;

    private LocalDateTime memberEndTime;

    private boolean liked;

}
