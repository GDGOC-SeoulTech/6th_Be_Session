package com.gdgoc.board.comment.domain;

import com.gdgoc.board.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    // TODO [세션 04 · 2-4] 게시글 연관관계 필드: @ManyToOne(fetch = LAZY) + @JoinColumn(name = "post_id", nullable = false) private Post post;

    // TODO [세션 04 · 2-4] 생성자 Comment(String content, Post post)
}
