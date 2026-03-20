package com.grad.sys.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grad.sys.common.Result;
import com.grad.sys.entity.SysWeeklyReport;
import com.grad.sys.service.SysWeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weekly")
public class WeeklyReportController {
    @Autowired
    private SysWeeklyReportService sysWeeklyReportService;

    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody SysWeeklyReport report) {
        if(report.getId() == null) report.setStatus(0);
        return Result.success(sysWeeklyReportService.saveOrUpdate(report));
    }

    @GetMapping("/page")
    public Result<Page<SysWeeklyReport>> page(@RequestParam(required=false) Long studentId, @RequestParam(required=false) Long topicId, @RequestParam(defaultValue="1") Integer current, @RequestParam(defaultValue="10") Integer size) {
        QueryWrapper<SysWeeklyReport> wrapper = new QueryWrapper<>();
        if(studentId != null) wrapper.eq("student_id", studentId);
        if(topicId != null) wrapper.eq("topic_id", topicId);
        wrapper.orderByDesc("week_num", "create_time");
        return Result.success(sysWeeklyReportService.page(new Page<>(current, size), wrapper));
    }

    @PostMapping("/audit/{id}")
    public Result<Boolean> audit(@PathVariable Long id, @RequestParam Integer status, @RequestParam(required=false) String comment) {
        SysWeeklyReport report = new SysWeeklyReport();
        report.setId(id); report.setStatus(status); report.setTeacherComment(comment);
        return Result.success(sysWeeklyReportService.updateById(report));
    }
}
