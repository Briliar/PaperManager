package com.grad.sys.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("sys_thesis")
public class SysThesis {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private Long topicId;
    private String fileName;
    private String fileUrl;
    private Integer type; // 0:初稿, 1:定稿
    private Integer status; // 0:待审, 1:通过, 2:打回
    private BigDecimal score;
    private String teacherComment;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
