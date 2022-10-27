package com.jialing.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.jialing.reggie.entity.Employee;
import com.jialing.reggie.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    // 定义不需要处理的请求
    public String[] urls = new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求：{}",request.getRequestURI());
        //1.获取本次请求的URI
        String uri = request.getRequestURI();


        //2.判断本次请求是否需要被处理
        boolean check = check(uri);
        //3.如果不需要处理，直接放行
        if (check){
            log.info("本次请求{}不需要处理",request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        //4.判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee")!=null){
            log.info("本次请求{}用户已登录，用户id为{}",request.getRequestURI(), request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }
        //5.如果未登录，则返回未登录的结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("用户未登录");
        return;

    }

    public boolean check(String requestURI){
        for (String uri :
                urls) {
            boolean match = PATH_MATCHER.match(uri, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
