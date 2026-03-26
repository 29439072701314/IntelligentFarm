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

        // 路径匹配器
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String path : allowedPaths) {
            if (pathMatcher.match(path, request.getRequestURI())) {
                return true;
            }
        }

        // 检查 token
        String token = request.getHeader("Authorization");

        Map<String, Object> claims = JWTUtils.checkToken(token);

        //  token 不合法，返回错误
        if (claims == null){
            ResponseMessage responseMessage = ResponseMessage.error(ErrorCode.TOKEN_INVALID.getCode(), ErrorCode.TOKEN_INVALID.getMessage());

            // 转换为 JSONObject
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(responseMessage);
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
