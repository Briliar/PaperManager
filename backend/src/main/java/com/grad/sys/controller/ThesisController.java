package com.grad.sys.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grad.sys.common.Result;
import com.grad.sys.entity.SysThesis;
import com.grad.sys.service.SysThesisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/thesis")
public class ThesisController {
    @Autowired
    private SysThesisService sysThesisService;

    @PostMapping("/submit")
    public Result<Boolean> submit(@RequestBody SysThesis thesis) {
        if(thesis.getId() == null) thesis.setStatus(0);
        return Result.success(sysThesisService.saveOrUpdate(thesis));
    }

    @GetMapping("/page")
    public Result<Page<SysThesis>> page(@RequestParam(required=false) Long studentId, @RequestParam(required=false) Long topicId, @RequestParam(defaultValue="1") Integer current, @RequestParam(defaultValue="10") Integer size) {
        QueryWrapper<SysThesis> wrapper = new QueryWrapper<>();
        if(studentId != null) wrapper.eq("student_id", studentId);
        if(topicId != null) wrapper.eq("topic_id", topicId);
        wrapper.orderByDesc("create_time");
        return Result.success(sysThesisService.page(new Page<>(current, size), wrapper));
    }

    @PostMapping("/audit/{id}")
    public Result<Boolean> audit(@PathVariable Long id, @RequestParam Integer status, @RequestParam(required=false) String comment, @RequestParam(required=false) java.math.BigDecimal score) {
        SysThesis t = new SysThesis();
        t.setId(id); t.setStatus(status); t.setTeacherComment(comment); t.setScore(score);
        return Result.success(sysThesisService.updateById(t));
    }
}
