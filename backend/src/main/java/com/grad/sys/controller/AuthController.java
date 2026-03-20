package com.grad.sys.controller;

import com.grad.sys.common.JwtUtils;
import com.grad.sys.common.Result;
import com.grad.sys.entity.SysUser;
import com.grad.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "认证模块")
public class AuthController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        SysUser user = sysUserService.getByUsername(loginDTO.getUsername());
        if (user == null || !user.getPassword().equals(loginDTO.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            return Result.error("账号已被禁用");
        }
        
        String token = jwtUtils.generateToken(user.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("realName", user.getRealName());
        data.put("roleId", user.getRoleId());
        
        return Result.success(data);
    }
}

@Data
class LoginDTO {
    private String username;
    private String password;
}
