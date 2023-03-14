package com.hj.reggie.backend.dto;


import com.hj.reggie.backend.bean.Dish;
import com.hj.reggie.backend.bean.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
