package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.BaseContext;
import com.reggie.common.R;
import com.reggie.entity.Orders;
import com.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page,int pageSize) {

        Page<Orders> pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(pageInfo ,queryWrapper);
        return R.success(pageInfo);
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize) {
        Page<Orders> pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(pageInfo ,queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> paisong(@RequestBody Orders orders) {
//        log.info("order:{}",orders);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        log.info("id:{}",BaseContext.getCurrentId());
        Integer status = orders.getStatus();
        if(status == 2) {
            orders.setStatus(4);
            ordersService.updateById(orders);
        }
        return R.success("派送成功");
    }
}
