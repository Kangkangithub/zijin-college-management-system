package com.zijin.entity;
import com.baomidou.mybatisplus.annotation.*;
@TableName("t_major")
public class Major {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String majorName;
    private String majorCode;
    private Integer deptId;
    private String description;
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getMajorName() { return majorName; } public void setMajorName(String n) { this.majorName = n; }
    public String getMajorCode() { return majorCode; } public void setMajorCode(String c) { this.majorCode = c; }
    public Integer getDeptId() { return deptId; } public void setDeptId(Integer d) { this.deptId = d; }
    public String getDescription() { return description; } public void setDescription(String d) { this.description = d; }
}
