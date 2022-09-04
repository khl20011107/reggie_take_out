package com.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.reggie.common.BaseContext;
import com.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
@Component
public class LoginFilter implements Filter {

    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        1、获取请求的URI
        String requestURI = request.getRequestURI();
        String[] urls = new String[]{"/employee/login", "/employee/logout", "/backend/**", "/front/**","/user/sendMsg","/user/login"};
//        2、判断请求是否要处理
        boolean check = check(urls, requestURI);
//        3、不需要处理则直接放行
        if(check) {
            filterChain.doFilter(request, response);
            return;
        }
//        4-1、判断登录状态，已登陆则直接放行(后台)
        if(request.getSession().getAttribute("employee") != null){

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }
//        4-2、判断登录状态，已登录则直接放行(前台)
        if(request.getSession().getAttribute("user") != null){

            Long userID = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userID);

            filterChain.doFilter(request, response);
            return;
        }
//        5、未登录则返回未登录的结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */

    public boolean check(String[] urls,String requestURI) {
        for (String url : urls) {
            boolean match = ANT_PATH_MATCHER.match(url, requestURI);
            if(match) {
                return true;
            }
        }
        return false;
    }

}
