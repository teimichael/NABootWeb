package stu.napls.nabootweb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;
import stu.napls.nabootweb.auth.annotation.Auth;
import stu.napls.nabootweb.auth.model.*;
import stu.napls.nabootweb.auth.request.AuthRequest;
import stu.napls.nabootweb.config.GlobalConstant;
import stu.napls.nabootweb.config.property.SocketServer;
import stu.napls.nabootweb.core.dictionary.ResponseCode;
import stu.napls.nabootweb.core.exception.Assert;
import stu.napls.nabootweb.core.response.Response;
import stu.napls.nabootweb.model.User;
import stu.napls.nabootweb.model.vo.UserRegisterVO;
import stu.napls.nabootweb.service.UserService;
import stu.napls.nabootweb.socket.model.SocketResponse;
import stu.napls.nabootweb.socket.model.SocketThirdRegister;
import stu.napls.nabootweb.socket.request.SocketRequest;
import stu.napls.nabootweb.utils.BeanUtils;

import javax.annotation.Resource;

/**
 * @Author Tei Michael
 * @Date 12/27/2019
 */
@RestController
@RequestMapping("/access")
public class AccessController extends BaseController {

    @Resource
    private AuthRequest authRequest;

    @Resource
    private SocketRequest socketRequest;

    @Resource
    private UserService userService;

    @Resource
    private SocketServer socketServer;

    @PostMapping("/login")
    public Response<Object> login(@RequestBody AuthLogin authLogin) {
        authLogin.setSource(GlobalConstant.SERVICE_ID);
        AuthResponse authResponse = authRequest.login(authLogin);

        Assert.notNull(authResponse, "Authentication failed.");
        Assert.isTrue(authResponse.getCode() == ResponseCode.SUCCESS, authResponse.getMessage());

        return Response.success("Login successfully.", authResponse.getData());
    }

    @PostMapping("/register")
    public Response<Object> register(@RequestParam String username, @RequestParam String password, @RequestBody UserRegisterVO userRegisterVO) {
        AuthPreregister authPreregister = new AuthPreregister();
        authPreregister.setUsername(username);
        authPreregister.setPassword(password);
        authPreregister.setSource(GlobalConstant.SERVICE_ID);
        AuthResponse authResponse = authRequest.preregister(authPreregister);
        Assert.notNull(authResponse, "Preregistering auth server failed.");
        Assert.isTrue(authResponse.getCode() == ResponseCode.SUCCESS, authResponse.getMessage());
        String uuid = authResponse.getData().toString();

        // TODO Register third services.
        // Register Socket Server account if enabled
        if (socketServer.isEnabled()) {
            SocketThirdRegister socketThirdRegister = new SocketThirdRegister();
            socketThirdRegister.setUuid(uuid);
            SocketResponse socketResponse = socketRequest.registerFromThird(socketThirdRegister);
            Assert.notNull(socketResponse, "Registering socket server failed");
        }

        User user = BeanUtils.copyBean(userRegisterVO, User.class);

        user.setUuid(uuid);
        userService.update(user);

        AuthRegister authRegister = new AuthRegister();
        authRegister.setUuid(uuid);
        authResponse = authRequest.register(authRegister);
        Assert.isTrue(authResponse != null && authResponse.getCode() == ResponseCode.SUCCESS, "Register failed. Please contact the administrator.");

        return Response.success("Register successfully");
    }

    @Operation(security = @SecurityRequirement(name = "Authorization"))
    @Auth
    @PostMapping("/logout")
    public Response<Object> logout() {
        AuthLogout authLogout = new AuthLogout();
        authLogout.setUuid(getSessionUserUUID());
        AuthResponse authResponse = authRequest.logout(authLogout);
        Assert.notNull(authResponse, "Authentication failed.");
        Assert.isTrue(authResponse.getCode() == ResponseCode.SUCCESS, "Logout failed.");
        return Response.success("Logout successfully.");
    }

}
