package com.grad.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.grad.sys.entity.SysProposal;
import com.grad.sys.entity.SysStudentTopic;
import com.grad.sys.mapper.SysProposalMapper;
import com.grad.sys.mapper.SysStudentTopicMapper;
import com.grad.sys.service.SysProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysProposalServiceImpl extends ServiceImpl<SysProposalMapper, SysProposal> implements SysProposalService {

    @Autowired
    private SysStudentTopicMapper sysStudentTopicMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitProposal(SysProposal proposal) {
        // 1. 检查该学生是否有已通过的课题
        SysStudentTopic approvedTopic = sysStudentTopicMapper.selectOne(
            new QueryWrapper<SysStudentTopic>()
                .eq("student_id", proposal.getStudentId())
                .eq("status", 1) // 1: 已同意
        );
        
        if (approvedTopic == null) {
            throw new RuntimeException("您尚未有通过双选的课题，无法提交开题报告");
        }
        
        proposal.setTopicId(approvedTopic.getTopicId());
        
        // 2. 检查是否已经提交过
        SysProposal exist = getOne(new QueryWrapper<SysProposal>()
                .eq("student_id", proposal.getStudentId())
                .eq("topic_id", proposal.getTopicId()));
                
        if (exist != null) {
            if (exist.getStatus() == 1) {
                throw new RuntimeException("开题报告已通过审核，无法修改");
            }
            // 更新已有的（被驳回或待审核状态下修改）
            proposal.setId(exist.getId());
            proposal.setStatus(0); // 重新变为待审核
            proposal.setTeacherComment(""); // 清空旧意见
            updateById(proposal);
        } else {
            // 新增
            proposal.setStatus(0);
            save(proposal);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProposal(Long id, Integer status, String comment) {
        SysProposal proposal = getById(id);
        if (proposal == null) {
            throw new RuntimeException("开题报告不存在");
        }
        proposal.setStatus(status);
        proposal.setTeacherComment(comment);
        updateById(proposal);
    }
}
