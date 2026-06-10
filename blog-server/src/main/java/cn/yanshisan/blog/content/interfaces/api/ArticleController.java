package cn.yanshisan.blog.content.interfaces.api;

import cn.yanshisan.blog.content.application.ArticleApplicationService;
import cn.yanshisan.blog.content.domain.entity.ContentRevision;
import cn.yanshisan.blog.content.interfaces.dto.ArticleContentUpdateDTO;
import cn.yanshisan.blog.content.interfaces.dto.ArticleCreateDTO;
import cn.yanshisan.blog.content.interfaces.dto.ArticleUpdateDTO;
import cn.yanshisan.blog.content.interfaces.dto.ArticleVO;
import cn.yanshisan.blog.shared.api.PageResult;
import cn.yanshisan.blog.shared.api.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleApplicationService articleApplicationService;

    @PostMapping
    public R<ArticleVO> create(@RequestBody @Valid ArticleCreateDTO dto) {
        ArticleVO vo = articleApplicationService.createArticle(
                dto.getTitle(), dto.getSummary(), dto.getCoverUrl(),
                dto.getBody(), dto.getContentFormat()
        );
        return R.ok(vo);
    }

    @PutMapping("/{code}")
    public R<ArticleVO> updateMetadata(@PathVariable("code") String articleCode,
                                       @RequestBody ArticleUpdateDTO dto) {
        ArticleVO vo = articleApplicationService.updateMetadata(
                articleCode, dto.getTitle(), dto.getSummary(), dto.getCoverUrl()
        );
        return R.ok(vo);
    }

    @PutMapping("/{code}/content")
    public R<ArticleVO> updateContent(@PathVariable("code") String articleCode,
                                      @RequestBody @Valid ArticleContentUpdateDTO dto) {
        ArticleVO vo = articleApplicationService.updateContent(articleCode, dto.getBody());
        return R.ok(vo);
    }

    @PutMapping("/{code}/publish")
    public R<ArticleVO> publish(@PathVariable("code") String articleCode) {
        ArticleVO vo = articleApplicationService.publish(articleCode);
        return R.ok(vo);
    }

    @PutMapping("/{code}/pin")
    public R<ArticleVO> togglePin(@PathVariable("code") String articleCode) {
        ArticleVO vo = articleApplicationService.togglePin(articleCode);
        return R.ok(vo);
    }

    @DeleteMapping("/{code}")
    public R<Void> delete(@PathVariable("code") String articleCode) {
        articleApplicationService.deleteArticle(articleCode);
        return R.ok();
    }

    @GetMapping
    public R<PageResult<ArticleVO>> list(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "20") int pageSize) {
        PageResult<ArticleVO> result = articleApplicationService.listPublishedArticles(page, pageSize);
        return R.ok(result);
    }

    @GetMapping("/{code}")
    public R<ArticleVO> getDetail(@PathVariable("code") String articleCode) {
        ArticleVO vo = articleApplicationService.getArticleDetail(articleCode);
        return R.ok(vo);
    }

    @GetMapping("/{code}/revisions")
    public R<List<ContentRevision>> listRevisions(@PathVariable("code") String articleCode) {
        List<ContentRevision> revisions = articleApplicationService.listRevisions(articleCode);
        return R.ok(revisions);
    }

    @GetMapping("/{code}/revisions/{version}")
    public R<ContentRevision> getRevision(@PathVariable("code") String articleCode,
                                           @PathVariable("version") int version) {
        ContentRevision revision = articleApplicationService.getRevision(articleCode, version);
        return R.ok(revision);
    }
}
