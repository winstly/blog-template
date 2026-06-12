package com.winstly.blog.diary.domain.factory;

import com.winstly.blog.diary.domain.entity.DiaryDO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class DiaryFactory {

    public DiaryDO createDiary(String content, String images, LocalDate diaryDate) {
        DiaryDO diary = new DiaryDO();
        diary.setDiaryCode("diary_" + diaryDate.toString().replace("-", "") + "_" + UUID.randomUUID().toString().substring(0, 4));
        diary.setContent(content);
        diary.setImages(images);
        diary.setDiaryDate(diaryDate);
        diary.setYear(diaryDate.getYear());
        return diary;
    }
}
