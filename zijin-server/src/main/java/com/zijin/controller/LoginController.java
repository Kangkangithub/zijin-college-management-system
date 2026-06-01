package com.zijin.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zijin.common.Result;
import com.zijin.dto.LoginDto;
import com.zijin.dto.RegisterDto;
import com.zijin.entity.User;
import com.zijin.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final UserService userService;
    public LoginController(UserService userService) { this.userService = userService; }

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDto dto) {
        User user = userService.login(dto);
        if (user == null) {
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("username", dto.getUsername());
            if (userService.count(qw) == 0) {
                return Result.fail("NOT_EXIST");
            }
            return Result.fail("密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) return Result.fail("账号已被禁用");
        user.setPwd(null);
        return Result.ok(user);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterDto dto) {
        Map<String, Object> result = userService.register(dto);
        if ((boolean) result.get("ok")) return Result.ok(result.get("msg"));
        return Result.fail((String) result.get("msg"));
    }
}
