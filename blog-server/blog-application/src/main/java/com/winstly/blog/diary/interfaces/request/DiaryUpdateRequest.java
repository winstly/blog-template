package com.winstly.blog.diary.interfaces.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DiaryUpdateRequest {
    private String content;
    private String images;
    private LocalDate diaryDate;
}
