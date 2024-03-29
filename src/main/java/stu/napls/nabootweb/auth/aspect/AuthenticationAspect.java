package stu.napls.nabootweb.auth.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import stu.napls.nabootweb.auth.annotation.Auth;
import stu.napls.nabootweb.auth.model.AuthResponse;
import stu.napls.nabootweb.auth.model.AuthVerify;
import stu.napls.nabootweb.auth.request.AuthRequest;
import stu.napls.nabootweb.core.exception.Assert;
import stu.napls.nabootweb.core.dictionary.ResponseCode;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author Tei Michael
 * @Date 12/29/2019
 */
@Aspect
@Component
public class AuthenticationAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAspect.class);

    @Resource
    private AuthRequest authRequest;

    @Around("@annotation(auth)")
    public Object processAuthentication(ProceedingJoinPoint point, Auth auth) throws Throwable {
        // Intercept http request
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        Assert.notNull(servletRequestAttributes, "Not servlet request.");

        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();

        // Get token
        String token = httpServletRequest.getHeader("Authorization");

        // Verify token
        AuthVerify authVerify = new AuthVerify();
        authVerify.setToken(token);
        AuthResponse authResponse = authRequest.verify(authVerify);

        Assert.notNull(authResponse, "Authentication failed.");
        Assert.isTrue(authResponse.getCode() == ResponseCode.SUCCESS, authResponse.getMessage());

        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute("uuid", authResponse.getData());

        // Execute controller
        Object result = point.proceed();

        // Clear session.
        httpSession.removeAttribute("uuid");

        return result;
    }

}
