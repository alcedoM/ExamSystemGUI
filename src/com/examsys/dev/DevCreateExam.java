package com.examsys.dev;

import com.alibaba.fastjson.JSON;
import com.examsys.modules.Exam;
import com.examsys.modules.Quest;
import com.examsys.modules.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DevCreateExam {
    public static void main(String[] args) throws IOException {

        List<Exam> exams = new ArrayList<Exam>();


//        Path path = Paths.get("data\\exams.txt");
//        String data = Files.readString(path);
//        exams = JSON.parseArray(data, Exam.class);
//        System.out.println(exams.get(0).getQuestGroup());


        List<Quest> questgroup =new ArrayList<>();
        List<Quest> questgroup2 =new ArrayList<>();
        String[] choices = {"A","B","C","D"};
        Quest q1 = new Quest(0,Quest.CHOICE,"这是第一题",choices,"a");
        Quest q2 = new Quest(1,Quest.CHOICE,"这是第二题",choices,"a");
        Quest q3 = new Quest(2,Quest.CHOICE,"这是第三题",choices,"b");
        Quest q4 = new Quest(3,Quest.CHOICE,"这是第四题",choices,"c");
        Quest q5 = new Quest(4,Quest.CHOICE,"这是第五题",choices,"d");
        Quest q6 = new Quest(5,Quest.CHOICE,"这是第六题",choices,"c");

        questgroup.add(q1);
        questgroup.add(q2);
        questgroup.add(q3);
        questgroup.add(q4);
        questgroup.add(q5);
        questgroup.add(q6);

        questgroup2.add(q1);
        questgroup2.add(q2);

        Exam exam = new Exam("第一个考试",6,30,true,questgroup);
        Exam exam1 = new Exam("第二个考试",2,60,true,questgroup2);
        exams.add(exam);
        exams.add(exam1);

        String examdata = JSON.toJSONString(exams);
        String questdata = JSON.toJSONString(questgroup);

//        save(String.format("data\\quest_%s.txt",exam.getExamName()),questdata);
//        save(String.format("data\\quest_%s.txt",exam1.getExamName()),questdata);

        save("data\\exams.txt",examdata);



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
