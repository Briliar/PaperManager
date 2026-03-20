package com.grad.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_topic")
public class SysTopic {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String summary;
    private String requireDesc;
    private Long teacherId;
    private String source;
    private String type;
    private Integer maxStudents;
    private Integer status; // 0:待审核, 1:已通过, 2:已驳回
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
