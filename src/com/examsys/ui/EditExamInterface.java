package com.examsys.ui;

import af.swing.AfButton;
import af.swing.layout.HLayout;
import af.swing.layout.VLayout;
import com.examsys.components.EditQuestCard;
import com.examsys.components.MyButton;
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
import java.util.List;
import java.util.Objects;


//负责处理编辑考试的部分
public class EditExamInterface {

    JFrame jf = new JFrame("考试系统");
    private static User currentUser = new User("未登录","null",1);
    List<int[]> answer = new ArrayList<>();

    int screenWidth = ScreenUtils.getScreenWidth();
    int screenHeight = ScreenUtils.getScreenHeight();
    final int WIDTH = screenWidth/2;
    final int HEIGHT = screenHeight/2;


    //初始化图形界面
    public void init(User currentUser,Exam exam,List<Exam> examList) throws IOException {
        init((screenWidth-WIDTH)/2,(screenHeight-HEIGHT)/2,currentUser,exam,examList);
    }
    public void init(int x, int y, User currentUser, Exam exam,List<Exam> examList) throws IOException {
        List<Quest> quests = exam.getQuestGroup();

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
        AfButton saveBtn = new AfButton("保存");
        AfButton quitBtn = new AfButton("返回");
        AfButton newBtn = new AfButton("新建题目");
        leftBarBtn.add(saveBtn);
        leftBarBtn.add(quitBtn);

        //考试名称
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLable = new JLabel("考试名称：");
        JTextField nameTF = new JTextField(String.valueOf(exam.getExamName()));
        nameBox.add(nameLable);
        nameBox.add(nameTF);

        //考试时间
        Box timeBox = Box.createHorizontalBox();
        JLabel timeLable = new JLabel("考试时间(分钟)：");
        JTextField timeTF = new JTextField(String.valueOf(exam.getTimeLimit()));
        timeBox.add(timeLable);
        timeBox.add(timeTF);

        //需要答题的数量
        Box needAnsBox = Box.createHorizontalBox();
        JLabel needAnsLable = new JLabel("答题数量：");
        JTextField needAnsTF = new JTextField(String.valueOf(exam.getQuestNumNeedAns()));
        needAnsBox.add(needAnsLable);
        needAnsBox.add(needAnsTF);

        //组装左边栏
        leftBar.add(uLable,"10%");
        leftBar.add(nameBox,"10%");
        leftBar.add(timeBox,"10%");
        leftBar.add(needAnsBox,"10%");

        JPanel toQuest = new JPanel();
        if(quests.size()!=0){
            //题目跳转
            toQuest.setLayout(new GridLayout((quests.size() / 5) + 1, 5));
            toQuest.setBackground(Color.white);
            leftBar.add(toQuest,"auto");
        }

        leftBar.add(newBtn,"10%");
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
        if(quests.size()!=0){
            for (int i = 0; i < quests.size(); i++) {
                questPanel.add(new EditQuestCard(jf,quests.get(i),i+1,exam,examList,currentUser),"quest"+i+1);
            }
        }else {
            questPanel.add(new JLabel("还没有题目"));
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
                for (int i = 0; i < quests.size(); i++) {
                    if(e.getActionCommand().equals(String.valueOf(i+1))){
                        cardLayout.show(questPanel,"quest"+i+1);
                    }
                }
            }
        };
        //创建所有跳转按钮并绑定上一个监听函数
        if(quests.size()!=0){
            for (int i = 0; i < quests.size(); i++) {
                MyButton btn = new MyButton(String.valueOf(i+1));
                btn.addActionListener(toTargetQuest);
                toQuest.add(btn);
            }
        }

        //新建题目
        newBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object result = JOptionPane.showInputDialog(jf,"","选择题目类型",JOptionPane.DEFAULT_OPTION,null,new String[]{"单选","多选","判断","填空"},"单选");
                String[] blankChoice = {"","","",""};
                if(result==null){
                }else if(result.toString().equals("单选")){
                    Quest newQuest = new Quest(exam.getQuestIdPoint(),Quest.CHOICE,"",blankChoice,"");
                    quests.add(newQuest);
                    exam.setQuestIdPoint(exam.getQuestIdPoint()+1);//id指针自增
                }else if(result.toString().equals("多选")){
                    Quest newQuest = new Quest(exam.getQuestIdPoint(),Quest.MULTI_CHOICE,"",blankChoice,"");
                    quests.add(newQuest);
                    exam.setQuestIdPoint(exam.getQuestIdPoint()+1);//id指针自增
                }else if(result.toString().equals("判断")){
                    Quest newQuest = new Quest(exam.getQuestIdPoint(),Quest.JUDEG_TF,"",blankChoice,"");
                    quests.add(newQuest);
                    exam.setQuestIdPoint(exam.getQuestIdPoint()+1);//id指针自增
                }else if(result.toString().equals("填空")){
                    Quest newQuest = new Quest(exam.getQuestIdPoint(),Quest.Fill_BLANK,"",blankChoice,"");
                    quests.add(newQuest);
                    exam.setQuestIdPoint(exam.getQuestIdPoint()+1);//id指针自增
                }

                try {
                    new ExamService().saveExam(examList);
                    new EditExamInterface().init(jf.getX(), jf.getY(), currentUser,exam,examList);
                    jf.dispose();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(jf,"error","错误",JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });

        //保存试卷
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                exam.setExamName(nameTF.getText());
                exam.setQuestNumNeedAns(Integer.parseInt(needAnsTF.getText()));
                //获取时间限制
                if(isNumeric(timeTF.getText())){
                    int timeLimit = Integer.parseInt(timeTF.getText());
                    exam.setTimeLimit(timeLimit);
                }else {
                    JOptionPane.showMessageDialog(jf,"设置考试时间区域请输入数字","提示",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //判断题库是否多于要求
                if(quests.size()<exam.getQuestNumNeedAns()){
                    JOptionPane.showMessageDialog(jf,"题目数量少于要求数量","提示",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 存储编辑情况
                //跳转回主页面
                int result = JOptionPane.showConfirmDialog(jf,"是否保存","提示",JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    try {
                        exam.setQuestGroup(quests);
                        new ExamService().saveExam(examList);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    int returnRes = JOptionPane.showConfirmDialog(jf,"保存成功是否返回主界面","警告",JOptionPane.YES_NO_OPTION);
                    if(returnRes == JOptionPane.YES_OPTION){
                        try {
                            new InnerInterface().init(jf.getX(), jf.getY(), currentUser);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        jf.dispose();
                    }
                }
            }
        });
        //退出按钮
        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jf,"是否返回主界面，您将失去未保存的内容","提示",JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    try {
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
//        List<Exam> examList = new ExamService().getExamList();
//        new EditExamInterface().init(currentUser,examList.get(0),examList);
//    }

    //判断是否为数字函数
    private boolean isNumeric(String var1) {
        try {
            Long.valueOf(var1);
            return true;
        } catch (NumberFormatException var3) {
            return false;
        }
    }

}
