package cn.yanshisan.blog.site.domain.repository;

import cn.yanshisan.blog.site.domain.entity.SysDict;
import cn.yanshisan.blog.site.domain.vo.DictTriple;

import java.util.List;
import java.util.Optional;

public interface DictRepository {

    void save(SysDict dict);

    void update(SysDict dict);

    Optional<SysDict> findByTriple(DictTriple triple);

    List<SysDict> listByBizCode(String bizCode);

    List<SysDict> listByBizAndSub(String bizCode, String subCode);

    void logicalDelete(DictTriple triple);

    boolean existsByTriple(DictTriple triple);
}
