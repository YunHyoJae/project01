package com.moocafe.project.entity;

import com.moocafe.project.dto.FranchiseReplySaveDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FranchiseReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;
    @OneToOne(fetch=FetchType.LAZY)
    private FranchiseBoard board;
    @ManyToOne(fetch=FetchType.LAZY)
    private Member member;
    private String content;
    private String state;

    public FranchiseReply saveAsSaveDto(FranchiseReplySaveDto dto, String state) {
        return FranchiseReply.builder().id(this.id)
                .board(this.board)
                .member(dto.getMember())
                .content(dto.getContent())
                .state(state)
                .build();
    }
}
