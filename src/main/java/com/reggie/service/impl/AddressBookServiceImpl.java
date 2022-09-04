package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.entity.AddressBook;
import com.reggie.entity.Employee;
import com.reggie.mapper.AddressBookMapper;
import com.reggie.mapper.EmployeeMapper;
import com.reggie.service.AddressBookService;
import com.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
