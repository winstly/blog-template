package com.winstly.blog.site.domain.repository;

import com.winstly.blog.site.domain.entity.SysDictDO;
import com.winstly.blog.site.domain.valueobject.DictTriple;

import java.util.List;
import java.util.Optional;

public interface DictRepository {

    void save(SysDictDO dict);

    void update(SysDictDO dict);

    Optional<SysDictDO> findByTriple(DictTriple triple);

    List<SysDictDO> listByBizCode(String bizCode);

    List<SysDictDO> listByBizAndSub(String bizCode, String subCode);

    void logicalDelete(DictTriple triple);

    boolean existsByTriple(DictTriple triple);

    Optional<SysDictDO> findByBizCodeAndItemCode(String bizCode, String itemCode);
}
