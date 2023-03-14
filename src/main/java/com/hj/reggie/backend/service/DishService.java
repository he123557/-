package com.hj.reggie.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.reggie.backend.bean.Category;
import com.hj.reggie.backend.bean.Dish;
import com.hj.reggie.backend.dto.DishDto;

import java.util.List;

/**
 * @create 2023-03-10 9:24
 */
public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
    DishDto getDishDtoById(Long dishId);
    void updateDishDto(DishDto dishDto);
    void deleteWithFlavor(List<Long> ids);
}
