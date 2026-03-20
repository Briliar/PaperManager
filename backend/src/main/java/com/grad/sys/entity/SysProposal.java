package com.grad.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_proposal")
public class SysProposal {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private Long topicId;
    private String content;
    private Integer status; // 0:待审核, 1:已通过, 2:已驳回
    private String teacherComment;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
