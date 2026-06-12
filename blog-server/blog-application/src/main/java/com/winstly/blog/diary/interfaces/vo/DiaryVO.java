package com.winstly.blog.diary.interfaces.vo;

import com.winstly.blog.diary.domain.entity.DiaryDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaryVO {
    private Long id;
    private String diaryCode;
    private String content;
    private String images;
    private LocalDate diaryDate;
    private Integer year;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public static DiaryVO from(DiaryDO diary) {
        DiaryVO vo = new DiaryVO();
        vo.setId(diary.getId());
        vo.setDiaryCode(diary.getDiaryCode());
        vo.setContent(diary.getContent());
        vo.setImages(diary.getImages());
        vo.setDiaryDate(diary.getDiaryDate());
        vo.setYear(diary.getYear());
        vo.setGmtCreate(diary.getGmtCreate());
        vo.setGmtModified(diary.getGmtModified());
        return vo;
    }
}
