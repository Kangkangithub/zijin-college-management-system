package com.zijin.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zijin.entity.Major;
import com.zijin.mapper.MajorMapper;
import org.springframework.stereotype.Service;

@Service
public class MajorService extends ServiceImpl<MajorMapper, Major> {}
