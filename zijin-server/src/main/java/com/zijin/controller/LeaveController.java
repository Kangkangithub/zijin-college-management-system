package com.zijin.controller;
import com.zijin.common.Result;
import com.zijin.entity.Leave;
import com.zijin.service.LeaveService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {
    private final LeaveService leaveService;
    public LeaveController(LeaveService s) { this.leaveService = s; }

    @GetMapping
    public Result<List<Leave>> list(
        @RequestParam(required = false) Integer studentId,
        @RequestParam(required = false) Integer classId) {
        QueryWrapper<Leave> qw = new QueryWrapper<>();
        if (studentId != null) qw.eq("student_id", studentId);
        if (classId != null) qw.eq("class_id", classId);
        qw.orderByDesc("id");
        return Result.ok(leaveService.list(qw));
    }

    @PostMapping
    public Result<?> add(@RequestBody Leave l) {
        l.setStatus("pending");
        l.setCreateTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date()));
        leaveService.save(l);
        return Result.ok();
    }

    @PutMapping("/approve/{id}")
    public Result<?> approve(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        Leave l = leaveService.getById(id);
        if (l != null) {
            l.setStatus(body.get("status"));
            l.setApproverName(body.getOrDefault("approverName", ""));
            leaveService.updateById(l);
        }
        return Result.ok();
    }
}
