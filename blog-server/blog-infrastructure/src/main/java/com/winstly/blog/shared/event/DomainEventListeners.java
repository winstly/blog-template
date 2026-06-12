package com.winstly.blog.shared.event;

import com.winstly.blog.classification.domain.repository.RelationRepository;
import com.winstly.blog.content.domain.event.ArticleDeletedEvent;
import com.winstly.blog.classification.domain.event.TagDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventListeners {

    private final RelationRepository relationRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onArticleDeleted(ArticleDeletedEvent event) {
        log.info("Article deleted: {}, cascading relation cleanup", event.getArticleCode());
        relationRepository.logicalDeleteByArticleCode(event.getArticleCode());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTagDeleted(TagDeletedEvent event) {
        log.info("Tag deleted: {}, cascading relation cleanup", event.getTagCode());
        relationRepository.logicalDeleteByTagCode(event.getTagCode());
    }
}
