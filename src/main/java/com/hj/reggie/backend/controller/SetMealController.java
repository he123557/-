package com.hj.reggie.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.reggie.backend.bean.*;
import com.hj.reggie.backend.dto.DishDto;
import com.hj.reggie.backend.dto.SetMealDto;
import com.hj.reggie.backend.service.*;
import com.hj.reggie.commons.bean.R;
import com.hj.reggie.commons.constants.Constant;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @create 2023-03-11 14:28
 */
@Controller
public class SetMealController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private SetMealService setMealService;
    @Resource
    private SetMealDishService setMealDishService;

    /**
     * 实现添加套餐
     *
     * @param setMealDto
     * @return
     */
    @PostMapping("/setmeal")
    @ResponseBody
    public Object addMenuFood(@RequestBody SetMealDto setMealDto) {
        setMealService.saveSetMealDto(setMealDto);
        return R.success("套餐信息添加成功");
    }

    /**
     * 实现套餐管理分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/setmeal/page")
    @ResponseBody
    public Object SetMealPage(Integer page, Integer pageSize, String name) {
        Page<SetMeal> setMealPage = new Page<>(page, pageSize);
        Page<SetMealDto> setMealDtoPage = new Page<>();
        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.like(name != null, SetMeal::getName, name).orderByDesc(SetMeal::getUpdateTime);
        setMealService.page(setMealPage, setMealLambdaQueryWrapper);
        BeanUtils.copyProperties(setMealPage, setMealDtoPage, "records");
        List<SetMeal> setMealRecords = setMealPage.getRecords();
        List<SetMealDto> setMealDtoRecords = setMealRecords.stream().map(setMealRecord -> {
            SetMealDto setMealDto = new SetMealDto();
            BeanUtils.copyProperties(setMealRecord, setMealDto);
            Long categoryId = setMealRecord.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setMealDto.setCategoryName(category.getName());
            return setMealDto;
        }).collect(Collectors.toList());
        setMealDtoPage.setRecords(setMealDtoRecords);
        return R.success(setMealDtoPage);
    }

    /**
     * 将需要修改的套餐信息显示在修改页面
     *
     * @param id
     * @return
     */
    @GetMapping("/setmeal/{id}")
    @ResponseBody
    public Object showEditSetMeal(@PathVariable Long id) {
        SetMealDto mealDto = setMealService.getSetMealDtoById(id);
        return R.success(mealDto);
    }

    /**
     * 保存修改的套餐数据
     *
     * @param setMealDto
     * @return
     */
    @PutMapping("/setmeal")
    @ResponseBody
    public Object editSetMeal(@RequestBody SetMealDto setMealDto) {
        setMealService.editSetMealDto(setMealDto);
        return R.success("套餐信息修改成功");
    }

    /**
     * 批量删除套餐信息
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/setmeal")
    @ResponseBody
    public Object deleteSetMeal(@RequestParam("ids") List<Long> ids) {
        setMealService.deleteSetMealDtoByIds(ids);
        return R.success("套餐信息删除成功");
    }

    /**
     * 批量停售或起售
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("setmeal/status/{status}")
    @ResponseBody
    public Object stopOrStartSetMeal(@PathVariable Integer status, @RequestParam("ids") List<Long> ids) {
        List<SetMeal> setMealList = new ArrayList<>();
        ids.forEach(id -> {
            SetMeal setMeal = new SetMeal();
            setMeal.setId(id);
            setMeal.setStatus(status);
            setMealList.add(setMeal);
        });
        setMealService.updateBatchById(setMealList);
        return R.success("菜品停售/启售成功");
    }

    /**
     * 用户端点击商务套餐，儿童套餐 显示该套餐的商品
     *
     * @param setMeal
     * @return
     */
    @GetMapping("/setmeal/list")
    @ResponseBody
    public Object showSetMealList(SetMeal setMeal) {
        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(setMeal.getCategoryId() != null, SetMeal::getCategoryId, setMeal.getCategoryId())
                .eq(setMeal.getStatus() != null, SetMeal::getStatus, setMeal.getStatus())
                .orderByDesc(SetMeal::getUpdateTime);
        List<SetMeal> setMealList = setMealService.list(setMealLambdaQueryWrapper);
        return R.success(setMealList);
    }

    /**
     * 点击套餐显示套餐详情
     *
     * @param id
     * @return
     */
    @GetMapping("/setmeal/dish/{id}")
    @ResponseBody
    public Object showSetMealDish(@PathVariable Long id) {
        LambdaQueryWrapper<SetMealDish> setMealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealDishLambdaQueryWrapper.eq(SetMealDish::getSetmealId, id);
        List<SetMealDish> setMealDishList = setMealDishService.list(setMealDishLambdaQueryWrapper);
        return R.success(setMealDishList);
    }
}
