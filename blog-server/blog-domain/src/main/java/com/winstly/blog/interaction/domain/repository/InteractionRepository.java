package com.winstly.blog.interaction.domain.repository;

import com.winstly.blog.interaction.domain.entity.InteractionDO;
import com.winstly.blog.interaction.domain.enums.Action;

import java.util.List;
import java.util.Optional;

public interface InteractionRepository {

    void save(InteractionDO interaction);

    void update(InteractionDO interaction);

    Optional<InteractionDO> findById(Long id);

    List<InteractionDO> findRootsByTarget(String targetType, String targetCode, Action actionType, int page, int pageSize);

    List<InteractionDO> findRepliesByTarget(String targetType, String targetCode, Action actionType);

    long countRootsByTarget(String targetType, String targetCode, Action actionType);

    void logicalDeleteById(Long id);

    void updateDisplayStatus(Long id, Integer displayStatus);

    void batchUpdateDisplayStatus(List<Long> ids, Integer displayStatus);

    void batchLogicalDelete(List<Long> ids);

    long countByTargetAndStatus(String targetType, String targetCode, Action action, Integer displayStatus);

    List<InteractionDO> findRootsByTargetAndStatus(String targetType, String targetCode, Action action, Integer displayStatus, int page, int pageSize);
}
