package cn.yanshisan.blog.diary.infrastructure.persistence;

import cn.yanshisan.blog.diary.domain.entity.Diary;
import cn.yanshisan.blog.diary.domain.repository.DiaryRepository;
import cn.yanshisan.blog.diary.infrastructure.persistence.mapper.DiaryMapper;
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
    public void save(Diary diary) {
        diaryMapper.insert(diary);
    }

    @Override
    public void update(Diary diary) {
        diaryMapper.updateById(diary);
    }

    @Override
    public Optional<Diary> findByCode(String diaryCode) {
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Diary::getDiaryCode, diaryCode);
        return Optional.ofNullable(diaryMapper.selectOne(wrapper));
    }

    @Override
    public List<Diary> findByYear(Integer year, int page, int pageSize) {
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Diary::getYear, year)
               .orderByDesc(Diary::getDiaryDate);
        Page<Diary> result = diaryMapper.selectPage(new Page<>(page, pageSize), wrapper);
        return result.getRecords();
    }

    @Override
    public long countByYear(Integer year) {
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Diary::getYear, year);
        return diaryMapper.selectCount(wrapper);
    }

    @Override
    public List<Diary> findAll(int page, int pageSize) {
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Diary::getDiaryDate);
        Page<Diary> result = diaryMapper.selectPage(new Page<>(page, pageSize), wrapper);
        return result.getRecords();
    }

    @Override
    public long countAll() {
        return diaryMapper.selectCount(new LambdaQueryWrapper<>());
    }

    @Override
    public void delete(Diary diary) {
        diaryMapper.deleteById(diary.getId());
    }

    @Override
    public List<Integer> findDistinctYears() {
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Diary::getYear)
               .orderByDesc(Diary::getYear)
               .groupBy(Diary::getYear);
        return diaryMapper.selectList(wrapper).stream()
                .map(Diary::getYear)
                .distinct()
                .collect(Collectors.toList());
    }
}
