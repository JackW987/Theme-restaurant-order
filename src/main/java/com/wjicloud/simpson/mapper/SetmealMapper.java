package com.wjicloud.simpson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjicloud.simpson.domain.Setmeal;
import com.wjicloud.simpson.domain.SetmealDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    /**
     * 新增套餐，同时保存和菜品的关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);
}
