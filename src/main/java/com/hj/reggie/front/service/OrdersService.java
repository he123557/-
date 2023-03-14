package com.hj.reggie.front.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.reggie.front.bean.Orders;

/**
 * @create 2023-03-13 17:51
 */
public interface OrdersService extends IService<Orders> {
    void saveOrdersAndOrderDetail(Orders orders);
}
