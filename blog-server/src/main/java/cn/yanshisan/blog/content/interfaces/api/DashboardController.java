package cn.yanshisan.blog.content.interfaces.api;

import cn.yanshisan.blog.content.application.ArticleApplicationService;
import cn.yanshisan.blog.content.interfaces.dto.ArticleVO;
import cn.yanshisan.blog.content.interfaces.dto.DashboardStatsVO;
import cn.yanshisan.blog.content.interfaces.dto.ViewTrendVO;
import cn.yanshisan.blog.interaction.application.InteractionApplicationService;
import cn.yanshisan.blog.interaction.interfaces.dto.InteractionVO;
import cn.yanshisan.blog.shared.api.PageResult;
import cn.yanshisan.blog.shared.api.R;
import cn.yanshisan.blog.site.application.SiteApplicationService;
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

    private final ArticleApplicationService articleApplicationService;
    private final InteractionApplicationService interactionApplicationService;
    private final SiteApplicationService siteApplicationService;

    @GetMapping("/statistics")
    public R<DashboardStatsVO> getStatistics() {
        PageResult<ArticleVO> firstPage = articleApplicationService.listAllArticles(1, 1, null, null, null);
        long totalArticles = firstPage.getPagination().getTotal();

        long totalViews = 0;
        int batchSize = 100;
        int currentPage = 1;
        List<ArticleVO> allArticles = new ArrayList<>();
        while (true) {
            PageResult<ArticleVO> batch = articleApplicationService.listAllArticles(currentPage, batchSize, null, null, null);
            allArticles.addAll(batch.getList());
            totalViews += batch.getList().stream()
                    .mapToLong(a -> a.getViewCount() != null ? a.getViewCount() : 0)
                    .sum();
            if (batch.getList().size() < batchSize) {
                break;
            }
            currentPage++;
        }

        PageResult<InteractionVO> comments = interactionApplicationService.listByTarget("site", "site", "comment", 1, 1);
        long totalComments = comments.getPagination().getTotal();

        long totalLinks = siteApplicationService.getFriendLinks().size();

        return R.ok(new DashboardStatsVO(totalArticles, totalViews, totalComments, totalLinks));
    }

    @GetMapping("/view-trends")
    public R<List<ViewTrendVO>> getViewTrends(@RequestParam(defaultValue = "7") int days) {
        List<ArticleVO> allArticles = new ArrayList<>();
        int batchSize = 100;
        int currentPage = 1;
        while (true) {
            PageResult<ArticleVO> batch = articleApplicationService.listAllArticles(currentPage, batchSize, null, null, null);
            allArticles.addAll(batch.getList());
            if (batch.getList().size() < batchSize) {
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
        PageResult<InteractionVO> result = interactionApplicationService.listByTarget("site", "site", "comment", 1, limit);
        return R.ok(result.getList());
    }
}
