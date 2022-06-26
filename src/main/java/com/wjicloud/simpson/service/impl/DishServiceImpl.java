package com.wjicloud.simpson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjicloud.simpson.domain.Dish;
import com.wjicloud.simpson.domain.DishFlavor;
import com.wjicloud.simpson.dto.DishDto;
import com.wjicloud.simpson.mapper.DishMapper;
import com.wjicloud.simpson.service.DishFlavorService;
import com.wjicloud.simpson.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 构造基本类
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        // 拷贝dish属性到dishDto
        BeanUtils.copyProperties(dish,dishDto);
        // 查询dishId对应的Flavors
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        // 将对应的Flavors赋值到dishDto上
        dishDto.setFlavors(list);
        return dishDto;
    }
    @Transactional
    @Override
    public void updateWithFlavors(DishDto dishDto) {
        // 更新dish表信息
        this.updateById(dishDto);
        Long dishId = dishDto.getId();
        // 清理flavors数据
        // 构造条件
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        // 插入新的数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }
}
