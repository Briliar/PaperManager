package com.grad.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grad.sys.common.Result;
import com.grad.sys.entity.SysStudentTopic;
import com.grad.sys.service.SysStudentTopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-topic")
@Tag(name = "StudentTopic", description = "学生选题模块")
public class StudentTopicController {

    @Autowired
    private SysStudentTopicService sysStudentTopicService;

    @PostMapping("/apply")
    @Operation(summary = "学生申请课题")
    public Result<Boolean> apply(@RequestParam Long studentId, @RequestParam Long topicId) {
        sysStudentTopicService.applyTopic(studentId, topicId);
        return Result.success(true);
    }

    @GetMapping("/my-apply")
    @Operation(summary = "学生查看自己的申请记录")
    public Result<Page<SysStudentTopic>> myApply(
            @RequestParam Long studentId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        
        Page<SysStudentTopic> page = new Page<>(current, size);
        QueryWrapper<SysStudentTopic> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId).orderByDesc("create_time");
        return Result.success(sysStudentTopicService.page(page, wrapper));
    }

    @GetMapping("/teacher-list")
    @Operation(summary = "教师查看自己名下课题的被申请记录 (简易实现, 直接根据topicId查询)")
    public Result<Page<SysStudentTopic>> teacherList(
            @RequestParam Long topicId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        
        Page<SysStudentTopic> page = new Page<>(current, size);
        QueryWrapper<SysStudentTopic> wrapper = new QueryWrapper<>();
        wrapper.eq("topic_id", topicId).orderByDesc("create_time");
        return Result.success(sysStudentTopicService.page(page, wrapper));
    }

    @PostMapping("/audit/{id}")
    @Operation(summary = "教师审核学生的申请 (同意/拒绝)")
    public Result<Boolean> audit(@PathVariable Long id, @RequestParam Integer status) {
        sysStudentTopicService.auditApplication(id, status);
        return Result.success(true);
    }
}
