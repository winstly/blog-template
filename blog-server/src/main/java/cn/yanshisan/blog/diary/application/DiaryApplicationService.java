package cn.yanshisan.blog.diary.application;

import cn.yanshisan.blog.diary.domain.entity.Diary;
import cn.yanshisan.blog.diary.domain.repository.DiaryRepository;
import cn.yanshisan.blog.diary.domain.service.DiaryManagementService;
import cn.yanshisan.blog.diary.interfaces.dto.DiaryCreateDTO;
import cn.yanshisan.blog.diary.interfaces.dto.DiaryUpdateDTO;
import cn.yanshisan.blog.diary.interfaces.dto.DiaryVO;
import cn.yanshisan.blog.shared.api.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryApplicationService {

    private final DiaryManagementService diaryManagementService;
    private final DiaryRepository diaryRepository;

    public DiaryVO createDiary(DiaryCreateDTO dto) {
        Diary diary = diaryManagementService.createDiary(dto.getContent(), dto.getImages(), dto.getDiaryDate());
        return DiaryVO.from(diary);
    }

    public DiaryVO updateDiary(String diaryCode, DiaryUpdateDTO dto) {
        diaryManagementService.updateDiary(diaryCode, dto.getContent(), dto.getImages(), dto.getDiaryDate());
        return DiaryVO.from(diaryRepository.findByCode(diaryCode).orElseThrow());
    }

    public void deleteDiary(String diaryCode) {
        diaryManagementService.deleteDiary(diaryCode);
    }

    public PageResult<DiaryVO> listDiaries(int page, int pageSize, Integer year) {
        List<Diary> diaries;
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
