package org.noostak.server.appointment.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noostak.server.appointment.common.AppointmentSuccessCode;
import org.noostak.server.appointment.domain.AppointmentDateTime;
import org.noostak.server.appointment.domain.AppointmentRepositoryTest;
import org.noostak.server.appointment.dto.request.AppointmentCreateRequest;
import org.noostak.server.group.domain.Group;
import org.noostak.server.group.domain.GroupRepositoryTest;
import org.noostak.server.group.domain.vo.GroupImageUrl;
import org.noostak.server.group.domain.vo.GroupName;
import org.noostak.server.member.domain.Member;
import org.noostak.server.member.domain.MemberRepositoryTest;
import org.noostak.server.member.domain.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepositoryTest memberRepository;

    @Autowired
    private GroupRepositoryTest groupRepository;

    @Autowired
    private AppointmentRepositoryTest appointmentRepository;

    private Long savedGroupId;

    private Long savedMemberId;

    private static final LocalDateTime FIXED_DATE_TIME = LocalDateTime.of(2025, 1, 1, 12, 0);

    @BeforeEach
    void setUp() {
        groupRepository.deleteAll();
        memberRepository.deleteAll();
        appointmentRepository.deleteAll();

        Member savedMember = saveMember("firstMember", "https://example.com/profile1.jpg", "123456", "refreshToken1");
        Group savedGroup = saveGroup(savedMember.getMemberId(), "Group 1", "https://example.com/group1.png", "123ABC");

        savedGroupId = savedGroup.getGroupId();
        savedMemberId = savedMember.getMemberId();
    }

    @Test
    @DisplayName("성공적으로 약속을 생성")
    void shouldCreateAppointmentSuccessfully() throws Exception {
        // given
        Long groupId = savedGroupId;
        String appointmentName = "약속";
        String category = "중요";
        int duration = 60;
        List<AppointmentDateTime> appointmentDateTimes = List.of(createSampleAppointmentDateTime());

        AppointmentCreateRequest appointmentRequest = createSampleAppointmentRequest(
                appointmentName,
                category,
                duration,
                appointmentDateTimes
        );

        // when & then
        mockMvc.perform(post("/api/v1/groups/{groupId}/appointments", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(AppointmentSuccessCode.SUCCESS_CREATE_APPOINTMENT.getMessage()));
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

    private AppointmentDateTime createSampleAppointmentDateTime() {
        return AppointmentDateTime.of(
                FIXED_DATE_TIME,
                FIXED_DATE_TIME,
                FIXED_DATE_TIME
        );
    }

    private AppointmentCreateRequest createSampleAppointmentRequest(
            String appointmentName,
            String category,
            int duration,
            List<AppointmentDateTime> appointmentDateTimes
    ) {
        return AppointmentCreateRequest.of(
                appointmentName,
                category,
                duration,
                appointmentDateTimes
        );
    }
}

