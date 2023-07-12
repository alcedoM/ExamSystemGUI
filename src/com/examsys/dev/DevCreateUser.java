package com.examsys.dev;

import com.alibaba.fastjson.JSON;
import com.examsys.modules.Exam;
import com.examsys.modules.Quest;
import com.examsys.modules.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DevCreateUser {
    public static void main(String[] args) throws IOException {

        List<User> users = new ArrayList<User>();


//        Path path = Paths.get("data\\exams.txt");
//        String data = Files.readString(path);
//        exams = JSON.parseArray(data, Exam.class);
//        System.out.println(exams.get(0).getQuestGroup());

        User admin = new User("admin",String.valueOf("admin".hashCode()),1);
        User test = new User("test",String.valueOf("test".hashCode()),0);
        User std = new User("2020150901004",String.valueOf("lovemine".hashCode()),0);
        users.add(admin);
        users.add(test);
        users.add(std);


        String userdata = JSON.toJSONString(users);

        save("data\\users.txt",userdata);



    }
    private static void save(String path, String data) throws IOException {
        File savedata = new File(path);
        if(!savedata.exists()){
            savedata.createNewFile();
        }

        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(savedata), StandardCharsets.UTF_8));


        bufferedWriter.write(data);
        bufferedWriter.close();

    }
}
