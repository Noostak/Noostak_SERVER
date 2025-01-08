package org.noostak.server.group.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.noostak.server.appointment.domain.Appointment;
import org.noostak.server.global.entity.BaseTimeEntity;
import org.noostak.server.member.domain.Member;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@RequiredArgsConstructor
public class Group extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Embedded
    @AttributeOverride(name = "groupName", column = @Column(name = "group_name", nullable = false))
    private GroupName name;

    @Embedded
//    @AttributeOverride(name = ) // TODO: 컬렉션으로 관리해야할지, 하게되면 테이블 만들어야 됨. 중복 관리? 안하면 그냥 필드
    private InviteCodes inviteCodes = InviteCodes.init();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Member> members = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Appointment> appointments = new HashSet<>();

}
