package com.zijin.controller;
import com.zijin.common.Result;
import com.zijin.entity.User;
import com.zijin.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService s) { this.userService = s; }

    @GetMapping
    public Result<List<User>> list() {
        List<User> users = userService.list();
        users.forEach(u -> u.setPwd(null));
        return Result.ok(users);
    }

    @PostMapping
    public Result<?> add(@RequestBody User user) { userService.save(user); return Result.ok(); }

    @PutMapping
    public Result<?> update(@RequestBody User user) {
        if (user.getPwd() == null || user.getPwd().isEmpty()) {
            User old = userService.getById(user.getId());
            if (old != null) user.setPwd(old.getPwd());
        }
        userService.updateById(user);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) { userService.removeById(id); return Result.ok(); }
}
