package com.grad.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.grad.sys.entity.SysStudentTopic;

public interface SysStudentTopicService extends IService<SysStudentTopic> {
    void applyTopic(Long studentId, Long topicId);
    void auditApplication(Long applicationId, Integer status);
}
