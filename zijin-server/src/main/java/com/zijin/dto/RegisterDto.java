package com.zijin.dto;
public class RegisterDto {
    private String username;
    private String pwd;
    private String confirmPwd;
    private String realName;
    private String grade;
    private Integer majorId;
    private Integer classId;
    private String phone;
    private String email;
    public String getUsername() { return username; } public void setUsername(String u) { this.username = u; }
    public String getPwd() { return pwd; } public void setPwd(String p) { this.pwd = p; }
    public String getConfirmPwd() { return confirmPwd; } public void setConfirmPwd(String c) { this.confirmPwd = c; }
    public String getRealName() { return realName; } public void setRealName(String n) { this.realName = n; }
    public String getGrade() { return grade; } public void setGrade(String g) { this.grade = g; }
    public Integer getMajorId() { return majorId; } public void setMajorId(Integer m) { this.majorId = m; }
    public Integer getClassId() { return classId; } public void setClassId(Integer c) { this.classId = c; }
    public String getPhone() { return phone; } public void setPhone(String p) { this.phone = p; }
    public String getEmail() { return email; } public void setEmail(String e) { this.email = e; }
}
