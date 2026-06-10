package com.zijin.controller;
import com.zijin.common.Result;
import com.zijin.service.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final DepartmentService deptSvc;
    private final ClassService classSvc;
    private final UserService userSvc;
    private final MajorService majorSvc;
    private final AnnouncementService annSvc;
    private final LeaveService leaveSvc;
    public StatsController(DepartmentService d, ClassService c, UserService u, MajorService m, AnnouncementService a, LeaveService l) {
        this.deptSvc = d; this.classSvc = c; this.userSvc = u; this.majorSvc = m; this.annSvc = a; this.leaveSvc = l;
    }

    @GetMapping
    public Result<Map<String, Object>> stats() {
        Map<String, Object> map = new HashMap<>();
        map.put("deptCount", deptSvc.count());
        map.put("classCount", classSvc.count());
        map.put("userCount", userSvc.count());
        map.put("majorCount", majorSvc.count());
        map.put("announcementCount", annSvc.count());
        map.put("leaveCount", leaveSvc.count());
        map.put("adminCount", userSvc.list().stream().filter(u -> u.getRoleId() != null && u.getRoleId() == 1).count());
        map.put("teacherCount", userSvc.list().stream().filter(u -> u.getRoleId() != null && u.getRoleId() == 2).count());
        map.put("studentCount", userSvc.list().stream().filter(u -> u.getRoleId() != null && u.getRoleId() == 3).count());
        return Result.ok(map);
    }
}
