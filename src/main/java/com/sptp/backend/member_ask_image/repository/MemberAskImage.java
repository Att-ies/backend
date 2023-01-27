package com.sptp.backend.member_ask_image.repository;

import com.sptp.backend.common.entity.BaseEntity;
import com.sptp.backend.member_ask.repository.MemberAsk;
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
public class MemberAskImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_ask_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_ask_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MemberAsk memberAsk;

    private String image;
}
