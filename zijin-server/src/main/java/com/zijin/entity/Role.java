package com.zijin.entity;
import com.baomidou.mybatisplus.annotation.*;
@TableName("t_role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String roleName;
    private String roleCode;
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getRoleName() { return roleName; } public void setRoleName(String n) { this.roleName = n; }
    public String getRoleCode() { return roleCode; } public void setRoleCode(String c) { this.roleCode = c; }
}
