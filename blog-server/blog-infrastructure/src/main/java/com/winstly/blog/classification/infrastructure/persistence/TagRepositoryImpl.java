package com.winstly.blog.classification.infrastructure.persistence;

import com.winstly.blog.classification.domain.entity.TagDO;
import com.winstly.blog.classification.domain.repository.TagRepository;
import com.winstly.blog.classification.infrastructure.persistence.mapper.TagMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagMapper tagMapper;

    @Override
    public void save(TagDO tag) {
        tagMapper.insert(tag);
    }

    @Override
    public void update(TagDO tag) {
        tagMapper.updateById(tag);
    }

    @Override
    public Optional<TagDO> findByCode(String tagCode) {
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<TagDO>()
                .eq(TagDO::getTagCode, tagCode);
        return Optional.ofNullable(tagMapper.selectOne(wrapper));
    }

    @Override
    public List<TagDO> findAll() {
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<TagDO>()
                .orderByAsc(TagDO::getSortOrder)
                .orderByAsc(TagDO::getId);
        return tagMapper.selectList(wrapper);
    }

    @Override
    public List<TagDO> findByTreePathPrefix(String treePathPrefix) {
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<TagDO>()
                .likeRight(TagDO::getTreePath, treePathPrefix);
        return tagMapper.selectList(wrapper);
    }

    @Override
    public void delete(TagDO tag) {
        tagMapper.deleteById(tag.getId());
    }

    @Override
    public boolean existsByTagCode(String tagCode) {
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<TagDO>()
                .eq(TagDO::getTagCode, tagCode);
        return tagMapper.selectCount(wrapper) > 0;
    }

    @Override
    public Optional<TagDO> findById(Long id) {
        return Optional.ofNullable(tagMapper.selectById(id));
    }
}
