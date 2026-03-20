package com.grad.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.grad.sys.entity.SysProposal;

public interface SysProposalService extends IService<SysProposal> {
    void submitProposal(SysProposal proposal);
    void auditProposal(Long id, Integer status, String comment);
}
