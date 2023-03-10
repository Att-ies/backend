package com.sptp.backend.member_ask.repository;

import com.sptp.backend.common.entity.BaseEntity;
import com.sptp.backend.member.repository.Member;
import com.sptp.backend.member.web.dto.request.MemberAnswerRequest;
import com.sptp.backend.member.web.dto.request.MemberAskRequestDto;
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
public class MemberAsk extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_ask_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(length = 100)
    private String title;

    @Column(length = 3000)
    private String content;

    private String answer;
    private String status;

    public void updateMemberAsk(MemberAskRequestDto dto) {

        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

    public void updateMemberAnswer(MemberAnswerRequest dto) {

        this.answer = dto.getAnswer();
        this.status = MemberAskStatus.COMPLETE.name();
    }
}
