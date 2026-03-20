package com.grad.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.grad.sys.entity.SysStudentTopic;
import com.grad.sys.entity.SysTopic;
import com.grad.sys.mapper.SysStudentTopicMapper;
import com.grad.sys.mapper.SysTopicMapper;
import com.grad.sys.service.SysStudentTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysStudentTopicServiceImpl extends ServiceImpl<SysStudentTopicMapper, SysStudentTopic> implements SysStudentTopicService {

    @Autowired
    private SysTopicMapper sysTopicMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyTopic(Long studentId, Long topicId) {
        // 1. 检查是否已经申请过该课题
        long count = count(new QueryWrapper<SysStudentTopic>()
                .eq("student_id", studentId)
                .eq("topic_id", topicId));
        if (count > 0) {
            throw new RuntimeException("您已申请过该课题，请勿重复申请");
        }
        
        // 2. 检查学生是否已经有被同意的课题了
        long successCount = count(new QueryWrapper<SysStudentTopic>()
                .eq("student_id", studentId)
                .eq("status", 1));
        if (successCount > 0) {
            throw new RuntimeException("您已有通过的课题，不能继续申请");
        }

        // 3. 检查课题限选人数
        SysTopic topic = sysTopicMapper.selectById(topicId);
        if (topic == null || topic.getStatus() != 1) {
            throw new RuntimeException("课题不存在或未通过审核");
        }
        
        long topicSelectedCount = count(new QueryWrapper<SysStudentTopic>()
                .eq("topic_id", topicId)
                .eq("status", 1));
        if (topicSelectedCount >= topic.getMaxStudents()) {
            throw new RuntimeException("该课题已达最大选课人数上限");
        }

        // 4. 创建申请记录
        SysStudentTopic application = new SysStudentTopic();
        application.setStudentId(studentId);
        application.setTopicId(topicId);
        application.setStatus(0);
        save(application);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditApplication(Long applicationId, Integer status) {
        SysStudentTopic application = getById(applicationId);
        if (application == null) {
            throw new RuntimeException("申请记录不存在");
        }
        
        if (status == 1) { // 同意
            SysTopic topic = sysTopicMapper.selectById(application.getTopicId());
            long topicSelectedCount = count(new QueryWrapper<SysStudentTopic>()
                    .eq("topic_id", application.getTopicId())
                    .eq("status", 1));
            if (topicSelectedCount >= topic.getMaxStudents()) {
                throw new RuntimeException("该课题已达最大选课人数上限，无法同意");
            }
            
            // 同意该申请后，将该学生对其他课题的待审核申请全部拒绝（一人一题）
            baseMapper.update(null, new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<SysStudentTopic>()
                    .set("status", 2)
                    .eq("student_id", application.getStudentId())
                    .eq("status", 0)
                    .ne("id", applicationId));
        }
        
        application.setStatus(status);
        updateById(application);
    }
}
