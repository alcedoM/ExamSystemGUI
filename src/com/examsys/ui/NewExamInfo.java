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

//�������½����ԵĲ���
public class NewExamInfo {

    JFrame jf = new JFrame("�½�����");
    //��ȡ��Ļ�ߴ�
    int screenWidth = ScreenUtils.getScreenWidth();
    int screenHeight = ScreenUtils.getScreenHeight();
    final int WIDTH = screenWidth/2;
    final int HEIGHT = screenHeight/2;

    //��ʼ��ͼ�ν���
    public void init(User currentUser,List<Exam> examList) throws IOException {
        init((screenWidth-WIDTH)/2,(screenHeight-HEIGHT)/2,currentUser,examList);
    }
    public void init(int x, int y,User currentUser,List<Exam> examList) throws IOException {
        //���ô��ھ���
        jf.setBounds(x,y,WIDTH,HEIGHT);

        //����ͼ��
        ImageIcon icon = new ImageIcon("data/icon.png");
        jf.setIconImage(icon.getImage());

        JPanel mainPanel = new JPanel();

        //�½��տ���
        List<Quest> newQuests = new ArrayList<>();
        Exam newExam = new Exam("",0,0,false,newQuests);

        //�������
        Box vBox = Box.createVerticalBox();
        //��������
        Box examNameBox = Box.createHorizontalBox();
        JLabel examNameLabel = new JLabel("��������:");
        JTextField examNameTF = new JTextField(16);
        //��Ŀ����
        Box questNumBox = Box.createHorizontalBox();
        JLabel questNumLabel = new JLabel("��Ҫ�������Ŀ����:");
        JTextField questNumTF = new JTextField(16);
        questNumTF.setDocument(new NumberLongLimit(10));
        //ʱ������
        Box timeLimitBox = Box.createHorizontalBox();
        JLabel timeLimitLabel = new JLabel("ʱ�����ƣ����ӣ�:");
        JTextField timeLimitTF = new JTextField(16);
        timeLimitTF.setDocument(new NumberLongLimit(10));

        //��ť
        Box btnBox = Box.createHorizontalBox();
        AfButton submitBtn = new AfButton("�½�");
        AfButton cancelBtn = new AfButton("ȡ��");

        //���ý����߼�����������
        //�ύ��ť
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //��ȡ����
                String examName = examNameTF.getText();
                String questNum = questNumTF.getText();
                String timeLimit = timeLimitTF.getText();
                //�ж������Ƿ�Ϊ��
                if(examName.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"�������Ʋ���Ϊ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
                }else if (questNum.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"��Ŀ��������Ϊ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
                }else if (timeLimit.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"ʱ�����Ʋ���Ϊ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
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
        //ȡ����ť
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

        //��װ��ͼ
        //��װ�����
        examNameBox.add(examNameLabel);
        examNameBox.add(Box.createHorizontalStrut(20));
        examNameBox.add(examNameTF);

        questNumBox.add(questNumLabel);
        questNumBox.add(Box.createHorizontalStrut(20));
        questNumBox.add(questNumTF);

        timeLimitBox.add(timeLimitLabel);
        timeLimitBox.add(Box.createHorizontalStrut(20));
        timeLimitBox.add(timeLimitTF);

        //��װ��ť��
        btnBox.add(submitBtn);
        btnBox.add(Box.createHorizontalStrut(60));
        btnBox.add(cancelBtn);

        //��װ����
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
        //��ʾ
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

}
