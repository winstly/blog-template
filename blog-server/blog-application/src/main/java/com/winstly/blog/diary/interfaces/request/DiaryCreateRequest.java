package com.winstly.blog.diary.interfaces.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DiaryCreateRequest {
    @NotBlank
    private String content;
    private String images;
    @NotNull
    private LocalDate diaryDate;
}
