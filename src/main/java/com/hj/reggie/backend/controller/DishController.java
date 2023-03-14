package com.hj.reggie.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.reggie.backend.bean.Category;
import com.hj.reggie.backend.bean.Dish;
import com.hj.reggie.backend.bean.DishFlavor;
import com.hj.reggie.backend.dto.DishDto;
import com.hj.reggie.backend.service.CategoryService;
import com.hj.reggie.backend.service.DishFlavorService;
import com.hj.reggie.backend.service.DishService;
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
 * 菜品管理
 *
 * @create 2023-03-10 11:36
 */
@Controller
public class DishController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private DishService dishService;
    @Resource
    private DishFlavorService dishFlavorService;

    /**
     * 添加套餐菜品中，实现点击添加菜单显示菜品
     *
     * @param dish
     * @return
     */
    /*@GetMapping("/dish/list")
    @ResponseBody
    public Object showAddMenuFood(Dish dish) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, dish.getCategoryId())
                .eq(Dish::getStatus, Constant.DISH_STATUS_START)
                .orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishService.list(dishLambdaQueryWrapper);
        return R.success(dishes);
    }*/
    @GetMapping("/dish/list")
    @ResponseBody
    public Object showAddMenuFood(Dish dish) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, dish.getCategoryId())
                .eq(Dish::getStatus, Constant.DISH_STATUS_START)
                .orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishService.list(dishLambdaQueryWrapper);
        List<DishDto> dishDtoList=dishes.stream().map(item->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category!=null){
                dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            if (dishFlavors!=null){
                dishDto.setFlavors(dishFlavors);
            }
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }

    /**
     * 新建菜品，保存，在数据库中插入数据
     *
     * @param dishDto
     * @return
     */
    @PostMapping("/dish")
    @ResponseBody
    public Object saveFood(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品添加成功");
    }

    /**
     * 菜品管理分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/dish/page")
    @ResponseBody
    public Object DishPage(Integer page, Integer pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        //这只有菜品id，需要拿到菜品name，要使用dishDto
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(name != null, Dish::getName, name).orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,dishLambdaQueryWrapper);
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        //拿到dishPage的records集合
        List<Dish> dishRecords = dishPage.getRecords();
        //遍历集合，调用cageService查到name赋值给cageName
        List<DishDto> dishDtoRecords=dishRecords.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            //调用service层，根据id查询category
            Category category = categoryService.getById(item.getCategoryId());
            //给dishDto的category的categoryName赋值
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoRecords);
        return R.success(dishDtoPage);
    }

    /**
     * 修改菜品，根据id查询信息显示在修改页面
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ResponseBody
    public Object showEditDishDto(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getDishDtoById(id);
        return R.success(dishDto);
    }

    /**
     * 保存修改的菜品信息
     * @param dishDto
     * @return
     */
    @PutMapping("/dish")
    @ResponseBody
    public Object editDishDto(@RequestBody DishDto dishDto){
        dishService.updateDishDto(dishDto);
        return R.success("菜品信息修改成功");
    }

    /**
     * 批量删除菜品信息
     * @param ids
     * @return
     */
    @DeleteMapping("/dish")
    @ResponseBody
    public Object deleteFoodByIds(@RequestParam("ids") List<Long> ids){
        dishService.deleteWithFlavor(ids);
        return R.success("批量删除成功");
    }

    /**
     * 批量停售(起售)商品信息
     * @return
     */
    @PostMapping("/dish/status/{status}")
    @ResponseBody
    public Object stopOrStartSellingFood(@RequestParam("ids") List<Long> ids,@PathVariable Integer status){
        List<Dish> dishes=new ArrayList<>();
        ids.forEach(id->{
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(status);
            dishes.add(dish);
        });
        dishService.updateBatchById(dishes);
        return R.success("菜品停售/启售成功");
    }
}
