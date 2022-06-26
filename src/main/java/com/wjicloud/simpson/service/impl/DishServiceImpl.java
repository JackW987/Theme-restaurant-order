package com.wjicloud.simpson.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjicloud.simpson.domain.Dish;
import com.wjicloud.simpson.mapper.DishMapper;
import com.wjicloud.simpson.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
