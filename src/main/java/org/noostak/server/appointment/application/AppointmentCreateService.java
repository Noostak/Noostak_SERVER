package org.noostak.server.appointment.application;

import lombok.RequiredArgsConstructor;
import org.noostak.server.appointment.common.AppointmentErrorCode;
import org.noostak.server.appointment.common.AppointmentException;
import org.noostak.server.appointment.domain.Appointment;
import org.noostak.server.appointment.domain.AppointmentDateTime;
import org.noostak.server.appointment.domain.AppointmentRepository;
import org.noostak.server.appointment.domain.vo.AppointmentStatus;
import org.noostak.server.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.server.appointment.dto.response.AppointmentCreateResponse;
import org.noostak.server.appointment.dto.response.AppointmentDateTimeResponse;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupRepository;
import org.noostak.server.member.domain.Member;
import org.noostak.server.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentCreateService implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public AppointmentCreateResponse createAppointment(Long userId, Long groupId, AppointmentCreateRequest request) {
        Member host = findHostById(userId);
        Group group = findGroupById(groupId);
        List<AppointmentDateTime> appointmentDateTimes = request.appointmentDateTimes();
        Appointment appointment = createAppointmentEntity(host, group, appointmentDateTimes, request);
        saveAppointment(appointment);
        return buildAppointmentCreateResponse(appointment, appointmentDateTimes);
    }

    private Member findHostById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.MEMBER_NOT_FOUND));
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.GROUP_NOT_FOUND));
    }

    private Appointment createAppointmentEntity(Member host, Group group, List<AppointmentDateTime> appointmentDateTimes, AppointmentCreateRequest request) {
        return Appointment.of(
                host,
                group,
                appointmentDateTimes,
                request.appointmentName(),
                request.duration(),
                request.category(),
                AppointmentStatus.PROGRESS
        );
    }

    private void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    private AppointmentCreateResponse buildAppointmentCreateResponse(Appointment appointment, List<AppointmentDateTime> appointmentDateTimes) {
        List<AppointmentDateTimeResponse> dateTimeResponses = createAppointmentDateTimeResponses(appointmentDateTimes);
        return createAppointmentCreateResponse(appointment, dateTimeResponses);
    }

    private List<AppointmentDateTimeResponse> createAppointmentDateTimeResponses(List<AppointmentDateTime> appointmentDateTimes) {
        return appointmentDateTimes.stream()
                .map(dateTime -> AppointmentDateTimeResponse.of(
                        dateTime.getDate(),
                        dateTime.getStartTime(),
                        dateTime.getEndTime()
                ))
                .collect(Collectors.toList());
    }

    private AppointmentCreateResponse createAppointmentCreateResponse(Appointment appointment, List<AppointmentDateTimeResponse> dateTimeResponses) {
        return AppointmentCreateResponse.of(
                appointment.getName().value(),
                appointment.getCategory().getMessage(),
                appointment.getDuration().value(),
                dateTimeResponses
        );
    }
}
