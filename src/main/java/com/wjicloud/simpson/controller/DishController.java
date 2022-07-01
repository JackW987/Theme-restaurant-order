package com.wjicloud.simpson.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjicloud.simpson.common.R;
import com.wjicloud.simpson.domain.Category;
import com.wjicloud.simpson.domain.Dish;
import com.wjicloud.simpson.domain.DishFlavor;
import com.wjicloud.simpson.dto.DishDto;
import com.wjicloud.simpson.service.CategoryService;
import com.wjicloud.simpson.service.DishFlavorService;
import com.wjicloud.simpson.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavors(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 修改菜品回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> update(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        Category category = categoryService.getById(dishDto.getCategoryId());
        String name = category.getName();
        dishDto.setCategoryName(name);
        return R.success(dishDto);
    }

    /**
     * 分页查询菜品
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        List<DishDto> dishDtoList  = new ArrayList<>();
        // 构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        // 条件过滤器对象
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        queryWrapper.like(name!=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        // 执行查询
        dishService.page(pageInfo,queryWrapper);
        // 拷贝数据
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        // 单独处理records数据
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item)->{
            // 创建一个空的dishDto接收值
            DishDto dishDto = new DishDto();
            // 将已有属性拷贝到dishDto中
            BeanUtils.copyProperties(item,dishDto);
            // 获取分类id
            Long categoryId = item.getCategoryId();
            // 根据分类id查询分类名字
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                // 将分类名字赋值给新创建的dishDto
                dishDto.setCategoryName(categoryName);
            }
            // 返回dishDto
            return dishDto;
        }).collect(Collectors.toList());
        // 执行查询
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 根据种类查询菜品
     */
//    @GetMapping("/list")
//    public R<List<Dish>> getDish(Dish dish){
//        // 初始化条件构造器
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        // 添加查询条件
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        // 查询是起售状态的菜品
//        queryWrapper.eq(Dish::getStatus,1);
//        // 添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        // 执行查询
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }

    @GetMapping("/list")
    public R<List<DishDto>> getDish(Dish dish){
        // 初始化条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        // 查询是起售状态的菜品
        queryWrapper.eq(Dish::getStatus,1);
        // 添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        // 执行查询
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item)->{
            // 创建一个空的dishDto接收值
            DishDto dishDto = new DishDto();
            // 将已有属性拷贝到dishDto中
            BeanUtils.copyProperties(item,dishDto);
            // 获取分类id
            Long categoryId = item.getCategoryId();
            // 根据分类id查询分类名字
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                // 将分类名字赋值给新创建的dishDto
                dishDto.setCategoryName(categoryName);
            }
            // 当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
            flavorQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(flavorQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            // 返回dishDto
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
    @DeleteMapping
    public R<String> delete(Long ids){
        dishService.removeById(ids);
        return R.success("删除成功");
    }
}

