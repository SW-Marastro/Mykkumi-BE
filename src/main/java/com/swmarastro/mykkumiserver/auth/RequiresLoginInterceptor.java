package com.swmarastro.mykkumiserver.auth;

import com.swmarastro.mykkumiserver.auth.annotation.RequiresLogin;
import com.swmarastro.mykkumiserver.auth.token.TokenService;
import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import com.swmarastro.mykkumiserver.user.User;
import com.swmarastro.mykkumiserver.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RequiresLoginInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequiresLogin requiresLogin = handlerMethod.getMethodAnnotation(RequiresLogin.class);

        if (requiresLogin == null) {
            return true;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            if (tokenService.isValidToken(token)) { //유효한 토큰인지 검증
                UUID userUuid = tokenService.getUserUuidFromToken(token);
                User user = userService.getUserByUuid(userUuid);
                return true;
            }
        }

        throw new CommonException(ErrorCode.AUTHENTICATION_REQUIRED, "로그인이 필요한 기능입니다.", "로그인이 필요한 기능입니다.");
    }
}
