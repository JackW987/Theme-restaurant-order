package com.wjicloud.simpson.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjicloud.simpson.domain.Orders;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
