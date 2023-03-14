package com.hj.reggie.front.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.reggie.front.bean.OrderDetail;
import com.hj.reggie.front.mapper.OrderDetailMapper;
import com.hj.reggie.front.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @create 2023-03-13 17:53
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
