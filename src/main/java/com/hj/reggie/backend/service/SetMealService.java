package com.hj.reggie.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.reggie.backend.bean.SetMeal;
import com.hj.reggie.backend.dto.SetMealDto;

import java.util.List;

/**
 * @create 2023-03-10 9:24
 */
public interface SetMealService extends IService<SetMeal> {
    void saveSetMealDto(SetMealDto setMealDto);
    SetMealDto getSetMealDtoById(Long id);
    void editSetMealDto(SetMealDto setMealDto);
    void deleteSetMealDtoByIds(List<Long> ids);
}
