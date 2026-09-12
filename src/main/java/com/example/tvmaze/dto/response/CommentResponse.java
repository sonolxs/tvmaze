package com.example.tvmaze.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {
    private String comment;
    private Integer rating;
}