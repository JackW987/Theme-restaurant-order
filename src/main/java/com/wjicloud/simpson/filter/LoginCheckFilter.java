package com.wjicloud.simpson.filter;

import com.alibaba.fastjson.JSON;
import com.wjicloud.simpson.common.BaseContext;
import com.wjicloud.simpson.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 判断是否未登录就直接访问内部页面
 * 采用过滤器的形式
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获取请求路径
        String requestURI = request.getRequestURI();
        // 放行的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/backend/page/demo/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        // 检查
        boolean check = check(urls, requestURI);
        // 检查为路径内，成功放行
        if(check){
            filterChain.doFilter(request,response);
            return;
        }
        //检查失败，为非放行路径，检查登录状态
        Object employee = request.getSession().getAttribute("employee");
        if(employee!=null){
            Long empId = (Long) employee;
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }
        //判断移动端用户是否登录
        Object user = request.getSession().getAttribute("user");
        if(user!=null){
            Long userId = (Long) user;
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }
        // 登录状态检测失败，返还结果给前端
        // 这里直接拦截然后response回去了，不需要filterChain放行了
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check(String[] urls,String requestUrl){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if(match){
                 return true;
            }
        }
        return false;
    }
}
