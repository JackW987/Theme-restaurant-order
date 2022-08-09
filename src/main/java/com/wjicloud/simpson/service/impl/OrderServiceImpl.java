package com.wjicloud.simpson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjicloud.simpson.common.BaseContext;
import com.wjicloud.simpson.common.CustomException;
import com.wjicloud.simpson.domain.*;
import com.wjicloud.simpson.mapper.OrderMapper;
import com.wjicloud.simpson.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Override
    @Transactional
    public void submit(Orders orders) {
        // 获得当前用户id
        Long userId = BaseContext.getCurrentId();
        // 查询当前用户购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        // 向订单表插入数据，一条数据
        if(shoppingCartList==null || shoppingCartList.size()==0){
            throw new CustomException("购物车为空，不能下单");
        }
        // 查询用户数据
        User user = userService.getById(userId);
        // 查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook==null){
            throw new CustomException("用户地址信息有误，不能下单");
        }
        // 向订单明细表插入数据,多条数据----------------------------------
        long orderId = IdWorker.getId();
        // 原子操作 保证多线程的情况下计算没有问题,保证线程安全
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = shoppingCartList.stream().map((item)-> {
            // 这里遍历的item是购物车里的菜品
            // 设置订单明细 每一个订单明细关联一个订单 每一个订单有多个明细
            OrderDetail orderDetail = new OrderDetail();
            // 关联订单
            orderDetail.setOrderId(orderId);
            // 添加菜品数量
            orderDetail.setNumber(item.getNumber());
            // 添加菜品口味
            orderDetail.setDishFlavor(item.getDishFlavor());
            // 添加菜品id
            orderDetail.setDishId(item.getDishId());
            // 添加套餐id
            orderDetail.setSetmealId(item.getSetmealId());
            // 添加菜品名称
            orderDetail.setName(item.getName());
            // 添加菜品图片
            orderDetail.setImage(item.getImage());
            // 添加金额
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        // 设置订单id
        orders.setId(orderId);
        // 设置订单下单时间
        orders.setOrderTime(LocalDateTime.now());
        // 设置支付时间，这里就不模拟支付的情况了
        orders.setCheckoutTime(LocalDateTime.now());
        // 订单当前状态
        orders.setStatus(2);
        // 订单总金额
        orders.setAmount(new BigDecimal(amount.get()));
        // 用户id
        orders.setUserId(userId);
        // 设置订单号
        orders.setNumber(String.valueOf(orderId));
        // 设置用户姓名
        orders.setUserName(user.getName());
        // 设置收货人
        orders.setConsignee(addressBook.getConsignee());
        // 设置收货人手机号
        orders.setPhone(addressBook.getPhone());
        // 设置地址详细信息
        orders.setAddress((addressBook.getProvinceName()==null ? "" : addressBook.getProvinceName())
                        +(addressBook.getCityName()==null ? "" : addressBook.getCityName())
                        +(addressBook.getDistrictName()==null ? "" : addressBook.getDistrictName())
                        +(addressBook.getDetail()==null ? "" : addressBook.getDetail()));
        // 向订单表插入数据
        this.save(orders);
        // 向订单明细表插入数据
        orderDetailService.saveBatch(orderDetails);
        // 清空购物车数据
        shoppingCartService.remove(queryWrapper);
    }
}
