package com.examsys.ui;

import af.swing.AfButton;
import af.swing.layout.FreeLayout;
import af.swing.layout.HLayout;
import af.swing.layout.Margin;
import com.alibaba.fastjson.JSON;
import com.examsys.components.ExamBriefCard;
import com.examsys.modules.Exam;
import com.examsys.modules.User;
import com.examsys.servises.ExamService;
import com.examsys.util.ScreenUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.lang.Math.max;

//负责处理登录后的界面，包括考试列表的部分
public class InnerInterface {

    JFrame jf = new JFrame("考试系统");

    //获取屏幕尺寸
    int screenWidth = ScreenUtils.getScreenWidth();
    int screenHeight = ScreenUtils.getScreenHeight();
    final int WIDTH = screenWidth/2;
    final int HEIGHT = screenHeight/2;

    //初始化图形界面
    public void init(User currentUser) throws IOException {
        init((screenWidth-WIDTH)/2,(screenHeight-HEIGHT)/2,currentUser);
    }
    public void init(int x, int y,User currentUser) throws IOException {
        //设置窗口居中
        jf.setBounds(x,y,WIDTH,HEIGHT);

        //设置图标
        ImageIcon icon = new ImageIcon("data/icon.png");
        jf.setIconImage(icon.getImage());

        //文件选择器
        JFileChooser chooser = new JFileChooser(".");

        //获取考试
        List<Exam> examList = new ExamService().getExamList();

        JPanel mainPanel = new JPanel(new HLayout());

        //声明组件
        //左边栏
        JPanel leftBar = new JPanel();
        leftBar.setBackground(Color.white);

        JLabel uLable = new JLabel("欢迎！"+currentUser.getId());
        AfButton logoutBtn = new AfButton("登出");
        AfButton newExamBtn = new AfButton("新建考试");
        AfButton inportExamBtn = new AfButton("导入考试");

        leftBar.add(uLable);
        leftBar.add(logoutBtn);

        //管理员专属
        if(currentUser.getIs_admin()==1){
            leftBar.add(newExamBtn);
            leftBar.add(inportExamBtn);
        }

        //考试选择区
        JPanel examListPanel = new JPanel(new FreeLayout());
        JPanel innerExamListPanel = new JPanel();
        innerExamListPanel.setLayout(new GridLayout(max((examList.size()/4)+1,4),4,10,10));

        //读取考试并生成考试卡片
        if (!examList.isEmpty()){
            for (Exam exam : examList) {
                //如果发布就展示
                if(exam.isPublished() || currentUser.getIs_admin()==1){
                    innerExamListPanel.add(new ExamBriefCard(jf,exam,currentUser,examList));
                }
            }
        }else {
            innerExamListPanel.add(new JLabel("还没有考试哦！"));
        }

        //交互逻辑
        //登出逻辑
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //返回登录界面
                    new MainInterface().init(jf.getX(), jf.getY());
                    jf.dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //新建考试
        newExamBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //跳转到新建界面
                    new NewExamInfo().init(jf.getX(), jf.getY(),currentUser,examList);
                    jf.dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //导入考试
        inportExamBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //创建文件选择器
                chooser.setFileFilter(new FileNameExtensionFilter("txt","txt"));
                chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());

                //显示文件选择器
                int state = chooser.showOpenDialog(jf);
                File examName = chooser.getSelectedFile();

                if(state==JFileChooser.APPROVE_OPTION){
                    String data;
                    try {
                        //读取文件
                        data = Files.readString(examName.toPath());
                        Exam newExam = JSON.parseObject(data, Exam.class);
                        newExam.setPublished(false);
                        for (Exam exam : examList) {
                            if(exam.getExamName().equals(newExam.getExamName())){
                                JOptionPane.showMessageDialog(jf,"存在重名考试请调整后重试","error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        //不存在重名考试时导入
                        examList.add(newExam);
                        new ExamService().saveExam(examList);
                        new ExamService().refreshJF(jf,currentUser, jf.getX(), jf.getY());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(jf,"导入失败","error",JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        //组装视图
        examListPanel.add(innerExamListPanel, new Margin(10,10,10,10));
        mainPanel.add(leftBar,"20%");
        mainPanel.add(examListPanel,"80%");

        jf.add(mainPanel);
        //显示
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }


    //测试入口
//    public static void main(String[] args) throws IOException {
//        new InnerInterface().init(new User("未登录","null",1));
//    }

}
