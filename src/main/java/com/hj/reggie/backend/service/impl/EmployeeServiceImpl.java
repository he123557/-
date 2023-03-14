package com.hj.reggie.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.reggie.backend.mapper.EmployeeMapper;
import com.hj.reggie.backend.bean.Employee;
import com.hj.reggie.backend.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @create 2023-03-07 11:32
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
