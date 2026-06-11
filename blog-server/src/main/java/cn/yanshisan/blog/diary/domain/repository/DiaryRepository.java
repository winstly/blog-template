package cn.yanshisan.blog.diary.domain.repository;

import cn.yanshisan.blog.diary.domain.entity.Diary;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository {

    void save(Diary diary);

    void update(Diary diary);

    Optional<Diary> findByCode(String diaryCode);

    List<Diary> findByYear(Integer year, int page, int pageSize);

    long countByYear(Integer year);

    List<Diary> findAll(int page, int pageSize);

    long countAll();

    void delete(Diary diary);

    List<Integer> findDistinctYears();
}
