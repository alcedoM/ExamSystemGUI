package com.examsys.servises;

import com.alibaba.fastjson.JSON;
import com.examsys.modules.User;

import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RegisterServise {
    public static String[] register(String id, String pwd) throws IOException {
        //��ȡ�û��ļ�
        List<User> users = new ArrayList<User>();
        Path path = Paths.get("data\\users.txt");
        String data = Files.readString(path);
        users = JSON.parseArray(data, User.class);

        //�ж��û��Ƿ��Ѿ�ע��
        for (User loopUser : users) {
            if (loopUser.getId().equals(id)) {
                return new String[]{"0", "��ѧ���Ѿ�ע�ᣬ��ǰ����¼"};
            }
        }

        //ע��
        User newUser = new User(id,String.valueOf(pwd.hashCode()),0);
        users.add(newUser);
        String saveUser = JSON.toJSONString(users);
        //������Ϣ���ļ�
        File save = new File("data\\users.txt");
        if(!save.exists()){
            save.createNewFile();
        }
        FileWriter writer = new FileWriter(save);
        BufferedWriter out = new BufferedWriter(writer);
        out.write(saveUser);
        out.flush();
        out.close();
        return new String[]{"1","ע��ɹ�"};
    }

}
