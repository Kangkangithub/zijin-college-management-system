package com.zijin.entity;
import com.baomidou.mybatisplus.annotation.*;
@TableName("t_comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer announcementId;
    private Integer userId;
    private String userName;
    private String content;
    private String createTime;
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public Integer getAnnouncementId() { return announcementId; } public void setAnnouncementId(Integer a) { this.announcementId = a; }
    public Integer getUserId() { return userId; } public void setUserId(Integer u) { this.userId = u; }
    public String getUserName() { return userName; } public void setUserName(String n) { this.userName = n; }
    public String getContent() { return content; } public void setContent(String c) { this.content = c; }
    public String getCreateTime() { return createTime; } public void setCreateTime(String t) { this.createTime = t; }
}
