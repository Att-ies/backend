package com.sptp.backend.notification.repository;

import com.sptp.backend.common.entity.BaseEntity;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.web.dto.request.MemberUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    private String title;
    private String message;
    private String details;
    private Long data;
    private Boolean checked;
    private Long chatRoomId; // 채팅 알림이 한 채팅방에 대해 여러 개 생성되는 것 방지. chatRoomId 저장. 채팅 알림 아니면 전부 null.

    public void read() {

        this.checked = true;
    }

    public void updateMessage() {

        this.checked = false;
    }
}
