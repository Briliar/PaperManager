package com.grad.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grad.sys.common.Result;
import com.grad.sys.entity.SysProposal;
import com.grad.sys.service.SysProposalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proposal")
@Tag(name = "Proposal", description = "开题报告模块")
public class ProposalController {

    @Autowired
    private SysProposalService sysProposalService;

    @PostMapping("/submit")
    @Operation(summary = "学生提交开题报告")
    public Result<Boolean> submit(@RequestBody SysProposal proposal) {
        sysProposalService.submitProposal(proposal);
        return Result.success(true);
    }

    @GetMapping("/my")
    @Operation(summary = "学生查看自己的开题报告")
    public Result<SysProposal> myProposal(@RequestParam Long studentId) {
        SysProposal proposal = sysProposalService.getOne(
            new QueryWrapper<SysProposal>().eq("student_id", studentId)
        );
        return Result.success(proposal);
    }

    @GetMapping("/teacher-list")
    @Operation(summary = "教师查看名下学生的开题报告")
    public Result<Page<SysProposal>> teacherList(
            @RequestParam Long topicId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        
        Page<SysProposal> page = new Page<>(current, size);
        QueryWrapper<SysProposal> wrapper = new QueryWrapper<>();
        wrapper.eq("topic_id", topicId).orderByDesc("create_time");
        return Result.success(sysProposalService.page(page, wrapper));
    }

    @PostMapping("/audit/{id}")
    @Operation(summary = "教师审核开题报告")
    public Result<Boolean> audit(
            @PathVariable Long id, 
            @RequestParam Integer status, 
            @RequestParam(required = false) String comment) {
        sysProposalService.auditProposal(id, status, comment);
        return Result.success(true);
    }
}
