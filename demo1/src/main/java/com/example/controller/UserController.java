package com.example.controller;/**
 * @program:demo1
 * @description
 * @author:Xieshuyang
 * @create:
 **/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AccessService accessService;

    @GetMapping("/{resource}")
    public ResponseEntity<String> getUserResource(@PathVariable("resource") String resource,
                                                  @RequestHeader("Authorization") String encodedHeader) {
        // 解码请求头中的角色信息
        String decodedHeader = new String(Base64.getDecoder().decode(encodedHeader));

        // 提取角色信息
        JSONObject headerJson = JSON.parseObject(decodedHeader);
        int userId = headerJson.getIntValue("userId");

        // 验证用户是否有访问权限
        if (accessService.hasAccess(userId, resource)) {
            return ResponseEntity.ok("Success: User has access to resource " + resource);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failure: User does not have access to resource " + resource);
        }
    }
}
