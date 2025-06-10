package com.moocafe.project.entity;

import com.moocafe.project.dao.FranchiseReplyDao;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name="franchiseBoard")
@EntityListeners(AuditingEntityListener.class)
public class FranchiseBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;
    private String name;
    private String tel;
    private String email;
    private String space;
    private String store;
    private String time;
    private String course;
    private String content;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate = LocalDateTime.now();
    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private FranchiseReply reply;
    private String state;

    public FranchiseBoard maskAsState(String state) {
        return FranchiseBoard.builder().id(this.id).name(this.name)
                .tel(this.tel).email(this.email).store(this.store).space(this.space)
                .time(this.time).course(this.course).content(this.content)
                .state(state)
                .build();
    }
}
