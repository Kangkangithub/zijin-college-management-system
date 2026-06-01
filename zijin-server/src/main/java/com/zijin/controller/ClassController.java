package com.zijin.controller;
import com.zijin.common.Result;
import com.zijin.entity.ClassInfo;
import com.zijin.service.ClassService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/class")
public class ClassController {
    private final ClassService classService;
    public ClassController(ClassService s) { this.classService = s; }

    @GetMapping
    public Result<List<ClassInfo>> list() { return Result.ok(classService.list()); }

    @PostMapping
    public Result<?> add(@RequestBody ClassInfo c) { classService.save(c); return Result.ok(); }

    @PutMapping
    public Result<?> update(@RequestBody ClassInfo c) { classService.updateById(c); return Result.ok(); }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) { classService.removeById(id); return Result.ok(); }
}
