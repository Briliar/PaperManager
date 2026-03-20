package com.grad.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.grad.sys.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    SysUser getByUsername(String username);
}
