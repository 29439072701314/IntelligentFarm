package com.example.intelligentfarmcore.utils;

import com.example.intelligentfarmcore.pojo.dto.UserDTO;
import com.example.intelligentfarmcore.pojo.entity.User;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    private static final Key jwtToken = Keys.hmacShaKeyFor("12345678901234567890123456789012".getBytes());
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    /**
     * 生成JWT Token
     */
     public static String createToken(User user){
         Map<String,Object> claims = new HashMap<>();
         user.setPassword(null);
         UserDTO userDTO = new UserDTO(user);
         claims.put("user",mapper.convertValue(userDTO, Map.class));
         JwtBuilder jwtBuilder = Jwts.builder()
                 .signWith(SignatureAlgorithm.HS256, jwtToken)  // 签名算法和密钥
                 .setClaims(claims)  // 自定义payload, body 数据，要唯一，自行设置
                 .setIssuedAt(new Date())   // 设置签发时间
                 .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));  // 设置过期时间为24小时
         return jwtBuilder.compact();
     }

    /**
     * 解析JWT Token
     */
     public static Map<String, Object> checkToken(String token) {
         try{
             if (token != null && token.startsWith("Bearer ")) {
                 token = token.substring(7);
             }
             Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
             return (Map<String, Object>) parse.getBody();
         }catch (Exception e){
             e.printStackTrace();
         }
         return null;
    }
    /**
     * 获取user信息
     */
     public static User getUserFromToken(String token){
         Map<String, Object> claims = checkToken(token);
         if(claims == null){
             return null;
         }
         User user = mapper.convertValue(claims.get("user"), User.class);
         return user;
     }
}
