package com.zijin.controller;
import com.zijin.common.Result;
import com.zijin.entity.Announcement;
import com.zijin.entity.Comment;
import com.zijin.service.AnnouncementService;
import com.zijin.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final CommentService commentService;
    public AnnouncementController(AnnouncementService a, CommentService c) {
        this.announcementService = a; this.commentService = c;
    }

    @GetMapping
    public Result<List<Map<String, Object>>> list() {
        List<Announcement> list = announcementService.list(
            new QueryWrapper<Announcement>().orderByDesc("id"));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Announcement a : list) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", a.getId());
            m.put("title", a.getTitle());
            m.put("content", a.getContent());
            m.put("publisherId", a.getPublisherId());
            m.put("publisherName", a.getPublisherName());
            m.put("likeCount", a.getLikeCount());
            m.put("createTime", a.getCreateTime());
            m.put("comments", commentService.list(
                new QueryWrapper<Comment>().eq("announcement_id", a.getId()).orderByAsc("id")));
            result.add(m);
        }
        return Result.ok(result);
    }

    @PostMapping
    public Result<?> add(@RequestBody Announcement a) {
        a.setCreateTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date()));
        a.setLikeCount(0);
        announcementService.save(a);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        announcementService.removeById(id);
        commentService.remove(new QueryWrapper<Comment>().eq("announcement_id", id));
        return Result.ok();
    }

    @PutMapping("/like/{id}")
    public Result<?> like(@PathVariable Integer id) {
        Announcement a = announcementService.getById(id);
        if (a != null) { a.setLikeCount(a.getLikeCount() + 1); announcementService.updateById(a); }
        return Result.ok();
    }

    @PostMapping("/comment")
    public Result<?> comment(@RequestBody Comment c) {
        c.setCreateTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date()));
        commentService.save(c);
        return Result.ok();
    }
}
