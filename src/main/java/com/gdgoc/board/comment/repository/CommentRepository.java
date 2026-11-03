package com.gdgoc.board.comment.repository;

import com.gdgoc.board.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // TODO [세션 04 · 3-2] 게시글별 댓글 조회 findByPostId(postId)
    //   (dto 새 파일 2개도 이때 함께 만듭니다: comment/dto/CommentCreateRequest, comment/dto/CommentResponse)

    // TODO [세션 04 · 3-5] 게시글의 댓글 전체 삭제 deleteByPostId(postId)

    // TODO [세션 04 · 5-4] fetch join 으로 댓글 + 게시글 한 번에 조회 findAllWithPost() (@Query)
}
