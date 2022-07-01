package com.wjicloud.simpson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjicloud.simpson.common.CustomException;
import com.wjicloud.simpson.domain.Setmeal;
import com.wjicloud.simpson.domain.SetmealDish;
import com.wjicloud.simpson.domain.SetmealDto;
import com.wjicloud.simpson.mapper.SetmealMapper;
import com.wjicloud.simpson.service.SetmealDishService;
import com.wjicloud.simpson.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    /**
     * 和菜品信息一起新增套餐
     * @param setmealDto
     */
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 调用service保存setmeal信息
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        // 保存套餐和菜品的关联关系
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getStatus,1);
        queryWrapper.in(Setmeal::getId,ids);
        int count = this.count(queryWrapper);
        if(count > 0){
            // 如果不能删除，抛出业务异常
            throw new CustomException("套餐正在售卖中，无法删除");
        }
        // 删除套餐中的数据--setmeal
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(SetmealDish::getSetmealId, ids);
        // 删除关系表的数据--setmeal_dish
        setmealDishService.remove(queryWrapper2);
    }
}
