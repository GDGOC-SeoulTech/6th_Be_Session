package com.gdgoc.board.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    // TODO [세션 04 · 3-3] 필드 추가: CommentRepository, PostRepository

    // TODO [세션 04 · 3-3] 댓글 작성 create(postId, request) / 목록 findByPost(postId) / 삭제 delete(commentId)

    // TODO [세션 04 · 5-3] 전체 댓글 + 게시글 제목 조회 findAllWithPost()
    //   (dto 새 파일: comment/dto/CommentWithPostResponse) → 5-4 에서 fetch join 으로 수정
}
