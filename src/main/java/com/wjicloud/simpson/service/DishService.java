package com.wjicloud.simpson.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjicloud.simpson.domain.Dish;
import com.wjicloud.simpson.domain.DishFlavor;
import com.wjicloud.simpson.dto.DishDto;

public interface DishService extends IService<Dish> {
    // 保存菜品和口味信息
    void saveWithFlavor(DishDto dishDto);
    // 根据id查询菜品和口味信息
    DishDto getByIdWithFlavor(Long id);
    // 根据信息更新数据
    void updateWithFlavors(DishDto dishDto);
}
