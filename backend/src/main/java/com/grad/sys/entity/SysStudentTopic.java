package com.grad.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_student_topic")
public class SysStudentTopic {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private Long topicId;
    private Integer status; // 0:待审核, 1:已同意, 2:已拒绝
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
