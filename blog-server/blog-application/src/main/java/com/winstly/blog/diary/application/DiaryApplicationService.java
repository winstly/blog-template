package com.winstly.blog.diary.application;

import com.winstly.blog.diary.domain.entity.DiaryDO;
import com.winstly.blog.diary.domain.repository.DiaryRepository;
import com.winstly.blog.diary.domain.service.DiaryManagementService;
import com.winstly.blog.diary.interfaces.request.DiaryCreateRequest;
import com.winstly.blog.diary.interfaces.request.DiaryUpdateRequest;
import com.winstly.blog.diary.interfaces.vo.DiaryVO;
import com.winstly.blog.shared.api.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryApplicationService {

    private final DiaryManagementService diaryManagementService;
    private final DiaryRepository diaryRepository;

    public DiaryVO createDiary(DiaryCreateRequest dto) {
        DiaryDO diary = diaryManagementService.createDiary(dto.getContent(), dto.getImages(), dto.getDiaryDate());
        return DiaryVO.from(diary);
    }

    public DiaryVO updateDiary(String diaryCode, DiaryUpdateRequest dto) {
        diaryManagementService.updateDiary(diaryCode, dto.getContent(), dto.getImages(), dto.getDiaryDate());
        return DiaryVO.from(diaryRepository.findByCode(diaryCode).orElseThrow());
    }

    public void deleteDiary(String diaryCode) {
        diaryManagementService.deleteDiary(diaryCode);
    }

    public PageResult<DiaryVO> listDiaries(int page, int pageSize, Integer year) {
        List<DiaryDO> diaries;
        long total;
        if (year != null) {
            diaries = diaryRepository.findByYear(year, page, pageSize);
            total = diaryRepository.countByYear(year);
        } else {
            diaries = diaryRepository.findAll(page, pageSize);
            total = diaryRepository.countAll();
        }
        List<DiaryVO> vos = diaries.stream().map(DiaryVO::from).toList();
        return new PageResult<>(vos, total, page, pageSize);
    }

    public List<Integer> listYears() {
        return diaryRepository.findDistinctYears();
    }
}
