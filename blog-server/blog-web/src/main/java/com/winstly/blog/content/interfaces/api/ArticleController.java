package com.winstly.blog.content.interfaces.api;

import com.winstly.blog.classification.application.TagApplicationService;
import com.winstly.blog.classification.domain.enums.RelationType;
import com.winstly.blog.content.application.ArticleApplicationService;
import com.winstly.blog.content.interfaces.request.ArticleContentUpdateRequest;
import com.winstly.blog.content.interfaces.request.ArticleCreateRequest;
import com.winstly.blog.content.interfaces.request.ArticleTagAssignmentRequest;
import com.winstly.blog.content.interfaces.request.ArticleUpdateRequest;
import com.winstly.blog.content.interfaces.vo.ArticleVO;
import com.winstly.blog.content.interfaces.vo.RevisionVO;
import com.winstly.blog.shared.api.PageResult;
import com.winstly.blog.shared.api.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleApplicationService articleApplicationService;
    private final TagApplicationService tagApplicationService;

    @PostMapping
    public R<ArticleVO> create(@RequestBody @Valid ArticleCreateRequest dto) {
        ArticleVO vo = articleApplicationService.createArticle(
                dto.getTitle(), dto.getSummary(), dto.getCoverUrl(),
                dto.getBody(), dto.getContentFormat()
        );
        return R.ok(vo);
    }

    @PutMapping("/{code}")
    public R<ArticleVO> updateMetadata(@PathVariable("code") String articleCode,
                                       @RequestBody ArticleUpdateRequest dto) {
        ArticleVO vo = articleApplicationService.updateMetadata(
                articleCode, dto.getTitle(), dto.getSummary(), dto.getCoverUrl()
        );
        return R.ok(vo);
    }

    @PutMapping("/{code}/content")
    public R<ArticleVO> updateContent(@PathVariable("code") String articleCode,
                                      @RequestBody @Valid ArticleContentUpdateRequest dto) {
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

    @GetMapping("/admin")
    public R<PageResult<ArticleVO>> listAdmin(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {
        PageResult<ArticleVO> result = articleApplicationService.listAllArticles(page, pageSize, keyword, status, category);
        return R.ok(result);
    }

    @DeleteMapping("/batch")
    public R<Void> batchDelete(@RequestBody List<String> articleCodes) {
        articleApplicationService.deleteArticles(articleCodes);
        return R.ok();
    }

    @PutMapping("/{code}/unpublish")
    public R<ArticleVO> unpublish(@PathVariable("code") String articleCode) {
        ArticleVO vo = articleApplicationService.unpublish(articleCode);
        return R.ok(vo);
    }

    @PutMapping("/{code}/tags")
    public R<Void> assignTags(@PathVariable("code") String articleCode,
                              @RequestBody ArticleTagAssignmentRequest dto) {
        tagApplicationService.assignTagsToArticle(articleCode, dto.getTagCodes(), RelationType.TAG);
        tagApplicationService.assignTagsToArticle(articleCode, dto.getCategoryCodes(), RelationType.CATEGORY);
        return R.ok();
    }

    @GetMapping("/{code}/revisions")
    public R<List<RevisionVO>> listRevisions(@PathVariable("code") String articleCode) {
        List<RevisionVO> revisions = articleApplicationService.listRevisions(articleCode);
        return R.ok(revisions);
    }

    @GetMapping("/{code}/revisions/{version}")
    public R<RevisionVO> getRevision(@PathVariable("code") String articleCode,
                                           @PathVariable("version") int version) {
        RevisionVO revision = articleApplicationService.getRevision(articleCode, version);
        return R.ok(revision);
    }
}
