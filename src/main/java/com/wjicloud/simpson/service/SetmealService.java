package com.wjicloud.simpson.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjicloud.simpson.domain.Setmeal;
import com.wjicloud.simpson.domain.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 和菜品信息一起新增套餐
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐和其对应菜品的关联数据
     * @param list
     */
    void removeWithDish(List<Long> list);
}
