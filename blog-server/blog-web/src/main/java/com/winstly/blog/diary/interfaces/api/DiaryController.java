package com.winstly.blog.diary.interfaces.api;

import com.winstly.blog.diary.application.DiaryApplicationService;
import com.winstly.blog.diary.interfaces.request.DiaryCreateRequest;
import com.winstly.blog.diary.interfaces.request.DiaryUpdateRequest;
import com.winstly.blog.diary.interfaces.vo.DiaryVO;
import com.winstly.blog.shared.api.PageResult;
import com.winstly.blog.shared.api.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryApplicationService diaryApplicationService;

    @PostMapping
    public R<DiaryVO> create(@RequestBody @Valid DiaryCreateRequest dto) {
        return R.ok(diaryApplicationService.createDiary(dto));
    }

    @PutMapping("/{code}")
    public R<DiaryVO> update(@PathVariable("code") String diaryCode,
                             @RequestBody DiaryUpdateRequest dto) {
        return R.ok(diaryApplicationService.updateDiary(diaryCode, dto));
    }

    @DeleteMapping("/{code}")
    public R<Void> delete(@PathVariable("code") String diaryCode) {
        diaryApplicationService.deleteDiary(diaryCode);
        return R.ok();
    }

    @GetMapping
    public R<PageResult<DiaryVO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) Integer year) {
        return R.ok(diaryApplicationService.listDiaries(page, pageSize, year));
    }

    @GetMapping("/years")
    public R<List<Integer>> listYears() {
        return R.ok(diaryApplicationService.listYears());
    }
}
