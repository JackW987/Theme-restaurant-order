package com.wjicloud.simpson.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjicloud.simpson.domain.Setmeal;
import com.wjicloud.simpson.mapper.SetmealMapper;
import com.wjicloud.simpson.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
