package com.zijin.entity;
import com.baomidou.mybatisplus.annotation.*;
@TableName("t_announcement")
public class Announcement {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private Integer publisherId;
    private String publisherName;
    private Integer likeCount;
    private String createTime;
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; } public void setTitle(String t) { this.title = t; }
    public String getContent() { return content; } public void setContent(String c) { this.content = c; }
    public Integer getPublisherId() { return publisherId; } public void setPublisherId(Integer p) { this.publisherId = p; }
    public String getPublisherName() { return publisherName; } public void setPublisherName(String n) { this.publisherName = n; }
    public Integer getLikeCount() { return likeCount; } public void setLikeCount(Integer l) { this.likeCount = l; }
    public String getCreateTime() { return createTime; } public void setCreateTime(String t) { this.createTime = t; }
}
