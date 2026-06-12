package com.winstly.blog.content.interfaces.vo;

import com.winstly.blog.content.domain.entity.ContentRevisionDO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RevisionVO {

    private Long id;
    private Integer version;
    private String changeSummary;
    private String contentFormat;
    private Integer wordCount;
    private LocalDateTime gmtCreate;

    public static RevisionVO from(ContentRevisionDO revision) {
        RevisionVO vo = new RevisionVO();
        vo.setId(revision.getId());
        vo.setVersion(revision.getVersion());
        vo.setChangeSummary(revision.getChangeSummary());
        vo.setContentFormat(revision.getContentFormat());
        vo.setWordCount(revision.getWordCount());
        vo.setGmtCreate(revision.getGmtCreate());
        return vo;
    }
}
