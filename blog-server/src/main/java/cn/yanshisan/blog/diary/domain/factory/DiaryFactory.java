package cn.yanshisan.blog.diary.domain.factory;

import cn.yanshisan.blog.diary.domain.entity.Diary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class DiaryFactory {

    public Diary createDiary(String content, String images, LocalDate diaryDate) {
        Diary diary = new Diary();
        diary.setDiaryCode("diary_" + diaryDate.toString().replace("-", "") + "_" + UUID.randomUUID().toString().substring(0, 4));
        diary.setContent(content);
        diary.setImages(images);
        diary.setDiaryDate(diaryDate);
        diary.setYear(diaryDate.getYear());
        return diary;
    }
}
