package com.examsys.ui;

import af.swing.AfButton;
import af.swing.layout.HLayout;
import af.swing.layout.VLayout;
import com.examsys.components.MyButton;
import com.examsys.components.QuestCard;
import com.examsys.modules.Exam;
import com.examsys.modules.Quest;
import com.examsys.modules.User;
import com.examsys.servises.ExamService;
import com.examsys.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//负责处理考试界面的部分
public class ExamInterface {

    private static User currentUser = new User("未登录","null",0);
    JFrame jf = new JFrame("考试系统");
    List<int[]> answer = new ArrayList<>();

    int screenWidth = ScreenUtils.getScreenWidth();
    int screenHeight = ScreenUtils.getScreenHeight();
    final int WIDTH = screenWidth/2;
    final int HEIGHT = screenHeight/2;
    Boolean submitFlag = false;


    //初始化图形界面
    public void init(User currentUser,Exam exam) throws IOException {
        init((screenWidth-WIDTH)/2,(screenHeight-HEIGHT)/2,currentUser,exam);
    }
    public void init(int x, int y, User currentUser, Exam exam) throws IOException {
        //获取题目
        List<Quest> quests = exam.getQuestGroup();
        //随机抽取题目
        Collections.shuffle(quests);
        List<Quest> randomQuests = quests.subList(0,exam.getQuestNumNeedAns());

        //设置窗口居中
        jf.setBounds(x,y,WIDTH,HEIGHT);

        //设置图标
        ImageIcon icon = new ImageIcon("data/icon.png");
        jf.setIconImage(icon.getImage());

        //声明组件
        JPanel mainPanel = new JPanel(new HLayout());
        //左边栏
        JPanel leftBar = new JPanel(new VLayout());
        leftBar.setBackground(Color.white);

        Box leftBarBtn = Box.createHorizontalBox();

        JLabel uLable = new JLabel("用户："+currentUser.getId());
        AfButton submitBtn = new AfButton("提交");
        AfButton quitBtn = new AfButton("放弃");
        leftBarBtn.add(submitBtn);
        leftBarBtn.add(quitBtn);

        //考试时间
        JLabel timeLable = new JLabel("考试时间："+exam.getTimeLimit()+"分钟");
        //多线程倒计时
        Thread timeCount = new Thread(() -> {
            int sec = 59;
            //获取考试时间
            int min = exam.getTimeLimit() - 1;
            //计时
            for (int i = 0; i < exam.getTimeLimit() * 60; i++) {
                try {
                    timeLable.setText("考试时间：" + min + ":" + sec);
                    if (min == 0 && sec == 0) {
                        JOptionPane.showMessageDialog(jf, "时间到,自动提交考卷", "提示", JOptionPane.INFORMATION_MESSAGE);
                        scoreAndReturn(exam, randomQuests, true, currentUser);
                    }
                    Thread.sleep(1000);
                    sec--;
                    if (sec < 0) {
                        sec = 59;
                        min--;
                    }
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        timeCount.start();

        //题目跳转
        JPanel toQuest = new JPanel(new GridLayout((randomQuests.size()/5)+1,5));
        toQuest.setBackground(Color.white);

        leftBar.add(uLable,"10%");
        leftBar.add(timeLable,"10%");
        leftBar.add(toQuest,"auto");
        leftBar.add(leftBarBtn,"10%");


        //考试区
        CardLayout cardLayout = new CardLayout();
        JPanel examPanel = new JPanel(new BorderLayout());
        JPanel questPanel = new JPanel(cardLayout);
        //按钮区
        Box btnBox = Box.createHorizontalBox();
        AfButton next = new AfButton("下一题");
        AfButton pre = new AfButton("上一题");

        btnBox.add(Box.createHorizontalGlue());
        btnBox.add(Box.createHorizontalGlue());
        btnBox.add(pre);
        btnBox.add(next);

        //插入题目卡片
        for (int i = 0; i < randomQuests.size(); i++) {
            questPanel.add(new QuestCard(randomQuests.get(i),this,i+1),"quest"+i+1);
        }

        //设置监听函数
        //下一题
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               cardLayout.next(questPanel);
            }
        });
        //上一题
        pre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.previous(questPanel);
            }
        });
        //跳转特定题目
        ActionListener toTargetQuest = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < exam.getQuestNumNeedAns(); i++) {
                    if(e.getActionCommand().equals(String.valueOf(i+1))){
                        cardLayout.show(questPanel,"quest"+i+1);
                    }
                }
            }
        };
        //创建所有跳转按钮并绑定上一个监听函数
        for (int i = 0; i < exam.getQuestNumNeedAns(); i++) {
            MyButton btn = new MyButton(String.valueOf(i+1));
            btn.addActionListener(toTargetQuest);
            toQuest.add(btn);
        }

        //提交试卷
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取当前的答题情况
                List<String> ans = getAnswer(randomQuests);

                //存储答题情况
                //跳转回主页面
                if(!ans.contains(null)){
                    int result = JOptionPane.showConfirmDialog(jf,"是否交卷","提示",JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION){
                        try {
                            timeCount.interrupt();
                            scoreAndReturn(exam,randomQuests,true,currentUser);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else{
                    int result = JOptionPane.showConfirmDialog(jf,"您还有题目未作答，是否交卷","提示",JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION){
                        try {
                            timeCount.interrupt();
                            scoreAndReturn(exam,randomQuests,true,currentUser);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }

            }
        });

        //推出考试按钮
        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jf,"是否放弃答题","提示",JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    try {
                        timeCount.interrupt();
                        new InnerInterface().init(jf.getX(), jf.getY(), currentUser);
                        jf.dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        //总装视图
        examPanel.add(questPanel);
        examPanel.add(btnBox,BorderLayout.SOUTH);

        mainPanel.add(leftBar,"20%");
        mainPanel.add(examPanel,"80%");

        jf.add(mainPanel);
        //显示
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    //测试入口
//    public static void main(String[] args) throws IOException {
//        new ExamInterface().init(currentUser,new ExamService().getExamList().get(0));
//    }

    //获取答案函数
    public List<String> getAnswer(List<Quest> quests){
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < quests.size(); i++) {
            ans.add(quests.get(i).getCurrentAns());
        }
        return ans;
    }

    //打分，保存并返回函数
    public void scoreAndReturn(Exam exam,List<Quest> quests,boolean socreFlag,User currentUser) throws IOException {
        //自动判卷
        if(socreFlag){
            int correctNum = 0;
            for (Quest quest : quests) {
                if(quest.getCurrentAns()!=null && quest.getCurrentAns().equals(quest.getCorrectAns())){
                    correctNum++;
                }
            }
            //保存答案
            new ExamService().saveAns(exam.getExamName(),currentUser.getId(),quests,correctNum);
            JOptionPane.showMessageDialog(jf,"您答对了："+correctNum+"/"+quests.size(),"结果",JOptionPane.INFORMATION_MESSAGE);
        }
        try {
            new InnerInterface().init(jf.getX(), jf.getY(), currentUser);
            jf.dispose();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
