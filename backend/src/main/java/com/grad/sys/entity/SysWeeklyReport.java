package com.grad.sys.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_weekly_report")
public class SysWeeklyReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private Long topicId;
    private Integer weekNum;
    private String content;
    private Integer status; // 0:待审, 1:已审, 2:需改
    private String teacherComment;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
