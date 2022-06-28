package com.wjicloud.simpson.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjicloud.simpson.domain.Setmeal;
import com.wjicloud.simpson.domain.SetmealDish;
import com.wjicloud.simpson.mapper.SetmealDishMapper;
import com.wjicloud.simpson.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>implements SetmealDishService {
}
