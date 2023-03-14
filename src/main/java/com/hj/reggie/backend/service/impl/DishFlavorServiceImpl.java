package com.hj.reggie.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.reggie.backend.bean.Dish;
import com.hj.reggie.backend.bean.DishFlavor;
import com.hj.reggie.backend.mapper.DishFlavorMapper;
import com.hj.reggie.backend.mapper.DishMapper;
import com.hj.reggie.backend.service.DishFlavorService;
import com.hj.reggie.backend.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @create 2023-03-10 9:24
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
