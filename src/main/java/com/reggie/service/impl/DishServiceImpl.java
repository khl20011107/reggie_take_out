package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.CustomException;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;
import com.reggie.entity.DishFlavor;
import com.reggie.mapper.DishMapper;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品同时保存口味数据，同时操作两张表
     *
     * @param dishDto
     */

    @Override
    public void saveWithFlavor(DishDto dishDto) {
//        保存菜品的基本数据到菜品表
        this.save(dishDto);
//        获取菜品的id
        Long dishId = dishDto.getId();
//        菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
//        保存菜品口味数据到口味表
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id删除数据
     * @param id
     */

    @Override
    public void removeWithFlavor(List<Long> id) {

//        构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        构造条件
//        in方法相当于id in (?)也就是where的查询条件
        queryWrapper.in(Dish::getId, id);
//        eq方法相当于SELECT id,name,password,age,tel FROM user WHERE (status = 1)
        queryWrapper.eq(Dish::getStatus, 1);

        int count = this.count(queryWrapper);
//        判断是否在售,在售则不能删除
        if (count > 0) {
            throw new CustomException("该菜品在售不能删除");
        }
//        不是在售状态所以可以删除菜品表中的数据
        this.removeByIds(id);
//        删除关系表中的数据
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        构造条件
        lambdaQueryWrapper.in(DishFlavor::getDishId, id);
        dishFlavorService.remove(lambdaQueryWrapper);
    }

    /**
     * 根据id查询回显数据
     * @param id
     * @return
     */

    @Override
    public DishDto selectById(Long id) {
//        从菜品表中查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
//        拷贝对象
        BeanUtils.copyProperties(dish,dishDto);

//        从口味表中查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 修改数据
     * @param dishDto
     */

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

//        删除口味表中的数据然后重新上传数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);


        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }



}
