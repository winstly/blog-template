package cn.yanshisan.blog.diary.interfaces.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DiaryUpdateDTO {
    private String content;
    private String images;
    private LocalDate diaryDate;
}
