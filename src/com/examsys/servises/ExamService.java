package com.examsys.servises;

import com.alibaba.fastjson.JSON;
import com.examsys.modules.Answer;
import com.examsys.modules.Exam;
import com.examsys.modules.Quest;
import com.examsys.modules.User;
import com.examsys.ui.InnerInterface;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//考试相关的处理
public class ExamService {
    //获取考试列表，从文件中读取
    public List<Exam> getExamList() throws IOException {
        List<Exam> examList;
        Path path = Paths.get("data\\exams.txt");
        String data = Files.readString(path);
        examList = JSON.parseArray(data, Exam.class);
        return examList;
    }
    //保存发布状态
    public String[] savePublish(List<Exam> examList,Exam exam,boolean publish) throws IOException {
        String[] res = new String[1];
        for (int i = 0; i < examList.size(); i++) {
            if(examList.get(i).getExamName().equals(exam.getExamName())){
                examList.get(i).setPublished(publish);
                saveExam(examList);
                res = new String[]{"1", "存储成功"};
            }else {
                res = new String[]{"0", "未找到该考试"};
            }
        }
        return res;
    }

    //保存考试
    public void saveExam(List<Exam> examList) throws IOException {
        File savedata = new File("data\\exams.txt");
        String data = JSON.toJSONString(examList);
        if(!savedata.exists()){
            savedata.createNewFile();
        }

        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(savedata), StandardCharsets.UTF_8));

        bufferedWriter.write(data);
        bufferedWriter.close();

    }

    //保存答题情况
    public void saveAns(String examName, String studentID, List<Quest> quests,int score) throws IOException {
        //读取本地数据
        Path path = Paths.get("data\\answers.txt");
        String data = Files.readString(path);
        List<Answer> answerList = new ArrayList<>();
        if(!data.isEmpty()){
            answerList = JSON.parseArray(data, Answer.class);
        }
        //添加答案
        Answer ans = new Answer(examName,studentID,quests,score);
        answerList.add(ans);

        //保存
        File savedata = new File("data\\answers.txt");
        String newdata = JSON.toJSONString(answerList);
        if(!savedata.exists()){
            savedata.createNewFile();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(savedata), StandardCharsets.UTF_8));

        bufferedWriter.write(newdata);
        bufferedWriter.close();
    }

    //导出考试
    public void exportExam(Exam exam, File dir) throws IOException {
        String examName = exam.getExamName();
        File savedata = new File(dir,examName+".txt");
        String data = JSON.toJSONString(exam);

        if(!savedata.exists()){
            savedata.createNewFile();
        }

        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(savedata), StandardCharsets.UTF_8));

        bufferedWriter.write(data);
        bufferedWriter.close();
    }

    //刷新jf
    public void refreshJF(JFrame jf,User currentUser,int x,int y) throws IOException {
        new InnerInterface().init(x, y,currentUser);
        jf.dispose();
    }
}
