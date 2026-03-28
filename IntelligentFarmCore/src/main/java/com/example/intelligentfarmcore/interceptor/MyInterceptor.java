package com.example.intelligentfarmcore.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.intelligentfarmcore.pojo.enums.ErrorCode;
import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.util.AntPathMatcher;
import java.util.Map;

public class MyInterceptor implements HandlerInterceptor {
    // 请求执行前，实现预处理（如登录检查），第三个参数为响应的处理器，自定义控制器

    // 直接放行的接口列表
    private final String[] allowedPaths = {
            "/user/login",
            "/user/register",
            "/upload/avatar",
            "/upload/avatar/**",
    };
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 处理预检请求(直接放行)
        if (request.getMethod().equals("OPTIONS")){
            return true;
        }

        // 获取请求路径
        String requestURI = request.getRequestURI();
        String servletPath = request.getServletPath();
        
        // 打印日志用于调试
        System.out.println("Request URI: " + requestURI);
        System.out.println("Servlet Path: " + servletPath);

        // 路径匹配器 - 使用 servletPath 进行匹配（不包含 /api 前缀）
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String path : allowedPaths) {
            // 尝试匹配 servletPath（不包含 /api 前缀）
            if (pathMatcher.match(path, servletPath)) {
                System.out.println("Path matched (servletPath): " + path);
                return true;
            }
            // 也尝试匹配 requestURI（包含 /api 前缀）
            if (pathMatcher.match("/api" + path, requestURI)) {
                System.out.println("Path matched (requestURI with /api): " + "/api" + path);
                return true;
            }
            // 使用 startsWith 进行简单匹配
            if (servletPath.startsWith(path.replace("/**", ""))) {
                System.out.println("Path matched (startsWith): " + path);
                return true;
            }
            if (requestURI.startsWith("/api" + path.replace("/**", ""))) {
                System.out.println("Path matched (startsWith with /api): " + "/api" + path);
                return true;
            }
        }

        // 检查 token
        String token = request.getHeader("Authorization");
        
        System.out.println("Authorization header: " + token);

        Map<String, Object> claims = JWTUtils.checkToken(token);

        //  token 不合法，返回错误
        if (claims == null){
            System.out.println("Token validation failed");
            ResponseMessage responseMessage = ResponseMessage.error(ErrorCode.TOKEN_INVALID.getCode(), ErrorCode.TOKEN_INVALID.getMessage());

            // 转换为 JSONObject
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(responseMessage);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 返回 401 而不是 403
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonObject.toString());
            return false;
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", claims.get("user"));
        // 放行
        return true;
    }
}
