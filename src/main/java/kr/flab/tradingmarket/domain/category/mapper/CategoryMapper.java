package kr.flab.tradingmarket.domain.category.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.flab.tradingmarket.domain.category.dto.response.SimpleCategoryDto;

@Mapper
@Repository
public interface CategoryMapper {

    List<SimpleCategoryDto> findFirstCategories();

    List<SimpleCategoryDto> findSecondCategories();

    List<SimpleCategoryDto> findThirdCategories();

    int existsCategory(Long categoryNo);
}
