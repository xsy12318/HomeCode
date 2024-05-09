package com.example.controller;/**
 * @program:demo1
 * @description
 * @author:Xieshuyang
 * @create:
 **/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.bean.UserAccess;
import com.example.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

/**
 *@description:
 **/
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AccessService accessService;

    @PostMapping("/addUser")
    public ResponseEntity<String> addUserAccess(@RequestBody UserAccess userAccess,
                                                @RequestHeader("Authorization") String encodedHeader) {
        // 解码请求头中的角色信息
        String decodedHeader = new String(Base64.getDecoder().decode(encodedHeader));

        // 提取角色信息
        JSONObject headerJson = JSON.parseObject(decodedHeader);
        String role = headerJson.getString("role");

        // 验证用户角色是否为管理员
        if ("admin".equals(role)) {
            // 添加用户访问权限
            accessService.addUserAccess(userAccess);
            return ResponseEntity.ok("User access added");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin can add user access");
        }
    }
}