package com.zijin.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zijin.entity.Department;
import com.zijin.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService extends ServiceImpl<DepartmentMapper, Department> {}
