package cn.yanshisan.blog.interaction.infrastructure.persistence;

import cn.yanshisan.blog.interaction.domain.entity.Interaction;
import cn.yanshisan.blog.interaction.domain.repository.InteractionRepository;
import cn.yanshisan.blog.interaction.domain.vo.Action;
import cn.yanshisan.blog.interaction.infrastructure.persistence.mapper.InteractionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InteractionRepositoryImpl implements InteractionRepository {

    private final InteractionMapper interactionMapper;

    @Override
    public void save(Interaction interaction) {
        interactionMapper.insert(interaction);
    }

    @Override
    public void update(Interaction interaction) {
        interactionMapper.updateById(interaction);
    }

    @Override
    public Optional<Interaction> findById(Long id) {
        return Optional.ofNullable(interactionMapper.selectById(id));
    }

    @Override
    public List<Interaction> findRootsByTarget(String targetType, String targetCode, Action actionType, int page, int pageSize) {
        LambdaQueryWrapper<Interaction> wrapper = new LambdaQueryWrapper<Interaction>()
                .eq(Interaction::getTargetType, targetType)
                .eq(Interaction::getTargetCode, targetCode)
                .eq(Interaction::getActionType, actionType)
                .eq(Interaction::getTreeDepth, 0)
                .orderByDesc(Interaction::getGmtCreate)
                .last("LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize);
        return interactionMapper.selectList(wrapper);
    }

    @Override
    public List<Interaction> findRepliesByTarget(String targetType, String targetCode, Action actionType) {
        LambdaQueryWrapper<Interaction> wrapper = new LambdaQueryWrapper<Interaction>()
                .eq(Interaction::getTargetType, targetType)
                .eq(Interaction::getTargetCode, targetCode)
                .eq(Interaction::getActionType, actionType)
                .gt(Interaction::getTreeDepth, 0)
                .orderByAsc(Interaction::getGmtCreate);
        return interactionMapper.selectList(wrapper);
    }

    @Override
    public long countRootsByTarget(String targetType, String targetCode, Action actionType) {
        LambdaQueryWrapper<Interaction> wrapper = new LambdaQueryWrapper<Interaction>()
                .eq(Interaction::getTargetType, targetType)
                .eq(Interaction::getTargetCode, targetCode)
                .eq(Interaction::getActionType, actionType)
                .eq(Interaction::getTreeDepth, 0);
        return interactionMapper.selectCount(wrapper);
    }

    @Override
    public void logicalDeleteById(Long id) {
        LambdaUpdateWrapper<Interaction> wrapper = new LambdaUpdateWrapper<Interaction>()
                .eq(Interaction::getId, id)
                .set(Interaction::getIsDeleted, 1);
        interactionMapper.update(null, wrapper);
    }

    @Override
    public void updateDisplayStatus(Long id, Integer displayStatus) {
        LambdaUpdateWrapper<Interaction> wrapper = new LambdaUpdateWrapper<Interaction>()
                .eq(Interaction::getId, id)
                .set(Interaction::getDisplayStatus, displayStatus);
        interactionMapper.update(null, wrapper);
    }

    @Override
    public void batchUpdateDisplayStatus(List<Long> ids, Integer displayStatus) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        LambdaUpdateWrapper<Interaction> wrapper = new LambdaUpdateWrapper<Interaction>()
                .in(Interaction::getId, ids)
                .set(Interaction::getDisplayStatus, displayStatus);
        interactionMapper.update(null, wrapper);
    }

    @Override
    public void batchLogicalDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        LambdaUpdateWrapper<Interaction> wrapper = new LambdaUpdateWrapper<Interaction>()
                .in(Interaction::getId, ids)
                .set(Interaction::getIsDeleted, 1);
        interactionMapper.update(null, wrapper);
    }

    @Override
    public long countByTargetAndStatus(String targetType, String targetCode, Action action, Integer displayStatus) {
        LambdaQueryWrapper<Interaction> wrapper = new LambdaQueryWrapper<Interaction>()
                .eq(Interaction::getTargetType, targetType)
                .eq(Interaction::getTargetCode, targetCode)
                .eq(Interaction::getActionType, action)
                .eq(Interaction::getTreeDepth, 0);
        if (displayStatus != null) {
            wrapper.eq(Interaction::getDisplayStatus, displayStatus);
        }
        return interactionMapper.selectCount(wrapper);
    }

    @Override
    public List<Interaction> findRootsByTargetAndStatus(String targetType, String targetCode, Action action, Integer displayStatus, int page, int pageSize) {
        LambdaQueryWrapper<Interaction> wrapper = new LambdaQueryWrapper<Interaction>()
                .eq(Interaction::getTargetType, targetType)
                .eq(Interaction::getTargetCode, targetCode)
                .eq(Interaction::getActionType, action)
                .eq(Interaction::getTreeDepth, 0);
        if (displayStatus != null) {
            wrapper.eq(Interaction::getDisplayStatus, displayStatus);
        }
        wrapper.orderByDesc(Interaction::getGmtCreate)
                .last("LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize);
        return interactionMapper.selectList(wrapper);
    }
}
