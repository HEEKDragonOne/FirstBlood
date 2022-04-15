package com.tothefor.config;


import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tothefor.utils.JWTUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt拦截器
 * @Author DragonOne
 * @Date 2022/3/4 13:28
 * @墨水记忆 www.tothefor.com
 */


@Configuration
public class JWTInterceptor implements HandlerInterceptor {

//    @Autowired
//    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        if (token == null || token=="") {
            return false;
        }
        // 获取 token 中的用户信息
        try {
            DecodedJWT djwt = JWTUtils.getTokenInfo(token); //验证成功并返回信息，失败直接中断
            String userid = djwt.getClaim("id").asString();
            Integer id = Integer.valueOf(userid);
//            User queryUser = userMapper.queryinfoByID(id);
//            if(queryUser==null) return false;
            return true;
        } catch (JWTDecodeException j) {
            return false;
        }
    }
}
