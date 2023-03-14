package com.hj.reggie.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.reggie.backend.bean.SetMeal;
import com.hj.reggie.backend.bean.SetMealDish;
import com.hj.reggie.backend.dto.SetMealDto;
import com.hj.reggie.backend.mapper.SetMealMapper;
import com.hj.reggie.backend.service.SetMealDishService;
import com.hj.reggie.backend.service.SetMealService;
import com.hj.reggie.commons.bean.R;
import com.hj.reggie.commons.constants.Constant;
import com.hj.reggie.commons.exception.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @create 2023-03-10 9:24
 */
@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, SetMeal> implements SetMealService {
    @Resource
    private SetMealDishService setMealDishService;

    @Override
    @Transactional
    public void saveSetMealDto(SetMealDto setMealDto) {
        this.save(setMealDto);
        Long setMealId = setMealDto.getId();
        List<SetMealDish> setMealDishes = setMealDto.getSetmealDishes();
        setMealDishes.forEach(setMealDish -> {
            setMealDish.setSetmealId(setMealId);
        });
        setMealDishService.saveBatch(setMealDishes);
    }

    @Override
    public SetMealDto getSetMealDtoById(Long id) {
        SetMealDto setMealDto = new SetMealDto();
        SetMeal setMeal = this.getById(id);
        BeanUtils.copyProperties(setMeal,setMealDto);
        LambdaQueryWrapper<SetMealDish> setMealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealDishLambdaQueryWrapper.eq(id!=null,SetMealDish::getSetmealId,id);
        List<SetMealDish> setMealDishes = setMealDishService.list(setMealDishLambdaQueryWrapper);
        setMealDto.setSetmealDishes(setMealDishes);
        return setMealDto;
    }

    @Override
    @Transactional
    public void editSetMealDto(SetMealDto setMealDto) {
        this.updateById(setMealDto);
        Long setMealId = setMealDto.getId();
        //删除之前的套餐菜品
        LambdaQueryWrapper<SetMealDish> setMealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealDishLambdaQueryWrapper.eq(SetMealDish::getSetmealId,setMealId);
        setMealDishService.remove(setMealDishLambdaQueryWrapper);
        //保存修改的套餐菜品
        List<SetMealDish> setmealDishes = setMealDto.getSetmealDishes();
        setmealDishes.forEach(setMealDish -> {
            setMealDish.setSetmealId(setMealId);
        });
        setMealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void deleteSetMealDtoByIds(List<Long> ids) {
        //判断套餐是否在售卖中，正在售卖则不能删除，必须停售才能删除
        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.in(SetMeal::getId,ids).eq(SetMeal::getStatus, Constant.SET_MEAL_STATUS_START);
        long count = this.count(setMealLambdaQueryWrapper);
        if (count>0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        this.removeBatchByIds(ids);
        LambdaQueryWrapper<SetMealDish> setMealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealDishLambdaQueryWrapper.in(SetMealDish::getSetmealId,ids);
        setMealDishService.remove(setMealDishLambdaQueryWrapper);
    }
}
