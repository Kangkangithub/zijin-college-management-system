package com.zijin.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zijin.entity.Leave;
import com.zijin.mapper.LeaveMapper;
import org.springframework.stereotype.Service;
@Service
public class LeaveService extends ServiceImpl<LeaveMapper, Leave> {}
