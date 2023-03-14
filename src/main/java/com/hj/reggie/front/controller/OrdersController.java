package com.hj.reggie.front.controller;

import com.hj.reggie.commons.bean.R;
import com.hj.reggie.front.bean.Orders;
import com.hj.reggie.front.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @create 2023-03-13 17:54
 */
@Controller
public class OrdersController {
    @Resource
    private OrdersService ordersService;

    /**
     * 点击去支付,完成支付下单成功
     *
     * @return
     */
    @PostMapping("/order/submit")
    @ResponseBody
    public Object pay(@RequestBody Orders orders) {
        ordersService.saveOrdersAndOrderDetail(orders);
        return R.success("支付成功");
    }
}
