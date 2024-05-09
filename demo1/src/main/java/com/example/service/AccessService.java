package com.example.service;/**
 * @program:demo1
 * @description
 * @author:Xieshuyang
 * @create:
 **/

import com.example.bean.UserAccess;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *@description:
 **/


@Service
public class AccessService {
    private static final String ACCESS_FILE_PATH = "access.txt";
    private Map<Integer, UserAccess> userAccessMap;

    public AccessService() {
        File file = new File(ACCESS_FILE_PATH);
        if (file.exists()){
        }else {
            try {
                file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        userAccessMap = new HashMap<Integer, UserAccess>();
        loadAccessFromFile();
    }

    public void addUserAccess(UserAccess userAccess) {
        userAccessMap.put(userAccess.getUserId(), userAccess);
        saveAccessToFile();
    }

    public boolean hasAccess(int userId, String resource) {
        UserAccess userAccess = userAccessMap.get(userId);
        return userAccess != null && userAccess.getEndpoints().contains(resource);
    }

    private void loadAccessFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ACCESS_FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                UserAccess userAccess = parseUserAccess(line);
                userAccessMap.put(userAccess.getUserId(), userAccess);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAccessToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ACCESS_FILE_PATH));
            for (UserAccess userAccess : userAccessMap.values()) {
                writer.write(userAccess.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UserAccess parseUserAccess(String line) {
        // 解析文件中的访问信息
        String[] parts = line.split(":");
        int userId = Integer.parseInt(parts[0]);
        String[] resources = parts[1].split(",");
        UserAccess userAccess = new UserAccess(userId, Arrays.asList(resources));
        return userAccess;
    }
}
