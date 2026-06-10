package cn.yanshisan.blog.interaction.domain.repository;

import cn.yanshisan.blog.interaction.domain.entity.Interaction;
import cn.yanshisan.blog.interaction.domain.vo.Action;

import java.util.List;
import java.util.Optional;

public interface InteractionRepository {

    void save(Interaction interaction);

    void update(Interaction interaction);

    Optional<Interaction> findById(Long id);

    List<Interaction> findRootsByTarget(String targetType, String targetCode, Action actionType, int page, int pageSize);

    List<Interaction> findRepliesByTarget(String targetType, String targetCode, Action actionType);

    long countRootsByTarget(String targetType, String targetCode, Action actionType);
}
