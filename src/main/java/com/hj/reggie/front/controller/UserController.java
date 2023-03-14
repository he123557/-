package com.hj.reggie.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.reggie.commons.bean.R;
import com.hj.reggie.commons.utils.ValidateCodeUtils;
import com.hj.reggie.front.bean.User;
import com.hj.reggie.front.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @create 2023-03-12 15:17
 */
@Controller
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 发送手机验证码短信，需要收费，改为了直接控制台输出验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/user/sendMsg")
    @ResponseBody
    public Object sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //使用阿里云短信服务发送短信
            log.info("短信验证码为：" + code);
            //SMSUtils.sendMessage("","",phone,code);
            //将生成的短信验证码保存到session中
            session.setAttribute(phone, code);
            return R.success("手机验证码发送成功");
        }
        return R.error("手机验证码发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @return
     */
    @PostMapping("/user/login")
    @ResponseBody
    public Object login(@RequestBody Map<String, Object> map, HttpSession session) {
        log.info(map.toString());
        //获取手机号
        String phone = (String) map.get("phone");
        //获取验证码
        String code = (String) map.get("code");
        //从session中取出验证码
        String codeSession = (String) session.getAttribute(phone);
        //比对验证码是否正确
        if (codeSession != null && codeSession.equals(code)) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            if (user == null) {
                //判断手机号是否为新用户，是新用户就偷偷的将手机号保存到数据库
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("userId", user.getId());
            return R.success(user);
        } else {
            return R.error("登录失败");
        }
    }
}
