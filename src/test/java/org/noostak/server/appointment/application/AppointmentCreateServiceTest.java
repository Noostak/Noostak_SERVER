package org.noostak.server.appointment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.noostak.server.appointment.common.AppointmentErrorCode;
import org.noostak.server.appointment.common.AppointmentException;
import org.noostak.server.appointment.domain.AppointmentDateTime;
import org.noostak.server.appointment.domain.AppointmentRepositoryTest;
import org.noostak.server.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.server.appointment.dto.response.AppointmentCreateResponse;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupRepositoryTest;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupName;
import org.noostak.server.member.domain.Member;
import org.noostak.server.member.domain.MemberRepositoryTest;
import org.noostak.server.member.domain.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
class AppointmentCreateServiceTest {

    @Autowired
    private AppointmentRepositoryTest appointmentRepository;

    @Autowired
    private GroupRepositoryTest groupRepository;

    @Autowired
    private MemberRepositoryTest memberRepository;

    @Autowired
    private AppointmentCreateService appointmentCreateService;

    private Long savedGroupId;
    private Long savedMemberId;

    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll();
        groupRepository.deleteAll();
        memberRepository.deleteAll();

        Member savedMember = saveMember("firstMember", "https://noostak.s3.ap-northeast-2.amazonaws.com/group-images/1.jpg", "123456", "refreshToken1");
        Group savedGroup = saveGroup(1L, "Group 1", "https://bucket-noostak.s3.ap-northeast-2.amazonaws.com/images/group1.png", "123ABC");

        savedGroupId = savedGroup.getGroupId();
        savedMemberId = savedMember.getMemberId();
    }

    @Nested
    @DisplayName("성공 케이스")
    class Success {

        @Test
        @DisplayName("약속 생성 성공")
        void shouldCreateAppointment() {
            // Given
            String appointmentName = "Study Group";
            String category = "중요";
            Integer duration = 120;
            List<AppointmentDateTime> dateTimeRequests = List.of(
                    AppointmentDateTime.of(
                            LocalDateTime.now(),
                            LocalDateTime.now().withHour(10).withMinute(0),
                            LocalDateTime.now().withHour(12).withMinute(0)
                    ),
                    AppointmentDateTime.of(
                            LocalDateTime.now().plusDays(1).withHour(14).withMinute(0),
                            LocalDateTime.now().plusDays(1).withHour(16).withMinute(0),
                            LocalDateTime.now().plusDays(1).withHour(18).withMinute(0)
                    )
            );


            AppointmentCreateRequest request = AppointmentCreateRequest.of(appointmentName, category, duration, dateTimeRequests);

            // When
            AppointmentCreateResponse response = appointmentCreateService.createAppointment(savedMemberId, savedGroupId, request);

            // Then
            assertThat(response.appointmentName()).isEqualTo(appointmentName);
            assertThat(response.category()).isEqualTo(category);
            assertThat(response.duration()).isEqualTo(duration);
            assertThat(response.appointmentDateTimes()).hasSize(2);
        }
    }

    @ParameterizedTest
    @DisplayName("약속 생성 실패 - 존재하지 않는 그룹")
    @CsvSource({
            "999",
            "1000",
            "1001"
    })
    void shouldFailToCreateAppointmentWhenGroupDoesNotExist(Long invalidGroupId) {
        // Given
        String appointmentName = "Study Group";
        String category = "Study";
        int duration = 120;
        List<AppointmentDateTime> appointmentDateTimes = List.of(
                AppointmentDateTime.of(
                        LocalDateTime.now().withHour(10).withMinute(0),
                        LocalDateTime.now().withHour(12).withMinute(0),
                        LocalDateTime.now().withHour(14).withMinute(0)
                )
        );
        AppointmentCreateRequest request = AppointmentCreateRequest.of(appointmentName, category, duration, appointmentDateTimes);

        // When & Then
        assertThatThrownBy(() -> appointmentCreateService.createAppointment(savedMemberId, invalidGroupId, request))
                .isInstanceOf(AppointmentException.class)
                .hasMessage(AppointmentErrorCode.GROUP_NOT_FOUND.getMessage());
    }

    @ParameterizedTest
    @DisplayName("약속 생성 실패 - 존재하지 않는 사용자")
    @CsvSource({
            "999",
            "1000",
            "1001"
    })
    void shouldFailToCreateAppointmentWhenUserDoesNotExist(Long invalidUserId) {
        // Given
        String appointmentName = "Study Group";
        String category = "Study";
        int duration = 120;
        List<AppointmentDateTime> appointmentDateTimes = List.of(
                AppointmentDateTime.of(
                        LocalDateTime.now().withHour(10).withMinute(0),
                        LocalDateTime.now().withHour(12).withMinute(0),
                        LocalDateTime.now().withHour(14).withMinute(0)
                )
        );
        AppointmentCreateRequest request = AppointmentCreateRequest.of(appointmentName, category, duration, appointmentDateTimes);

        // When & Then
        assertThatThrownBy(() -> appointmentCreateService.createAppointment(invalidUserId, savedGroupId, request))
                .isInstanceOf(AppointmentException.class)
                .hasMessage(AppointmentErrorCode.MEMBER_NOT_FOUND.getMessage());
    }


    private Member saveMember(String name, String imageUrl, String socialId, String refreshToken) {
        return memberRepository.save(
                Member.of(
                        MemberName.from(name),
                        ProfileImageUrl.from(imageUrl),
                        AccountStatus.ACTIVE,
                        SocialType.GOOGLE,
                        SocialId.from(socialId),
                        refreshToken
                )
        );
    }

    private Group saveGroup(Long leaderId, String groupName, String imageUrl, String inviteCode) {
        return groupRepository.save(
                Group.of(
                        leaderId,
                        GroupName.from(groupName),
                        GroupImageUrl.from(imageUrl),
                        inviteCode
                )
        );
    }
}
