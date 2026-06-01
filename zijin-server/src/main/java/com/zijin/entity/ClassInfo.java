package com.zijin.entity;
import com.baomidou.mybatisplus.annotation.*;
@TableName("t_class")
public class ClassInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String className;
    private String classCode;
    private Integer majorId;
    private String grade;
    private String description;
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getClassName() { return className; } public void setClassName(String n) { this.className = n; }
    public String getClassCode() { return classCode; } public void setClassCode(String c) { this.classCode = c; }
    public Integer getMajorId() { return majorId; } public void setMajorId(Integer m) { this.majorId = m; }
    public String getGrade() { return grade; } public void setGrade(String g) { this.grade = g; }
    public String getDescription() { return description; } public void setDescription(String d) { this.description = d; }
}
