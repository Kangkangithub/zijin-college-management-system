package com.zijin.controller;
import com.zijin.common.Result;
import com.zijin.entity.Department;
import com.zijin.service.DepartmentService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentService deptService;
    public DepartmentController(DepartmentService s) { this.deptService = s; }

    @GetMapping
    public Result<List<Department>> list() { return Result.ok(deptService.list()); }

    @PostMapping
    public Result<?> add(@RequestBody Department dept) {
        dept.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        deptService.save(dept);
        return Result.ok();
    }

    @PutMapping
    public Result<?> update(@RequestBody Department dept) { deptService.updateById(dept); return Result.ok(); }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) { deptService.removeById(id); return Result.ok(); }
}
