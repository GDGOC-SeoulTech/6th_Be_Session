package com.gdgoc.board.post.service;

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
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("없는 게시글을 조회하면 POST_NOT_FOUND 예외가 발생한다")
    void findById_notFound() {
        // given : 1번 게시글이 없다고 가정한다
        given(postRepository.findById(1L)).willReturn(Optional.empty());

        // when & then : 조회하면 예외가 발생해야 한다
        assertThatThrownBy(() -> postService.findById(1L))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.POST_NOT_FOUND);
    }
}
