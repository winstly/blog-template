package com.winstly.blog.interaction.infrastructure.persistence;

import com.winstly.blog.interaction.domain.entity.InteractionDO;
import com.winstly.blog.interaction.domain.repository.InteractionRepository;
import com.winstly.blog.interaction.domain.enums.Action;
import com.winstly.blog.interaction.domain.enums.DisplayStatus;
import com.winstly.blog.interaction.domain.enums.TreeConstants;
import com.winstly.blog.interaction.infrastructure.persistence.mapper.InteractionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InteractionRepositoryImpl implements InteractionRepository {

    private final InteractionMapper interactionMapper;

    @Override
    public void save(InteractionDO interaction) {
        interactionMapper.insert(interaction);
    }

    @Override
    public void update(InteractionDO interaction) {
        interactionMapper.updateById(interaction);
    }

    @Override
    public Optional<InteractionDO> findById(Long id) {
        return Optional.ofNullable(interactionMapper.selectById(id));
    }

    @Override
    public List<InteractionDO> findRootsByTarget(String targetType, String targetCode, Action actionType, int page, int pageSize) {
        LambdaQueryWrapper<InteractionDO> wrapper = new LambdaQueryWrapper<InteractionDO>()
                .eq(InteractionDO::getTargetType, targetType)
                .eq(InteractionDO::getTargetCode, targetCode)
                .eq(InteractionDO::getActionType, actionType)
                .eq(InteractionDO::getTreeDepth, TreeConstants.ROOT_DEPTH)
                .orderByDesc(InteractionDO::getGmtCreate)
                .last("LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize);
        return interactionMapper.selectList(wrapper);
    }

    @Override
    public List<InteractionDO> findRepliesByTarget(String targetType, String targetCode, Action actionType) {
        LambdaQueryWrapper<InteractionDO> wrapper = new LambdaQueryWrapper<InteractionDO>()
                .eq(InteractionDO::getTargetType, targetType)
                .eq(InteractionDO::getTargetCode, targetCode)
                .eq(InteractionDO::getActionType, actionType)
                .gt(InteractionDO::getTreeDepth, 0)
                .orderByAsc(InteractionDO::getGmtCreate);
        return interactionMapper.selectList(wrapper);
    }

    @Override
    public long countRootsByTarget(String targetType, String targetCode, Action actionType) {
        LambdaQueryWrapper<InteractionDO> wrapper = new LambdaQueryWrapper<InteractionDO>()
                .eq(InteractionDO::getTargetType, targetType)
                .eq(InteractionDO::getTargetCode, targetCode)
                .eq(InteractionDO::getActionType, actionType)
                .eq(InteractionDO::getTreeDepth, TreeConstants.ROOT_DEPTH);
        return interactionMapper.selectCount(wrapper);
    }

    @Override
    public void logicalDeleteById(Long id) {
        LambdaUpdateWrapper<InteractionDO> wrapper = new LambdaUpdateWrapper<InteractionDO>()
                .eq(InteractionDO::getId, id)
                .set(InteractionDO::getIsDeleted, 1);
        interactionMapper.update(null, wrapper);
    }

    @Override
    public void updateDisplayStatus(Long id, Integer displayStatus) {
        LambdaUpdateWrapper<InteractionDO> wrapper = new LambdaUpdateWrapper<InteractionDO>()
                .eq(InteractionDO::getId, id)
                .set(InteractionDO::getDisplayStatus, displayStatus);
        interactionMapper.update(null, wrapper);
    }

    @Override
    public void batchUpdateDisplayStatus(List<Long> ids, Integer displayStatus) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        LambdaUpdateWrapper<InteractionDO> wrapper = new LambdaUpdateWrapper<InteractionDO>()
                .in(InteractionDO::getId, ids)
                .set(InteractionDO::getDisplayStatus, displayStatus);
        interactionMapper.update(null, wrapper);
    }

    @Override
    public void batchLogicalDelete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        LambdaUpdateWrapper<InteractionDO> wrapper = new LambdaUpdateWrapper<InteractionDO>()
                .in(InteractionDO::getId, ids)
                .set(InteractionDO::getIsDeleted, 1);
        interactionMapper.update(null, wrapper);
    }

    @Override
    public long countByTargetAndStatus(String targetType, String targetCode, Action action, Integer displayStatus) {
        LambdaQueryWrapper<InteractionDO> wrapper = new LambdaQueryWrapper<InteractionDO>()
                .eq(InteractionDO::getTargetType, targetType)
                .eq(InteractionDO::getTargetCode, targetCode)
                .eq(InteractionDO::getActionType, action)
                .eq(InteractionDO::getTreeDepth, TreeConstants.ROOT_DEPTH);
        if (displayStatus != null) {
            wrapper.eq(InteractionDO::getDisplayStatus, displayStatus);
        }
        return interactionMapper.selectCount(wrapper);
    }

    @Override
    public List<InteractionDO> findRootsByTargetAndStatus(String targetType, String targetCode, Action action, Integer displayStatus, int page, int pageSize) {
        LambdaQueryWrapper<InteractionDO> wrapper = new LambdaQueryWrapper<InteractionDO>()
                .eq(InteractionDO::getTargetType, targetType)
                .eq(InteractionDO::getTargetCode, targetCode)
                .eq(InteractionDO::getActionType, action)
                .eq(InteractionDO::getTreeDepth, TreeConstants.ROOT_DEPTH);
        if (displayStatus != null) {
            wrapper.eq(InteractionDO::getDisplayStatus, displayStatus);
        }
        wrapper.orderByDesc(InteractionDO::getGmtCreate)
                .last("LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize);
        return interactionMapper.selectList(wrapper);
    }
}
