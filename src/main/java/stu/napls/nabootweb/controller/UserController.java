package stu.napls.nabootweb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stu.napls.nabootweb.auth.annotation.Auth;
import stu.napls.nabootweb.config.property.StaticServer;
import stu.napls.nabootweb.core.dictionary.StaticPath;
import stu.napls.nabootweb.core.exception.Assert;
import stu.napls.nabootweb.core.response.Response;
import stu.napls.nabootweb.model.User;
import stu.napls.nabootweb.service.StorageService;
import stu.napls.nabootweb.service.UserService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author Tei Michael
 * @date 2/21/2022
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private StorageService storageService;

    @Resource
    private StaticServer staticServer;

    @Operation(security = @SecurityRequirement(name = "Authorization"))
    @Auth
    @GetMapping("/get/info")
    public Response<User> getInfo() {
        return Response.success(getSessionUser());
    }

    @Operation(security = @SecurityRequirement(name = "Authorization"))
    @Auth
    @GetMapping("/get/list")
    public Response<List<User>> getList() {
        return Response.success(userService.findAllUser());
    }

    @Operation(security = @SecurityRequirement(name = "Authorization"))
    @Auth
    @PostMapping("/post/avatar")
    public Response<User> postAvatar(@RequestParam MultipartFile avatar) throws IOException {
        Assert.isTrue(staticServer.isEnabled(), "Static Server is disabled.");
        User user = getSessionUser();
        Assert.notNull(user, "Authentication failed");
        String name = storageService.storeImage(avatar, StaticPath.AVATAR, user.getId() + "_avatar");
        user.setAvatar(staticServer.getUrl() + StaticPath.AVATAR + name);
        return Response.success(userService.update(user));
    }
}
