package com.hj.reggie.front.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.reggie.front.bean.ShoppingCart;
import com.hj.reggie.front.mapper.ShoppingCartMapper;
import com.hj.reggie.front.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @create 2023-03-12 18:26
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
