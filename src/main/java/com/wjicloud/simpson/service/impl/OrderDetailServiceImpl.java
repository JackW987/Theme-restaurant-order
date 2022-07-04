package com.wjicloud.simpson.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjicloud.simpson.domain.OrderDetail;
import com.wjicloud.simpson.mapper.OrderDetailMapper;
import com.wjicloud.simpson.service.OrderDetailService;
import com.wjicloud.simpson.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
