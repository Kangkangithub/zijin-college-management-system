package com.zijin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zijin.entity.User;
import org.apache.ibatis.annotations.Select;
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM t_user WHERE username = #{username}")
    User findByUsername(String username);
}
