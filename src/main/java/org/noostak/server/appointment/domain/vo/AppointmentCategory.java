package org.noostak.server.appointment.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.server.appointment.common.AppointmentErrorCode;
import org.noostak.server.appointment.common.AppointmentException;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AppointmentCategory {
    IMPORTANT("중요"),
    SCHEDULE("일정"),
    HOBBY("취미"),
    OTHER("기타");

    private final String message;

    public static AppointmentCategory from(String category) {
        validateCategory(category);

        return Arrays.stream(values())
                .filter(c -> c.name().equalsIgnoreCase(category.trim()) || c.message.equals(category.trim()))
                .findFirst()
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_CATEGORY_NOT_FOUND));
    }

    private static void validateCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_CATEGORY_NULL_OR_BLANK);
        }
    }
}
