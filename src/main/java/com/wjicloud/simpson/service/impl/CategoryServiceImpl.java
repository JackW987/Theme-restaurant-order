package com.wjicloud.simpson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjicloud.simpson.common.CustomException;
import com.wjicloud.simpson.domain.Category;
import com.wjicloud.simpson.domain.Dish;
import com.wjicloud.simpson.domain.Setmeal;
import com.wjicloud.simpson.mapper.CategoryMapper;
import com.wjicloud.simpson.service.CategoryService;
import com.wjicloud.simpson.service.DishService;
import com.wjicloud.simpson.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishQueryWrapper);
        // 查询当前分类是否关联菜品
        if(count1 > 0){
            // 报业务层错误，因为该分类关联菜品
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> SetmealQueryWrapper = new LambdaQueryWrapper<>();
        SetmealQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(SetmealQueryWrapper);
        // 查询当前分类是否关联套餐
        if(count2 > 0){
            // 报业务层错误，因为该分类关联了套餐
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        super.removeById(id);
    }
}
