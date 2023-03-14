package com.hj.reggie.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.reggie.backend.bean.Category;

/**
 * @create 2023-03-10 9:24
 */
public interface CategoryService extends IService<Category> {
    void remove(Long ids);
}
