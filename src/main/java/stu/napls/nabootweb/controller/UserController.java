package stu.napls.nabootweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stu.napls.nabootweb.auth.annotation.Auth;
import stu.napls.nabootweb.core.response.Response;
import stu.napls.nabootweb.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @Author Tei Michael
 * @Date 12/28/2019
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Auth
    @GetMapping("/get/info")
    public Response getInfo(HttpSession session) {
        return Response.success(userService.findUserByUuid(session.getAttribute("uuid").toString()));
    }

    @Auth
    @GetMapping("/get/list")
    public Response getList() {
        return Response.success(userService.findAllUser());
    }
}
