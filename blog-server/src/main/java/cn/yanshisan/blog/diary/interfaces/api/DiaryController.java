package cn.yanshisan.blog.diary.interfaces.api;

import cn.yanshisan.blog.diary.application.DiaryApplicationService;
import cn.yanshisan.blog.diary.interfaces.dto.DiaryCreateDTO;
import cn.yanshisan.blog.diary.interfaces.dto.DiaryUpdateDTO;
import cn.yanshisan.blog.diary.interfaces.dto.DiaryVO;
import cn.yanshisan.blog.shared.api.PageResult;
import cn.yanshisan.blog.shared.api.R;
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
    public R<DiaryVO> create(@RequestBody @Valid DiaryCreateDTO dto) {
        return R.ok(diaryApplicationService.createDiary(dto));
    }

    @PutMapping("/{code}")
    public R<DiaryVO> update(@PathVariable("code") String diaryCode,
                             @RequestBody DiaryUpdateDTO dto) {
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
