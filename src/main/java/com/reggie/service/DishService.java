package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;

import java.util.List;


public interface DishService extends IService<Dish> {

//    新增菜品需要同时操作两张表
    public void saveWithFlavor(DishDto dishDto);

//    删除菜品时需要将菜品的关系表中关联的数据一起删除
    public void removeWithFlavor(List<Long> id);

    public DishDto selectById(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
