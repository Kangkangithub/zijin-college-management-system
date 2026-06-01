package com.zijin.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zijin.dto.LoginDto;
import com.zijin.dto.RegisterDto;
import com.zijin.entity.User;
import com.zijin.mapper.UserMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    public User login(LoginDto dto) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", dto.getUsername());
        qw.eq("pwd", dto.getPwd());
        return this.getOne(qw);
    }

    public Map<String, Object> register(RegisterDto dto) {
        if (!dto.getPwd().equals(dto.getConfirmPwd())) {
            return Map.of("ok", false, "msg", "两次密码不一致");
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", dto.getUsername());
        if (this.count(qw) > 0) {
            return Map.of("ok", false, "msg", "用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPwd(dto.getPwd());
        user.setRealName(dto.getRealName());
        user.setGrade(dto.getGrade());
        user.setMajorId(dto.getMajorId());
        user.setClassId(dto.getClassId());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRoleId(3); // default student
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.save(user);
        return Map.of("ok", true, "msg", "注册成功");
    }
}
