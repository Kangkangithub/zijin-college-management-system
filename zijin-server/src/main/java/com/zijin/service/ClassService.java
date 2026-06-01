package com.zijin.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zijin.entity.ClassInfo;
import com.zijin.mapper.ClassMapper;
import org.springframework.stereotype.Service;

@Service
public class ClassService extends ServiceImpl<ClassMapper, ClassInfo> {}
