package com.reggie.dto;


import com.reggie.entity.Dish;
import com.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;


/**
 * 这个类是数据传输对象，一般用于展示层与服务层的数据传输
 */


@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
