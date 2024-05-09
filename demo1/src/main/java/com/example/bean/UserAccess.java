package com.example.bean;/**
 * @program:demo1
 * @description
 * @author:Xieshuyang
 * @create:
 **/

import java.util.List;

/**
 *@description:
 **/
public class UserAccess {
    private int userId;

    private String accountName;

    private String role;

    private List<String> endpoints;

    public UserAccess() {
    }

    public UserAccess(int userId, List<String> endpoints) {
        this.userId = userId;
        this.endpoints = endpoints;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public String toString() {
        return "UserAccess{" +
                "userId=" + userId +
                ", accountName='" + accountName + '\'' +
                ", role='" + role + '\'' +
                ", endpoints=" + endpoints +
                '}';
    }
}
