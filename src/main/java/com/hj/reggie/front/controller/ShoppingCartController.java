package com.hj.reggie.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hj.reggie.backend.bean.Category;
import com.hj.reggie.backend.service.CategoryService;
import com.hj.reggie.commons.bean.R;
import com.hj.reggie.commons.utils.BaseContextUtil;
import com.hj.reggie.front.bean.ShoppingCart;
import com.hj.reggie.front.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @create 2023-03-12 18:20
 */
@Controller
public class ShoppingCartController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 显示分类列表
     *
     * @return
     */
    @RequestMapping("/category/list")
    @ResponseBody
    public Object categoryList() {
        List<Category> categoryList = categoryService.list();
        return R.success(categoryList);
    }

    /**
     * 显示购物车列表
     *
     * @return
     */
    @GetMapping("/shoppingCart/list")
    @ResponseBody
    public Object shoppingCart() {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContextUtil.getCurrentId())
                .orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return R.success(shoppingCartList);
    }

    /**
     * 往购物车添加商品
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/shoppingCart/add")
    @ResponseBody
    public Object addShoppingCart(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(userId != null, ShoppingCart::getUserId, userId)
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        //查询dishId或setMealId，如果能查到，数量加一
        if (shoppingCart1 != null) {
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCartService.updateById(shoppingCart1);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCart1 = shoppingCart;
        }
        return R.success(shoppingCart1);
    }

    /**
     * 将购物车指定商品移除
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/shoppingCart/sub")
    @ResponseBody
    public Object subShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        Long userId = BaseContextUtil.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(userId != null, ShoppingCart::getUserId, userId)
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        if (shoppingCart1.getNumber() > 1) {
            shoppingCart1.setNumber(shoppingCart1.getNumber() - 1);
            shoppingCartService.updateById(shoppingCart1);
            return R.success(shoppingCart1);
        } else {
            shoppingCartService.removeById(shoppingCart1);
            return R.success("商品更新成功");
        }
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @DeleteMapping("/shoppingCart/clean")
    @ResponseBody
    public Object cleanShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContextUtil.getCurrentId());
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        return R.success("购物车清空成功");
    }
}
