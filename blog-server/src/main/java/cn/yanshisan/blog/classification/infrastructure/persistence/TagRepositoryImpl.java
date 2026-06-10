package cn.yanshisan.blog.classification.infrastructure.persistence;

import cn.yanshisan.blog.classification.domain.entity.Tag;
import cn.yanshisan.blog.classification.domain.repository.TagRepository;
import cn.yanshisan.blog.classification.infrastructure.persistence.mapper.TagMapper;
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
    public void save(Tag tag) {
        tagMapper.insert(tag);
    }

    @Override
    public void update(Tag tag) {
        tagMapper.updateById(tag);
    }

    @Override
    public Optional<Tag> findByCode(String tagCode) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<Tag>()
                .eq(Tag::getTagCode, tagCode);
        return Optional.ofNullable(tagMapper.selectOne(wrapper));
    }

    @Override
    public List<Tag> findAll() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<Tag>()
                .orderByAsc(Tag::getSortOrder)
                .orderByAsc(Tag::getId);
        return tagMapper.selectList(wrapper);
    }

    @Override
    public List<Tag> findByTreePathPrefix(String treePathPrefix) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<Tag>()
                .likeRight(Tag::getTreePath, treePathPrefix);
        return tagMapper.selectList(wrapper);
    }

    @Override
    public void delete(Tag tag) {
        tagMapper.deleteById(tag.getId());
    }

    @Override
    public boolean existsByTagCode(String tagCode) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<Tag>()
                .eq(Tag::getTagCode, tagCode);
        return tagMapper.selectCount(wrapper) > 0;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return Optional.ofNullable(tagMapper.selectById(id));
    }
}
