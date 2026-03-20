package com.grad.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.grad.sys.entity.SysUser;
import com.grad.sys.mapper.SysUserMapper;
import com.grad.sys.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }
}
