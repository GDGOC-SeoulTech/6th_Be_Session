package com.gdgoc.board.comment.domain;

import com.gdgoc.board.global.common.BaseEntity;
import com.gdgoc.board.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    // TODO [세션 04 · 2-4] 게시글 연관관계 필드: @ManyToOne(fetch = LAZY) + @JoinColumn(name = "post_id", nullable = false) private Post post;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)              // 댓글(N) → 게시글(1)
    @JoinColumn(name = "post_id", nullable = false) // 외래키 컬럼 이름
    private Post post;                              // 숫자가 아니라 Post 객체

    public Comment(String content, Post post) {
        this.content = content;
        this.post = post;
    }
}
