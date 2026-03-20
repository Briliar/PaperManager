package com.grad.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.grad.sys.entity.SysTopic;
import com.grad.sys.mapper.SysTopicMapper;
import com.grad.sys.service.SysTopicService;
import org.springframework.stereotype.Service;

@Service
public class SysTopicServiceImpl extends ServiceImpl<SysTopicMapper, SysTopic> implements SysTopicService {
}
