package com.hj.reggie.backend.dto;

import com.hj.reggie.backend.bean.SetMeal;
import com.hj.reggie.backend.bean.SetMealDish;
import lombok.Data;


import java.util.List;

/**
 * @create 2023-03-11 14:43
 */
@Data
public class SetMealDto extends SetMeal {
    private List<SetMealDish> setmealDishes;

    private String categoryName;
}
