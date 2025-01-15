package org.noostak.server.appointment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noostak.server.appointment.domain.vo.*;
import org.noostak.server.global.entity.BaseTimeEntity;
import org.noostak.server.member.domain.Member;
import org.noostak.server.group.domain.Group;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "appointment_id")
    private List<AppointmentDateTime> appointmentDateTimes;

    @Embedded
    @AttributeOverride(name = "appointmentName", column = @Column(name = "appointment_name"))
    private AppointmentName name;

    @Embedded
    private AppointmentDuration duration;

    @Embedded
    @AttributeOverride(name = "count", column = @Column(name = "appointment_member_count"))
    private AppointMemberCount memberCount;

    @Enumerated(EnumType.STRING)
    private AppointmentCategory category;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    private Appointment(final Member host, final Group group, final List<AppointmentDateTime> appointmentDateTimes, final String name, final Integer duration, final String category, final AppointmentStatus appointmentStatus) {
        this.host = host;
        this.group = group;
        this.appointmentDateTimes = appointmentDateTimes;
        this.name = AppointmentName.from(name);
        this.duration = AppointmentDuration.from(duration);
        this.memberCount = AppointMemberCount.from(1L);
        this.category = AppointmentCategory.from(category);
        this.appointmentStatus = appointmentStatus;
    }

    public static Appointment of(final Member host, final Group group, final List<AppointmentDateTime> appointmentDateTimes, final String name, final Integer duration, final String category, final AppointmentStatus appointmentStatus) {
        return new Appointment(host, group, appointmentDateTimes, name, duration, category, appointmentStatus);
    }
}
