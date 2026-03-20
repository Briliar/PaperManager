package com.grad.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grad.sys.common.Result;
import com.grad.sys.entity.SysTopic;
import com.grad.sys.service.SysTopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic")
@Tag(name = "Topic", description = "课题管理接口")
public class TopicController {

    @Autowired
    private SysTopicService sysTopicService;

    @GetMapping("/page")
    @Operation(summary = "分页查询课题")
    public Result<Page<SysTopic>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status) {
        
        Page<SysTopic> page = new Page<>(current, size);
        QueryWrapper<SysTopic> wrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(title)) {
            wrapper.like("title", title);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        
        return Result.success(sysTopicService.page(page, wrapper));
    }

    @PostMapping("/save")
    @Operation(summary = "新增或修改课题")
    public Result<Boolean> saveOrUpdate(@RequestBody SysTopic topic) {
        // 简单模拟: 实际应从Token中获取当前教师ID
        if (topic.getId() == null) {
            topic.setTeacherId(1L); 
            topic.setStatus(0); // 新增默认为待审核
        }
        return Result.success(sysTopicService.saveOrUpdate(topic));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除课题")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(sysTopicService.removeById(id));
    }
    
    @PostMapping("/audit/{id}")
    @Operation(summary = "审核课题")
    public Result<Boolean> audit(@PathVariable Long id, @RequestParam Integer status) {
        SysTopic topic = new SysTopic();
        topic.setId(id);
        topic.setStatus(status);
        return Result.success(sysTopicService.updateById(topic));
    }
}
