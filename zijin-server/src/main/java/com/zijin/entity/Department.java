package com.zijin.entity;
import com.baomidou.mybatisplus.annotation.*;
@TableName("t_department")
public class Department {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String deptName;
    private String deptCode;
    private String description;
    private String createTime;
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getDeptName() { return deptName; } public void setDeptName(String n) { this.deptName = n; }
    public String getDeptCode() { return deptCode; } public void setDeptCode(String c) { this.deptCode = c; }
    public String getDescription() { return description; } public void setDescription(String d) { this.description = d; }
    public String getCreateTime() { return createTime; } public void setCreateTime(String t) { this.createTime = t; }
}
