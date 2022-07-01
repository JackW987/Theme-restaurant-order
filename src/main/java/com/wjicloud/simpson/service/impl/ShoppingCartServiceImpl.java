package com.wjicloud.simpson.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjicloud.simpson.domain.ShoppingCart;
import com.wjicloud.simpson.mapper.ShoppingCartMapper;
import com.wjicloud.simpson.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
