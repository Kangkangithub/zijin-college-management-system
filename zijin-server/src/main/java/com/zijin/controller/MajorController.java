package com.zijin.controller;
import com.zijin.common.Result;
import com.zijin.entity.Major;
import com.zijin.service.MajorService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/major")
public class MajorController {
    private final MajorService majorService;
    public MajorController(MajorService s) { this.majorService = s; }

    @GetMapping
    public Result<List<Major>> list() { return Result.ok(majorService.list()); }

    @PostMapping
    public Result<?> add(@RequestBody Major m) { majorService.save(m); return Result.ok(); }

    @PutMapping
    public Result<?> update(@RequestBody Major m) { majorService.updateById(m); return Result.ok(); }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) { majorService.removeById(id); return Result.ok(); }
}
