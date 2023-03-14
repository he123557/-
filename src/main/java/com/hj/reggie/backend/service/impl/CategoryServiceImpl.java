package com.hj.reggie.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.reggie.backend.bean.Category;
import com.hj.reggie.backend.bean.Dish;
import com.hj.reggie.backend.bean.SetMeal;
import com.hj.reggie.backend.mapper.CategoryMapper;
import com.hj.reggie.backend.mapper.DishMapper;
import com.hj.reggie.backend.mapper.SetMealMapper;
import com.hj.reggie.backend.service.CategoryService;
import com.hj.reggie.backend.service.DishService;
import com.hj.reggie.backend.service.SetMealService;
import com.hj.reggie.commons.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @create 2023-03-10 9:24
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    private DishService dishService;
    @Resource
    private SetMealService setMealService;

    @Override
    public void remove(Long ids) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        long dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount>0){
            //已经关联了菜品，抛出业务异常
            throw new CustomException("当前分类项关联了菜品，不能删除");
        }
        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(SetMeal::getCategoryId,ids);
        long setMealCount = setMealService.count(setMealLambdaQueryWrapper);
        if (setMealCount>0){
            //已经关联了套餐，抛出业务异常
            throw new CustomException("当前分类项关联了套餐，不能删除");
        }
        //正常删除
        super.removeById(ids);
    }
}
