package com.winstly.blog.content.interfaces.api;

import com.winstly.blog.content.application.ArticleApplicationService;
import com.winstly.blog.content.interfaces.vo.ArticleVO;
import com.winstly.blog.content.interfaces.vo.DashboardStatsVO;
import com.winstly.blog.content.interfaces.vo.ViewTrendVO;
import com.winstly.blog.interaction.application.InteractionApplicationService;
import com.winstly.blog.interaction.domain.enums.Action;
import com.winstly.blog.interaction.interfaces.vo.InteractionVO;
import com.winstly.blog.shared.constants.TargetType;
import com.winstly.blog.shared.api.PageResult;
import com.winstly.blog.shared.api.R;
import com.winstly.blog.site.application.SiteApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private static final int BATCH_SIZE = 100;

    private final ArticleApplicationService articleApplicationService;
    private final InteractionApplicationService interactionApplicationService;
    private final SiteApplicationService siteApplicationService;

    @GetMapping("/statistics")
    public R<DashboardStatsVO> getStatistics() {
        PageResult<ArticleVO> firstPage = articleApplicationService.listAllArticles(1, 1, null, null, null);
        long totalArticles = firstPage.getPagination().getTotal();

        long totalViews = 0;
        int BATCH_SIZE = 100;
        int currentPage = 1;
        List<ArticleVO> allArticles = new ArrayList<>();
        while (true) {
            PageResult<ArticleVO> batch = articleApplicationService.listAllArticles(currentPage, BATCH_SIZE, null, null, null);
            allArticles.addAll(batch.getList());
            totalViews += batch.getList().stream()
                    .mapToLong(a -> a.getViewCount() != null ? a.getViewCount() : 0)
                    .sum();
            if (batch.getList().size() < BATCH_SIZE) {
                break;
            }
            currentPage++;
        }

        PageResult<InteractionVO> comments = interactionApplicationService.listByTarget(TargetType.SITE, TargetType.SITE, Action.COMMENT.getValue(), 1, 1);
        long totalComments = comments.getPagination().getTotal();

        long totalLinks = siteApplicationService.getFriendLinks().size();

        return R.ok(new DashboardStatsVO(totalArticles, totalViews, totalComments, totalLinks));
    }

    @GetMapping("/view-trends")
    public R<List<ViewTrendVO>> getViewTrends(@RequestParam(defaultValue = "7") int days) {
        List<ArticleVO> allArticles = new ArrayList<>();
        int BATCH_SIZE = 100;
        int currentPage = 1;
        while (true) {
            PageResult<ArticleVO> batch = articleApplicationService.listAllArticles(currentPage, BATCH_SIZE, null, null, null);
            allArticles.addAll(batch.getList());
            if (batch.getList().size() < BATCH_SIZE) {
                break;
            }
            currentPage++;
        }

        LocalDate today = LocalDate.now();
        Map<LocalDate, Long> viewsByDate = allArticles.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getGmtCreate() != null ? a.getGmtCreate().toLocalDate() : today,
                        Collectors.summingLong(a -> a.getViewCount() != null ? a.getViewCount() : 0L)
                ));

        List<ViewTrendVO> trends = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            long views = viewsByDate.getOrDefault(date, 0L);
            trends.add(new ViewTrendVO(date.toString(), views));
        }
        return R.ok(trends);
    }

    @GetMapping("/recent-articles")
    public R<List<ArticleVO>> getRecentArticles(@RequestParam(defaultValue = "5") int limit) {
        PageResult<ArticleVO> result = articleApplicationService.listAllArticles(1, limit, null, null, null);
        return R.ok(result.getList());
    }

    @GetMapping("/recent-comments")
    public R<List<InteractionVO>> getRecentComments(@RequestParam(defaultValue = "5") int limit) {
        PageResult<InteractionVO> result = interactionApplicationService.listByTarget(TargetType.SITE, TargetType.SITE, Action.COMMENT.getValue(), 1, limit);
        return R.ok(result.getList());
    }
}
