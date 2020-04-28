package com.fc.work.intercepts;

import com.fc.work.exception.NoAutherException;
import com.fc.work.pojo.HeaderConstant;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 权限校验
 */
public class SecurityInterceptor implements HandlerInterceptor {

    private  final Pattern pattern =   Pattern.compile("/user/login");

    private StringRedisTemplate stringRedisTemplate;

    public  SecurityInterceptor(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前是否为登陆接口
        String url = request.getRequestURI();

        //放过访问 jss 与css静态请求
        if(url.contains(".css") || url.contains(".js") || url.contains("/error")){
            return true;
        }

        //如果是登陆请求放行
        //每个请求判断是否有权限访问
        if(pattern.matcher(url).find() || "/login".equals(url)) {
            return  true;
        }
        //每次携带token信息
        String token = null;
        for(Cookie cookie : request.getCookies()){
            if(HeaderConstant.HEARD_KEY.equals(cookie.getName())){
                token = cookie.getValue();
                break;
            }
        }
        if(token == null){
            response.sendRedirect("login");
            return false;
        }

        //从redis 中获取当前token对应 用户信息
        String userInfo = stringRedisTemplate.opsForValue().get(token);

        //非登陆请求，判断当前session中是否有用户信息
        if(userInfo == null || userInfo.isEmpty()){
            //非法登陆 跳到重新登陆界面
            response.sendRedirect("login");
        }
        //设置存活时间 默认60s 每次操作重新刷新token失效时间
        stringRedisTemplate.expire(token,60, TimeUnit.SECONDS);
        return true;
    }
}
