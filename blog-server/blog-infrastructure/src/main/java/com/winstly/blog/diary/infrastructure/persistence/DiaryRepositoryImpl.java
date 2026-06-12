package com.winstly.blog.diary.infrastructure.persistence;

import com.winstly.blog.diary.domain.entity.DiaryDO;
import com.winstly.blog.diary.domain.repository.DiaryRepository;
import com.winstly.blog.diary.infrastructure.persistence.mapper.DiaryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryImpl implements DiaryRepository {

    private final DiaryMapper diaryMapper;

    @Override
    public void save(DiaryDO diary) {
        diaryMapper.insert(diary);
    }

    @Override
    public void update(DiaryDO diary) {
        diaryMapper.updateById(diary);
    }

    @Override
    public Optional<DiaryDO> findByCode(String diaryCode) {
        LambdaQueryWrapper<DiaryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DiaryDO::getDiaryCode, diaryCode);
        return Optional.ofNullable(diaryMapper.selectOne(wrapper));
    }

    @Override
    public List<DiaryDO> findByYear(Integer year, int page, int pageSize) {
        LambdaQueryWrapper<DiaryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DiaryDO::getYear, year)
               .orderByDesc(DiaryDO::getDiaryDate);
        Page<DiaryDO> result = diaryMapper.selectPage(new Page<>(page, pageSize), wrapper);
        return result.getRecords();
    }

    @Override
    public long countByYear(Integer year) {
        LambdaQueryWrapper<DiaryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DiaryDO::getYear, year);
        return diaryMapper.selectCount(wrapper);
    }

    @Override
    public List<DiaryDO> findAll(int page, int pageSize) {
        LambdaQueryWrapper<DiaryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(DiaryDO::getDiaryDate);
        Page<DiaryDO> result = diaryMapper.selectPage(new Page<>(page, pageSize), wrapper);
        return result.getRecords();
    }

    @Override
    public long countAll() {
        return diaryMapper.selectCount(new LambdaQueryWrapper<>());
    }

    @Override
    public void delete(DiaryDO diary) {
        diaryMapper.deleteById(diary.getId());
    }

    @Override
    public List<Integer> findDistinctYears() {
        LambdaQueryWrapper<DiaryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DiaryDO::getYear)
               .orderByDesc(DiaryDO::getYear)
               .groupBy(DiaryDO::getYear);
        return diaryMapper.selectList(wrapper).stream()
                .map(DiaryDO::getYear)
                .distinct()
                .collect(Collectors.toList());
    }
}
