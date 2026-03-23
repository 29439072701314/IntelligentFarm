package com.example.intelligentfarmcore.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * 自定义过滤器：统计访问次数
 */
@WebFilter(urlPatterns = "/*")
@Component
public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 处理预检请求(直接放行)
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 访问的ip
        String ip = request.getRemoteAddr();

        // 统计访问次数并输出
        HttpSession session = request.getSession();
        Integer count = (Integer) session.getAttribute("filterCount");
        count = Objects.isNull(count) ? 1 : count + 1;
        System.out.println("ip:" + ip + " 过滤器访问次数:" + count);
        session.setAttribute("filterCount", count);

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
