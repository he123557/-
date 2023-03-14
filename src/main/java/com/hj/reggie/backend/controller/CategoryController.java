package com.hj.reggie.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.reggie.backend.bean.Category;
import com.hj.reggie.backend.service.CategoryService;
import com.hj.reggie.commons.bean.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类管理
 *
 * @create 2023-03-10 9:26
 */
@Controller
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    /**
     * 新增菜品分类,新增套餐分类
     *
     * @param category
     * @return
     */
    @PostMapping("/category")
    @ResponseBody
    public Object addFoodClassification(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分类管理分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/category/page")
    @ResponseBody
    public Object sort(Integer page, Integer pageSize) {
        Page<Category> categoryPage = new Page<>(page, pageSize);
        //根据sort进行排序
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(categoryPage,categoryLambdaQueryWrapper);
        return R.success(categoryPage);
    }

    /**
     * 删除分类,判断要删除的菜品是否关联菜品类和套餐类
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/category")
    @ResponseBody
    public Object deleteFood(Long ids) {
            categoryService.remove(ids);
            return R.success("分类信息删除成功");
    }

    /**
     * 修改分类
     *
     * @param category
     * @return
     */
    @PutMapping("/category")
    @ResponseBody
    public Object putFood(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }

    /**
     * 新建菜品,回显商品分类下拉框数据
     *
     * @param category
     * @return
     */
    @GetMapping("category/list")
    @ResponseBody
    public Object addFood(Category category) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType())
                .orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(categoryList);
    }
}
