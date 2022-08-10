package com.wjicloud.simpson.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjicloud.simpson.common.R;
import com.wjicloud.simpson.domain.Category;
import com.wjicloud.simpson.domain.Setmeal;
import com.wjicloud.simpson.domain.SetmealDto;
import com.wjicloud.simpson.service.CategoryService;
import com.wjicloud.simpson.service.SetmealDishService;
import com.wjicloud.simpson.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RequestMapping("/setmeal")
@RestController
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page,int pageSize,String name){
        // 获取套餐分页数据
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        // 需要分装一个Dto分页数据
        Page<SetmealDto> pageDto = new Page<>();
        // 查询构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 对套餐名字进行模糊查询
        queryWrapper.like(name!=null,Setmeal::getName,name);
        // 根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        // 执行查询
        setmealService.page(pageInfo,queryWrapper);
        // 将查询到的分页数据除了records赋值给Dto分页数据
        BeanUtils.copyProperties(pageInfo,pageDto,"records");
        // 获取套餐分页的records进行封装
        List<Setmeal> records = pageInfo.getRecords();
        // 利用steam流和map映射进行封装
        List<SetmealDto> dtoRecords = pageInfo.getRecords().stream().map((item)->{
            // 构建空的Dto对象
            SetmealDto setmealDto = new SetmealDto();
            // 将套餐属性拷贝给Dto对象
            BeanUtils.copyProperties(item,setmealDto);
            // 根据分类id查询分类名称
            Category category = categoryService.getById(item.getCategoryId());
            // 如果分类名称非空，将名称赋值给Dto对象
            if(category!=null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            // 返回封装好的Dto对象
            return setmealDto;
        }).collect(Collectors.toList());
        // 将records数据放入page中
        pageDto.setRecords(dtoRecords);
        return R.success(pageDto);
    }
    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list (Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }
}
