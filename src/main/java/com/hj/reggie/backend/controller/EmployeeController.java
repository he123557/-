package com.hj.reggie.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.reggie.backend.bean.Employee;
import com.hj.reggie.backend.service.EmployeeService;
import com.hj.reggie.commons.bean.R;
import com.hj.reggie.commons.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 员工管理
 *
 * @create 2023-03-07 11:50
 */
@Controller
@Slf4j
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    /**
     * 登录
     *
     * @param employee
     * @param session
     * @return
     */
    @PostMapping("/employee/login")
    @ResponseBody
    public Object login(@RequestBody Employee employee, HttpSession session) {
        //收集数据，将密码进行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据用户名查询数据
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<Employee>();
        employeeLambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(employeeLambdaQueryWrapper);
        //判断是否查询到结果
        if (emp == null) {
            return R.error("登陆失败！");
        }
        //查看密码是否一致
        if (!emp.getPassword().equals(password)) {
            return R.error("登陆失败！");
        }
        //查看员工状态是否禁用
        if (emp.getStatus() == Constant.EMPLOYEE_STATUS_FAIL) {
            return R.error("账号禁用，登陆失败！");
        }
        //将员工id存入session
        session.setAttribute("employeeId", emp.getId());
        return R.success(emp);
    }

    /**
     * 退出
     *
     * @param session
     * @return
     */
    @PostMapping("/employee/logout")
    @ResponseBody
    public Object logout(HttpSession session) {
        session.removeAttribute("employeeId");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping("/employee")
    @ResponseBody
    //保证前端传过来的json能正常封装需要加@RequestBody注解
    public Object addEmployee(@RequestBody Employee employee) {
        //设置初始密码123456,进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //status数据库默认初始为1，所以不用设置
        //设置创建时间,修改时间
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        //设置创建人,修改人
        //Long empId = (Long) session.getAttribute("employeeId");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
        //调用service层保存数据
        employeeService.save(employee);
        return R.success(employee);
    }

    /**
     * 员工信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/employee/page")
    @ResponseBody
    public Object employeePage(Integer page, Integer pageSize, String name) {
        //构造分页构造器
        Page<Employee> employeePage = new Page<>(page, pageSize);
        //查询语句
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name).orderByDesc(Employee::getUpdateTime);
        //调用service层分页查询
        employeeService.page(employeePage, employeeLambdaQueryWrapper);
        return R.success(employeePage);
    }

    /**
     * 根据id修改员工信息
     * 传输有精度丢失，添加拓展消息转换器，转成String传输防止精度丢失
     *
     * @param employee
     * @return
     */
    @PutMapping("/employee")
    @ResponseBody
    public Object disableEmployeeStatus(@RequestBody Employee employee) {
        //将状态设为0
        //employee.setUpdateUser((Long) session.getAttribute("employeeId"));
        //employee.setUpdateTime(LocalDateTime.now());
        //调用service层
        employeeService.updateById(employee);
        return R.success(employee);
    }

    /**
     * 编辑员工信息,通过id查询员工信息，显示在前端修改页面上
     *
     * @return
     */
    @GetMapping("/employee/{id}")
    @ResponseBody
    public Object editEmployee(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
