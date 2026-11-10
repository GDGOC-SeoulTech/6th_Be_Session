package com.gdgoc.board.comment.service;

import com.gdgoc.board.comment.dto.CommentCreateRequest;
import com.gdgoc.board.comment.repository.CommentRepository;
import com.gdgoc.board.global.exception.CustomException;
import com.gdgoc.board.global.exception.ErrorCode;
import com.gdgoc.board.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("없는 게시글에 댓글을 달면 POST_NOT_FOUND 예외가 발생한다")
    void create_postNotFound() {
        // given
        given(postRepository.findById(1L)).willReturn(Optional.empty());
        CommentCreateRequest request = new CommentCreateRequest("댓글");

        // when & then
        assertThatThrownBy(() -> commentService.create(1L, request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.POST_NOT_FOUND);
    }
}
