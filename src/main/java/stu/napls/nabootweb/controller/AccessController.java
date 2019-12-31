package stu.napls.nabootweb.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stu.napls.nabootweb.auth.annotation.Auth;
import stu.napls.nabootweb.auth.model.AuthRegister;
import stu.napls.nabootweb.auth.model.AuthResponse;
import stu.napls.nabootweb.auth.request.AuthRequest;
import stu.napls.nabootweb.core.exception.Assert;
import stu.napls.nabootweb.core.response.Response;
import stu.napls.nabootweb.core.response.ResponseCode;
import stu.napls.nabootweb.model.User;
import stu.napls.nabootweb.service.UserService;
import stu.napls.nabootweb.socket.model.SocketRegister;
import stu.napls.nabootweb.socket.model.SocketResponse;
import stu.napls.nabootweb.socket.request.SocketRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @Author Tei Michael
 * @Date 12/27/2019
 */
@RestController
@RequestMapping("/access")
public class AccessController {

    @Resource
    private AuthRequest authRequest;

    @Resource
    private SocketRequest socketRequest;

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Response login(String username, String password) {
        System.out.println(username);
        Response response;
        AuthResponse authResponse = authRequest.login(username, password);
        Assert.notNull(authResponse, "Authentication failed.");
        if (authResponse.getCode() == ResponseCode.SUCCESS) {
            response = Response.success("Login successfully.", authResponse.getData());
        } else {
            response = Response.failure(authResponse.getMessage());

        }
        return response;
    }

    @PostMapping("/register")
    public Response register(String username, String password, @RequestBody User user) {
        AuthResponse authResponse = authRequest.preregister(username, password);
        Assert.notNull(authResponse, "Preregistering auth server failed.");
        Assert.isTrue(authResponse.getCode() == ResponseCode.SUCCESS, authResponse.getMessage());
        String uuid = authResponse.getData().toString();

        SocketRegister socketRegister = new SocketRegister();
        socketRegister.setUuid(uuid);
        SocketResponse socketResponse = socketRequest.register(socketRegister);
        Assert.notNull(socketResponse, "Registering socket server failed");

        user.setUuid(uuid);
        userService.create(user);

        AuthRegister authRegister = new AuthRegister();
        authRegister.setUuid(uuid);
        authResponse = authRequest.register(authRegister);
        Assert.isTrue(authResponse != null && authResponse.getCode() == ResponseCode.SUCCESS, "Register failed. Please contact the administrator.");

        return Response.success("Register successfully");
    }

    @Auth
    @PostMapping("/logout")
    public Response logout(HttpSession session) {
        AuthResponse authResponse = authRequest.logout(session.getAttribute("uuid").toString());
        Assert.notNull(authResponse, "Authentication failed.");
        Assert.isTrue(authResponse.getCode() == ResponseCode.SUCCESS, "Logout failed.");
        return Response.success("Logout successfully.");
    }

}
