package cn.yanshisan.blog.diary.domain.service;

import cn.yanshisan.blog.diary.domain.entity.Diary;
import cn.yanshisan.blog.diary.domain.factory.DiaryFactory;
import cn.yanshisan.blog.diary.domain.repository.DiaryRepository;
import cn.yanshisan.blog.shared.api.ErrorCode;
import cn.yanshisan.blog.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryManagementService {

    private final DiaryRepository diaryRepository;
    private final DiaryFactory diaryFactory;

    @Transactional
    public Diary createDiary(String content, String images, LocalDate diaryDate) {
        Diary diary = diaryFactory.createDiary(content, images, diaryDate);
        diaryRepository.save(diary);
        return diary;
    }

    @Transactional
    public void updateDiary(String diaryCode, String content, String images, LocalDate diaryDate) {
        Diary diary = findDiaryOrThrow(diaryCode);
        if (content != null) diary.setContent(content);
        if (images != null) diary.setImages(images);
        if (diaryDate != null) {
            diary.setDiaryDate(diaryDate);
            diary.setYear(diaryDate.getYear());
        }
        diaryRepository.update(diary);
    }

    @Transactional
    public void deleteDiary(String diaryCode) {
        Diary diary = findDiaryOrThrow(diaryCode);
        diaryRepository.delete(diary);
    }

    private Diary findDiaryOrThrow(String diaryCode) {
        return diaryRepository.findByCode(diaryCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND, diaryCode));
    }
}
