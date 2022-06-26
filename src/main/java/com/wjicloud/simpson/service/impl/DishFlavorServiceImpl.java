package com.wjicloud.simpson.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjicloud.simpson.domain.DishFlavor;
import com.wjicloud.simpson.mapper.DishFlavorMapper;
import com.wjicloud.simpson.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
