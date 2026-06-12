package com.winstly.blog.diary.domain.service;

import com.winstly.blog.diary.domain.entity.DiaryDO;
import com.winstly.blog.diary.domain.factory.DiaryFactory;
import com.winstly.blog.diary.domain.repository.DiaryRepository;
import com.winstly.blog.shared.api.ErrorCode;
import com.winstly.blog.shared.exception.BusinessException;
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
    public DiaryDO createDiary(String content, String images, LocalDate diaryDate) {
        DiaryDO diary = diaryFactory.createDiary(content, images, diaryDate);
        diaryRepository.save(diary);
        return diary;
    }

    @Transactional
    public void updateDiary(String diaryCode, String content, String images, LocalDate diaryDate) {
        DiaryDO diary = findDiaryOrThrow(diaryCode);
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
        DiaryDO diary = findDiaryOrThrow(diaryCode);
        diaryRepository.delete(diary);
    }

    private DiaryDO findDiaryOrThrow(String diaryCode) {
        return diaryRepository.findByCode(diaryCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND, diaryCode));
    }
}
