package org.noostak.server.appointment.api;

import lombok.RequiredArgsConstructor;
import org.noostak.server.appointment.application.AppointmentCreateService;
import org.noostak.server.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.server.appointment.dto.response.AppointmentCreateResponse;
import org.noostak.server.global.success.core.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.noostak.server.appointment.common.AppointmentSuccessCode.SUCCESS_CREATE_APPOINTMENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class AppointmentController {

    private final AppointmentCreateService appointmentService;

    @PostMapping("/{groupId}/appointments")
    public ResponseEntity<SuccessResponse<AppointmentCreateResponse>> createAppointment(
//            @AuthenticationPrincipal Long userId,
            @PathVariable(name = "groupId") Long groupId,
            @RequestBody AppointmentCreateRequest request
    ) {
        Long userId = 125L;
        AppointmentCreateResponse appointmentCreateResponse = appointmentService.createAppointment(userId, groupId, request);
        return ResponseEntity.ok(SuccessResponse.of(SUCCESS_CREATE_APPOINTMENT, appointmentCreateResponse));
    }
}
