package com.examsys.ui;

import af.swing.AfButton;
import af.swing.layout.FreeLayout;
import af.swing.layout.HLayout;
import af.swing.layout.Margin;
import com.examsys.components.ExamBriefCard;
import com.examsys.modules.Exam;
import com.examsys.modules.Quest;
import com.examsys.modules.User;
import com.examsys.servises.ExamService;
import com.examsys.util.NumberLongLimit;
import com.examsys.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//负责处理新建考试的部分
public class NewExamInfo {

    JFrame jf = new JFrame("新建考试");
    //获取屏幕尺寸
    int screenWidth = ScreenUtils.getScreenWidth();
    int screenHeight = ScreenUtils.getScreenHeight();
    final int WIDTH = screenWidth/2;
    final int HEIGHT = screenHeight/2;

    //初始化图形界面
    public void init(User currentUser,List<Exam> examList) throws IOException {
        init((screenWidth-WIDTH)/2,(screenHeight-HEIGHT)/2,currentUser,examList);
    }
    public void init(int x, int y,User currentUser,List<Exam> examList) throws IOException {
        //设置窗口居中
        jf.setBounds(x,y,WIDTH,HEIGHT);

        //设置图标
        ImageIcon icon = new ImageIcon("data/icon.png");
        jf.setIconImage(icon.getImage());

        JPanel mainPanel = new JPanel();

        //新建空考试
        List<Quest> newQuests = new ArrayList<>();
        Exam newExam = new Exam("",0,0,false,newQuests);

        //声明组件
        Box vBox = Box.createVerticalBox();
        //考试名称
        Box examNameBox = Box.createHorizontalBox();
        JLabel examNameLabel = new JLabel("考试名称:");
        JTextField examNameTF = new JTextField(16);
        //题目数量
        Box questNumBox = Box.createHorizontalBox();
        JLabel questNumLabel = new JLabel("需要作答的题目数量:");
        JTextField questNumTF = new JTextField(16);
        questNumTF.setDocument(new NumberLongLimit(10));
        //时间限制
        Box timeLimitBox = Box.createHorizontalBox();
        JLabel timeLimitLabel = new JLabel("时间限制（分钟）:");
        JTextField timeLimitTF = new JTextField(16);
        timeLimitTF.setDocument(new NumberLongLimit(10));

        //按钮
        Box btnBox = Box.createHorizontalBox();
        AfButton submitBtn = new AfButton("新建");
        AfButton cancelBtn = new AfButton("取消");

        //设置交互逻辑，监听函数
        //提交按钮
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取输入
                String examName = examNameTF.getText();
                String questNum = questNumTF.getText();
                String timeLimit = timeLimitTF.getText();
                //判断输入是否为空
                if(examName.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"考试名称不能为空","提示",JOptionPane.INFORMATION_MESSAGE);
                }else if (questNum.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"题目数量不能为空","提示",JOptionPane.INFORMATION_MESSAGE);
                }else if (timeLimit.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"时间限制不能为空","提示",JOptionPane.INFORMATION_MESSAGE);
                }else {
                    newExam.setExamName(examName);
                    newExam.setQuestNumNeedAns(Integer.parseInt(questNum));
                    newExam.setTimeLimit(Integer.parseInt(timeLimit));
                    newExam.setQuestIdPoint(0);

                    examList.add(newExam);
                    try {
                        new ExamService().saveExam(examList);
                        new EditExamInterface().init(currentUser,newExam,examList);
                        jf.dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        //取消按钮
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new InnerInterface().init(currentUser);
                    jf.dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //组装视图
        //组装填空区
        examNameBox.add(examNameLabel);
        examNameBox.add(Box.createHorizontalStrut(20));
        examNameBox.add(examNameTF);

        questNumBox.add(questNumLabel);
        questNumBox.add(Box.createHorizontalStrut(20));
        questNumBox.add(questNumTF);

        timeLimitBox.add(timeLimitLabel);
        timeLimitBox.add(Box.createHorizontalStrut(20));
        timeLimitBox.add(timeLimitTF);

        //组装按钮栏
        btnBox.add(submitBtn);
        btnBox.add(Box.createHorizontalStrut(60));
        btnBox.add(cancelBtn);

        //组装整体
        vBox.add(Box.createVerticalStrut(HEIGHT/3));
        vBox.add(examNameBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(questNumBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(timeLimitBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);

        mainPanel.add(vBox);

        jf.add(mainPanel);
        //显示
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

}
