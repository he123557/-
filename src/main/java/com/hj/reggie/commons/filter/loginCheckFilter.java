package com.hj.reggie.commons.filter;

import com.alibaba.fastjson.JSON;
import com.hj.reggie.backend.bean.Employee;
import com.hj.reggie.commons.bean.R;
import com.hj.reggie.commons.utils.BaseContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @create 2023-03-07 17:56
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class loginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();// /backend/index.html

        log.info("拦截到请求：{}", requestURI);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/sendMsg"
        };


        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3、如果不需要处理，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //4-1、判断登录状态，如果已登录，则直接放行
        Long employeeId = (Long) request.getSession().getAttribute("employeeId");
        if (employeeId != null) {
            log.info("用户已登录，用户id为：{}", employeeId);
            //将employeeId放入ThreadLocal中，便于在MyMetaObjectHandler调用获取employeeId
            BaseContextUtil.setCurrentId(employeeId);
            filterChain.doFilter(request, response);
            return;
        }
        //4-2、判断登录状态，如果已登录，则直接放行
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId != null) {
            log.info("用户已登录，用户id为：{}", userId);
            //将employeeId放入ThreadLocal中，便于在MyMetaObjectHandler调用获取employeeId
            BaseContextUtil.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }
        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
