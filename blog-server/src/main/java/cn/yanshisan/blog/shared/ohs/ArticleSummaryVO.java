package cn.yanshisan.blog.shared.ohs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSummaryVO {

    private String articleCode;
    private String title;
    private String summary;
    private String coverUrl;
    private LocalDateTime publishedAt;
}
