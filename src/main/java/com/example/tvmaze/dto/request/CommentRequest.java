package com.example.tvmaze.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {

    @Schema(description = "Comment text", example = "Great show, highly recommended")
    @NotBlank(message = "Comment must not be blank")
    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    private String comment;

    @Schema(description = "Rating from 0 to 5", example = "4")
    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
}