package com.zijin.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zijin.entity.Comment;
import com.zijin.mapper.CommentMapper;
import org.springframework.stereotype.Service;
@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> {}
