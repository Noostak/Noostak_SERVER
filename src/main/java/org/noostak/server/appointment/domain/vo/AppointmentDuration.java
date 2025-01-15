package org.noostak.server.appointment.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.noostak.server.appointment.common.AppointmentErrorCode;
import org.noostak.server.appointment.common.AppointmentException;

@Embeddable
@EqualsAndHashCode
public class AppointmentDuration {

    private final Integer duration;

    protected AppointmentDuration() {
        this.duration = 0;
    }

    private AppointmentDuration(Integer duration) {
        validate(duration);
        this.duration = duration;
    }

    public static AppointmentDuration from(Integer duration) {
        return new AppointmentDuration(duration);
    }

    public Integer value() {
        return duration;
    }

    private void validate(Integer duration) {
        validateNonNegative(duration);
        validateMaxLimit(duration);
        validateMultipleOfSixty(duration);
    }

    private void validateNonNegative(Integer duration) {
        if (duration < 0) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_DURATION_NEGATIVE);
        }
    }

    private void validateMaxLimit(Integer duration) {
        if (duration > 1440) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_DURATION_MAX);
        }
    }

    private void validateMultipleOfSixty(Integer duration) {
        if (duration % 60 != 0) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_DURATION_INVALID_UNIT);
        }
    }
}
