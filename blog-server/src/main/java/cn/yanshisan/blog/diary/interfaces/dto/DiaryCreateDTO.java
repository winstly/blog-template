package cn.yanshisan.blog.diary.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DiaryCreateDTO {
    @NotBlank
    private String content;
    private String images;
    @NotNull
    private LocalDate diaryDate;
}
