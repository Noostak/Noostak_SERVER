package org.noostak.server.appointment.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noostak.server.appointment.common.AppointmentErrorCode;
import org.noostak.server.appointment.common.AppointmentException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointmentDurationTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("초기 약속 시간이 0인 객체 생성")
        void shouldCreateWithZeroInitialValue() {
            // Given
            Integer initialDuration = 0;

            // When
            AppointmentDuration duration = AppointmentDuration.from(initialDuration);

            // Then
            assertThat(duration.value()).isEqualTo(0);
        }

        @Test
        @DisplayName("유효한 약속 시간을 가진 객체 생성")
        void shouldCreateWithValidDuration() {
            // Given
            Integer validDuration = 120;

            // When
            AppointmentDuration duration = AppointmentDuration.from(validDuration);

            // Then
            assertThat(duration.value()).isEqualTo(120);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("약속 시간이 음수일 경우 예외 발생")
        void shouldThrowExceptionWhenDurationIsNegative() {
            // Given
            Integer invalidDuration = -10;

            // When & Then
            assertThatThrownBy(() -> AppointmentDuration.from(invalidDuration))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_DURATION_NEGATIVE.getMessage());
        }

        @Test
        @DisplayName("약속 시간이 1440분을 초과할 경우 예외 발생")
        void shouldThrowExceptionWhenDurationExceedsMaxLimit() {
            // Given
            Integer invalidDuration = 1500;

            // When & Then
            assertThatThrownBy(() -> AppointmentDuration.from(invalidDuration))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_DURATION_MAX.getMessage());
        }

        @Test
        @DisplayName("약속 시간이 60분 단위가 아닐 경우 예외 발생")
        void shouldThrowExceptionWhenDurationIsNotMultipleOfSixty() {
            // Given
            Integer invalidDuration = 125;

            // When & Then
            assertThatThrownBy(() -> AppointmentDuration.from(invalidDuration))
                    .isInstanceOf(AppointmentException.class)
                    .hasMessageContaining(AppointmentErrorCode.APPOINTMENT_DURATION_INVALID_UNIT.getMessage());
        }
    }
}
