package com.reggie.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.entity.Employee;
import org.springframework.transaction.annotation.Transactional;


public interface EmployeeService extends IService<Employee> {
}
