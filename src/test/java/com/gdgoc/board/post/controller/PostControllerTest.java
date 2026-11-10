package com.gdgoc.board.post.controller;

import com.gdgoc.board.global.exception.CustomException;
import com.gdgoc.board.global.exception.ErrorCode;
import com.gdgoc.board.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    @DisplayName("제목이 비어 있으면 400 을 응답한다")
    void create_blankTitle() throws Exception {
        String body = """
                {"title": "", "content": "내용"}
                """;

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("제목은 필수입니다."));
    }

    @Test
    @DisplayName("없는 게시글을 조회하면 404 를 응답한다")
    void findById_notFound() throws Exception {
        given(postService.findById(999L)).willThrow(new CustomException(ErrorCode.POST_NOT_FOUND));

        mockMvc.perform(get("/posts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."));
    }
}
