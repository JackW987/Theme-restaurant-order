package com.wjicloud.simpson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjicloud.simpson.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
