package com.winstly.blog.classification.interfaces.api;

import com.winstly.blog.classification.application.TagApplicationService;
import com.winstly.blog.classification.interfaces.vo.TagVO;
import com.winstly.blog.shared.api.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final TagApplicationService tagApplicationService;

    @GetMapping
    public R<List<TagVO>> list() {
        List<TagVO> vos = tagApplicationService.listAllCategories();
        return R.ok(vos);
    }

    @GetMapping("/{tagCode}/articles")
    public R<List<String>> listArticles(@PathVariable("tagCode") String tagCode) {
        List<String> articleCodes = tagApplicationService.listArticlesByCategory(tagCode);
        return R.ok(articleCodes);
    }
}
