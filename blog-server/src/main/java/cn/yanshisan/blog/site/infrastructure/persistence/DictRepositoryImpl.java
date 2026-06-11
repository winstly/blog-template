package cn.yanshisan.blog.site.infrastructure.persistence;

import cn.yanshisan.blog.site.domain.entity.SysDict;
import cn.yanshisan.blog.site.domain.repository.DictRepository;
import cn.yanshisan.blog.site.domain.vo.DictTriple;
import cn.yanshisan.blog.site.infrastructure.persistence.mapper.SysDictMapper;
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
    public void save(SysDict dict) {
        sysDictMapper.insert(dict);
    }

    @Override
    public void update(SysDict dict) {
        LambdaUpdateWrapper<SysDict> wrapper = new LambdaUpdateWrapper<SysDict>()
                .eq(SysDict::getBizCode, dict.getBizCode())
                .eq(SysDict::getSubCode, dict.getSubCode())
                .eq(SysDict::getItemCode, dict.getItemCode());
        sysDictMapper.update(dict, wrapper);
    }

    @Override
    public Optional<SysDict> findByTriple(DictTriple triple) {
        LambdaQueryWrapper<SysDict> wrapper = buildTripleWrapper(triple);
        return Optional.ofNullable(sysDictMapper.selectOne(wrapper));
    }

    @Override
    public List<SysDict> listByBizCode(String bizCode) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getBizCode, bizCode)
                .eq(SysDict::getDisplayStatus, 1)
                .orderByAsc(SysDict::getSortOrder);
        return sysDictMapper.selectList(wrapper);
    }

    @Override
    public List<SysDict> listByBizAndSub(String bizCode, String subCode) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getBizCode, bizCode)
                .eq(SysDict::getSubCode, subCode)
                .eq(SysDict::getDisplayStatus, 1)
                .orderByAsc(SysDict::getSortOrder);
        return sysDictMapper.selectList(wrapper);
    }

    @Override
    public void logicalDelete(DictTriple triple) {
        LambdaUpdateWrapper<SysDict> wrapper = new LambdaUpdateWrapper<SysDict>()
                .eq(SysDict::getBizCode, triple.getBizCode())
                .eq(SysDict::getSubCode, triple.getSubCode())
                .eq(SysDict::getItemCode, triple.getItemCode())
                .set(SysDict::getIsDeleted, 1);
        sysDictMapper.update(null, wrapper);
    }

    @Override
    public boolean existsByTriple(DictTriple triple) {
        LambdaQueryWrapper<SysDict> wrapper = buildTripleWrapper(triple);
        return sysDictMapper.selectCount(wrapper) > 0;
    }

    @Override
    public Optional<SysDict> findByBizCodeAndItemCode(String bizCode, String itemCode) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getBizCode, bizCode)
                .eq(SysDict::getItemCode, itemCode);
        return Optional.ofNullable(sysDictMapper.selectOne(wrapper));
    }

    private LambdaQueryWrapper<SysDict> buildTripleWrapper(DictTriple triple) {
        return new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getBizCode, triple.getBizCode())
                .eq(SysDict::getSubCode, triple.getSubCode())
                .eq(SysDict::getItemCode, triple.getItemCode());
    }
}
