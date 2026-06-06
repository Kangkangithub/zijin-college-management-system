package com.zijin.entity;
import com.baomidou.mybatisplus.annotation.*;
@TableName("t_leave")
public class Leave {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer studentId;
    private String studentName;
    private Integer classId;
    private String reason;
    private String startDate;
    private String endDate;
    private String status;
    private String approverName;
    private String createTime;
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public Integer getStudentId() { return studentId; } public void setStudentId(Integer s) { this.studentId = s; }
    public String getStudentName() { return studentName; } public void setStudentName(String n) { this.studentName = n; }
    public Integer getClassId() { return classId; } public void setClassId(Integer c) { this.classId = c; }
    public String getReason() { return reason; } public void setReason(String r) { this.reason = r; }
    public String getStartDate() { return startDate; } public void setStartDate(String s) { this.startDate = s; }
    public String getEndDate() { return endDate; } public void setEndDate(String e) { this.endDate = e; }
    public String getStatus() { return status; } public void setStatus(String s) { this.status = s; }
    public String getApproverName() { return approverName; } public void setApproverName(String a) { this.approverName = a; }
    public String getCreateTime() { return createTime; } public void setCreateTime(String t) { this.createTime = t; }
}
