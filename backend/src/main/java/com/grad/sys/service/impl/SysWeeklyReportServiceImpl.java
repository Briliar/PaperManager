package com.grad.sys.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.grad.sys.entity.SysWeeklyReport;
import com.grad.sys.mapper.SysWeeklyReportMapper;
import com.grad.sys.service.SysWeeklyReportService;
import org.springframework.stereotype.Service;
@Service
public class SysWeeklyReportServiceImpl extends ServiceImpl<SysWeeklyReportMapper, SysWeeklyReport> implements SysWeeklyReportService {}
