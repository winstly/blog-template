package cn.yanshisan.blog.classification.interfaces.api;

import cn.yanshisan.blog.classification.application.TagApplicationService;
import cn.yanshisan.blog.classification.interfaces.dto.TagCreateDTO;
import cn.yanshisan.blog.classification.interfaces.dto.TagMoveDTO;
import cn.yanshisan.blog.classification.interfaces.dto.TagUpdateDTO;
import cn.yanshisan.blog.classification.interfaces.dto.TagVO;
import cn.yanshisan.blog.shared.api.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagApplicationService tagApplicationService;

    @PostMapping
    public R<TagVO> create(@RequestBody @Valid TagCreateDTO dto) {
        TagVO vo = tagApplicationService.createTag(
                dto.getTagCode(), dto.getTagName(), dto.getDescription(), dto.getParentTagCode()
        );
        return R.ok(vo);
    }

    @PutMapping("/{code}")
    public R<TagVO> updateMetadata(@PathVariable("code") String tagCode,
                                   @RequestBody TagUpdateDTO dto) {
        TagVO vo = tagApplicationService.updateMetadata(
                tagCode, dto.getTagName(), dto.getDescription(), dto.getDisplayStatus()
        );
        return R.ok(vo);
    }

    @PutMapping("/{code}/move")
    public R<TagVO> move(@PathVariable("code") String tagCode,
                         @RequestBody TagMoveDTO dto) {
        TagVO vo = tagApplicationService.moveTag(tagCode, dto.getNewParentTagCode());
        return R.ok(vo);
    }

    @DeleteMapping("/{code}")
    public R<Void> delete(@PathVariable("code") String tagCode) {
        tagApplicationService.deleteTag(tagCode);
        return R.ok();
    }

    @GetMapping
    public R<List<TagVO>> list() {
        List<TagVO> vos = tagApplicationService.listAllTags();
        return R.ok(vos);
    }

    @GetMapping("/{code}")
    public R<TagVO> getDetail(@PathVariable("code") String tagCode) {
        TagVO vo = tagApplicationService.getTagDetail(tagCode);
        return R.ok(vo);
    }
}
