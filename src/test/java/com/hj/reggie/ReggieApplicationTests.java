package com.hj.reggie;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.reggie.backend.bean.Employee;
import com.hj.reggie.backend.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest
class ReggieApplicationTests {
    @Resource
    private EmployeeService employeeService;

    @Test
    void contextLoads() {
        System.out.println(LocalDateTime.now());
    }

}
