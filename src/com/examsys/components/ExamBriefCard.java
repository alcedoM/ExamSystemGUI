package com.examsys.components;

import af.swing.AfButton;
import com.examsys.modules.Exam;
import com.examsys.modules.User;
import com.examsys.servises.ExamService;
import com.examsys.ui.EditExamInterface;
import com.examsys.ui.ExamInterface;
import com.examsys.ui.InnerInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

//InnerInterface中显示的考试卡片，包括简单的信息
public class ExamBriefCard extends JPanel {
    public ExamBriefCard(JFrame jf, Exam exam, User currentUser, List<Exam> examList){
        //获取考试数据
        String examName = exam.getExamName();
        int questNum = exam.getQuestGroup().size();
        int needAnsNum  = exam.getQuestNumNeedAns();
        int timeLimit = exam.getTimeLimit();

        //声明组件
        Box box = Box.createVerticalBox();
        JLabel nameLabel = new JLabel(examName);
        JLabel needAnsLabel = new JLabel("题目数量："+String.valueOf(needAnsNum));
        JLabel questLabel = new JLabel("题库数量："+String.valueOf(questNum));
        JLabel timeLabel = new JLabel("时间限制："+String.valueOf(timeLimit)+"分钟");
        AfButton enter = new AfButton("进入");
        Box btnBox = Box.createHorizontalBox();
        AfButton edit = new AfButton("编辑");
        MyButton publish = new MyButton("发布");
        MyButton deleteExam = new MyButton("删除");
        MyButton exportExam = new MyButton("导出");

        //声明文件选择器
        JFileChooser chooser = new JFileChooser(".");

        //监听函数
        //编辑按钮
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new EditExamInterface().init(currentUser,exam,examList);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                jf.dispose();
            }
        });
        //进入考试按钮
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new ExamInterface().init(jf.getX(),jf.getY(),currentUser,exam);
                    jf.dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        //发布取消发布按钮
        publish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("发布")){
                    exam.setPublished(true);
                    publish.setText("取消发布");
                    try {
                        new ExamService().savePublish(examList,exam,true);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }else {
                    exam.setPublished(false);
                    publish.setText("发布");
                    try {
                        new ExamService().savePublish(examList,exam,false);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        //删除考试
        deleteExam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jf,"是否删除考试："+exam.getExamName(),"提示",JOptionPane.YES_NO_OPTION);
                if(result==JOptionPane.YES_OPTION){
                    examList.remove(exam);
                    try {
                        new ExamService().saveExam(examList);
                        new InnerInterface().init(jf.getX(),jf.getY(),currentUser);
                        jf.dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        //导出考试
        exportExam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int state = chooser.showSaveDialog(jf);
                File dir = chooser.getSelectedFile();

                if(state == JFileChooser.APPROVE_OPTION){
                    try {
                        new ExamService().exportExam(exam, dir);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        //组装
        box.add(nameLabel);
        box.add(needAnsLabel);
        if (currentUser.getIs_admin()==1){
            box.add(questLabel);
        }
        box.add(timeLabel);

        if(currentUser.getIs_admin()==1){
            btnBox.add(edit);
            if(exam.isPublished()){
                publish.setText("取消发布");
            }else{
                publish.setText("发布");
            }
            btnBox.add(publish);
            btnBox.add(deleteExam);
            btnBox.add(exportExam);
            box.add(btnBox);
        }else{
            box.add(enter);
        }

        add(box);
        setBackground(Color.white);
    }
}
