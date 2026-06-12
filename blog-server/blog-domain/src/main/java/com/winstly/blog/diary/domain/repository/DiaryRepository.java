package com.winstly.blog.diary.domain.repository;

import com.winstly.blog.diary.domain.entity.DiaryDO;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository {

    void save(DiaryDO diary);

    void update(DiaryDO diary);

    Optional<DiaryDO> findByCode(String diaryCode);

    List<DiaryDO> findByYear(Integer year, int page, int pageSize);

    long countByYear(Integer year);

    List<DiaryDO> findAll(int page, int pageSize);

    long countAll();

    void delete(DiaryDO diary);

    List<Integer> findDistinctYears();
}
