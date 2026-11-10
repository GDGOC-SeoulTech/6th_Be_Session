package com.gdgoc.board.post.dto;

import com.gdgoc.board.post.domain.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public record PostPageResponse(
        List<PostResponse> posts,
        int page,
        int totalPages,
        long totalElements,
        boolean hasNext
) {
    public static PostPageResponse from(Page<Post> postPage) {
        return new PostPageResponse(
                postPage.getContent().stream().map(PostResponse::from).toList(),
                postPage.getNumber(),
                postPage.getTotalPages(),
                postPage.getTotalElements(),
                postPage.hasNext()
        );
    }
}