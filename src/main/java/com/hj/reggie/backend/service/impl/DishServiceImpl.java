package com.hj.reggie.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.reggie.backend.bean.Category;
import com.hj.reggie.backend.bean.Dish;
import com.hj.reggie.backend.bean.DishFlavor;
import com.hj.reggie.backend.dto.DishDto;
import com.hj.reggie.backend.mapper.CategoryMapper;
import com.hj.reggie.backend.mapper.DishMapper;
import com.hj.reggie.backend.service.CategoryService;
import com.hj.reggie.backend.service.DishFlavorService;
import com.hj.reggie.backend.service.DishService;
import com.hj.reggie.commons.constants.Constant;
import com.hj.reggie.commons.exception.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @create 2023-03-10 9:24
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Resource
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存口味数据
     *
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品信息到dish
        this.save(dishDto);
        //给dishFlavor赋上categoryId
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
//        flavors.forEach(flavor->{
//            flavor.setDishId(dishId);
//        });
        //保存口味数据到dishFlavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品信息
     *
     * @param dishId
     * @return
     */
    @Override
    public DishDto getDishDtoById(Long dishId) {
        DishDto dishDto = new DishDto();
        Dish dish = this.getById(dishId);
        //把dish的参数复制到dishDto中
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(dishId != null, DishFlavor::getDishId, dishId);
        List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    /**
     * 修改菜品信息
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateDishDto(DishDto dishDto) {
        this.updateById(dishDto);
        Long dishId = dishDto.getId();
        //先清理之前的口味信息，比如说之前的口味你不想要了换了新的口味，那么如果不删除的话，之前的口味也会在数据库中
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        //把修改的信息重新添加进去，避免了数据的冗余
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(flavor->{
            flavor.setDishId(dishId);
        });
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 批量删除菜品信息
     * @param ids
     */
    @Override
    @Transactional
    public void deleteWithFlavor(List<Long> ids) {
        //判断菜品是否在售卖中，正在售卖则不能删除，必须停售才能删除
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Dish::getId,ids).eq(Dish::getStatus, Constant.DISH_STATUS_START);
        long count = this.count(dishLambdaQueryWrapper);
        if (count>0){
            throw new CustomException("菜品正在售卖中，不能删除");
        }

        this.removeBatchByIds(ids);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
    }
}
