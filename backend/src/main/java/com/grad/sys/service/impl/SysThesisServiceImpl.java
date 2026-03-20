package com.grad.sys.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.grad.sys.entity.SysThesis;
import com.grad.sys.mapper.SysThesisMapper;
import com.grad.sys.service.SysThesisService;
import org.springframework.stereotype.Service;
@Service
public class SysThesisServiceImpl extends ServiceImpl<SysThesisMapper, SysThesis> implements SysThesisService {}
