package com.winstly.blog.site.infrastructure.persistence;

import com.winstly.blog.site.domain.entity.SysDictDO;
import com.winstly.blog.site.domain.repository.DictRepository;
import com.winstly.blog.site.domain.valueobject.DictTriple;
import com.winstly.blog.interaction.domain.enums.DisplayStatus;
import com.winstly.blog.site.infrastructure.persistence.mapper.SysDictMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DictRepositoryImpl implements DictRepository {

    private final SysDictMapper sysDictMapper;

    @Override
    public void save(SysDictDO dict) {
        sysDictMapper.insert(dict);
    }

    @Override
    public void update(SysDictDO dict) {
        LambdaUpdateWrapper<SysDictDO> wrapper = new LambdaUpdateWrapper<SysDictDO>()
                .eq(SysDictDO::getBizCode, dict.getBizCode())
                .eq(SysDictDO::getSubCode, dict.getSubCode())
                .eq(SysDictDO::getItemCode, dict.getItemCode());
        sysDictMapper.update(dict, wrapper);
    }

    @Override
    public Optional<SysDictDO> findByTriple(DictTriple triple) {
        LambdaQueryWrapper<SysDictDO> wrapper = buildTripleWrapper(triple);
        return Optional.ofNullable(sysDictMapper.selectOne(wrapper));
    }

    @Override
    public List<SysDictDO> listByBizCode(String bizCode) {
        LambdaQueryWrapper<SysDictDO> wrapper = new LambdaQueryWrapper<SysDictDO>()
                .eq(SysDictDO::getBizCode, bizCode)
                .eq(SysDictDO::getDisplayStatus, DisplayStatus.APPROVED.getValue())
                .orderByAsc(SysDictDO::getSortOrder);
        return sysDictMapper.selectList(wrapper);
    }

    @Override
    public List<SysDictDO> listByBizAndSub(String bizCode, String subCode) {
        LambdaQueryWrapper<SysDictDO> wrapper = new LambdaQueryWrapper<SysDictDO>()
                .eq(SysDictDO::getBizCode, bizCode)
                .eq(SysDictDO::getSubCode, subCode)
                .eq(SysDictDO::getDisplayStatus, DisplayStatus.APPROVED.getValue())
                .orderByAsc(SysDictDO::getSortOrder);
        return sysDictMapper.selectList(wrapper);
    }

    @Override
    public void logicalDelete(DictTriple triple) {
        LambdaUpdateWrapper<SysDictDO> wrapper = new LambdaUpdateWrapper<SysDictDO>()
                .eq(SysDictDO::getBizCode, triple.getBizCode())
                .eq(SysDictDO::getSubCode, triple.getSubCode())
                .eq(SysDictDO::getItemCode, triple.getItemCode())
                .set(SysDictDO::getIsDeleted, 1);
        sysDictMapper.update(null, wrapper);
    }

    @Override
    public boolean existsByTriple(DictTriple triple) {
        LambdaQueryWrapper<SysDictDO> wrapper = buildTripleWrapper(triple);
        return sysDictMapper.selectCount(wrapper) > 0;
    }

    @Override
    public Optional<SysDictDO> findByBizCodeAndItemCode(String bizCode, String itemCode) {
        LambdaQueryWrapper<SysDictDO> wrapper = new LambdaQueryWrapper<SysDictDO>()
                .eq(SysDictDO::getBizCode, bizCode)
                .eq(SysDictDO::getItemCode, itemCode);
        return Optional.ofNullable(sysDictMapper.selectOne(wrapper));
    }

    private LambdaQueryWrapper<SysDictDO> buildTripleWrapper(DictTriple triple) {
        return new LambdaQueryWrapper<SysDictDO>()
                .eq(SysDictDO::getBizCode, triple.getBizCode())
                .eq(SysDictDO::getSubCode, triple.getSubCode())
                .eq(SysDictDO::getItemCode, triple.getItemCode());
    }
}
