package com.zijin.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zijin.entity.Announcement;
import com.zijin.mapper.AnnouncementMapper;
import org.springframework.stereotype.Service;
@Service
public class AnnouncementService extends ServiceImpl<AnnouncementMapper, Announcement> {}
