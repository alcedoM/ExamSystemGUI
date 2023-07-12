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

//�������¼��Ľ��棬���������б�Ĳ���
public class InnerInterface {

    JFrame jf = new JFrame("����ϵͳ");

    //��ȡ��Ļ�ߴ�
    int screenWidth = ScreenUtils.getScreenWidth();
    int screenHeight = ScreenUtils.getScreenHeight();
    final int WIDTH = screenWidth/2;
    final int HEIGHT = screenHeight/2;

    //��ʼ��ͼ�ν���
    public void init(User currentUser) throws IOException {
        init((screenWidth-WIDTH)/2,(screenHeight-HEIGHT)/2,currentUser);
    }
    public void init(int x, int y,User currentUser) throws IOException {
        //���ô��ھ���
        jf.setBounds(x,y,WIDTH,HEIGHT);

        //����ͼ��
        ImageIcon icon = new ImageIcon("data/icon.png");
        jf.setIconImage(icon.getImage());

        //�ļ�ѡ����
        JFileChooser chooser = new JFileChooser(".");

        //��ȡ����
        List<Exam> examList = new ExamService().getExamList();

        JPanel mainPanel = new JPanel(new HLayout());

        //�������
        //�����
        JPanel leftBar = new JPanel();
        leftBar.setBackground(Color.white);

        JLabel uLable = new JLabel("��ӭ��"+currentUser.getId());
        AfButton logoutBtn = new AfButton("�ǳ�");
        AfButton newExamBtn = new AfButton("�½�����");
        AfButton inportExamBtn = new AfButton("���뿼��");

        leftBar.add(uLable);
        leftBar.add(logoutBtn);

        //����Աר��
        if(currentUser.getIs_admin()==1){
            leftBar.add(newExamBtn);
            leftBar.add(inportExamBtn);
        }

        //����ѡ����
        JPanel examListPanel = new JPanel(new FreeLayout());
        JPanel innerExamListPanel = new JPanel();
        innerExamListPanel.setLayout(new GridLayout(max((examList.size()/4)+1,4),4,10,10));

        //��ȡ���Բ����ɿ��Կ�Ƭ
        if (!examList.isEmpty()){
            for (Exam exam : examList) {
                //���������չʾ
                if(exam.isPublished() || currentUser.getIs_admin()==1){
                    innerExamListPanel.add(new ExamBriefCard(jf,exam,currentUser,examList));
                }
            }
        }else {
            innerExamListPanel.add(new JLabel("��û�п���Ŷ��"));
        }

        //�����߼�
        //�ǳ��߼�
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //���ص�¼����
                    new MainInterface().init(jf.getX(), jf.getY());
                    jf.dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //�½�����
        newExamBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //��ת���½�����
                    new NewExamInfo().init(jf.getX(), jf.getY(),currentUser,examList);
                    jf.dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //���뿼��
        inportExamBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //�����ļ�ѡ����
                chooser.setFileFilter(new FileNameExtensionFilter("txt","txt"));
                chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());

                //��ʾ�ļ�ѡ����
                int state = chooser.showOpenDialog(jf);
                File examName = chooser.getSelectedFile();

                if(state==JFileChooser.APPROVE_OPTION){
                    String data;
                    try {
                        //��ȡ�ļ�
                        data = Files.readString(examName.toPath());
                        Exam newExam = JSON.parseObject(data, Exam.class);
                        newExam.setPublished(false);
                        for (Exam exam : examList) {
                            if(exam.getExamName().equals(newExam.getExamName())){
                                JOptionPane.showMessageDialog(jf,"�����������������������","error",JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        //��������������ʱ����
                        examList.add(newExam);
                        new ExamService().saveExam(examList);
                        new ExamService().refreshJF(jf,currentUser, jf.getX(), jf.getY());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(jf,"����ʧ��","error",JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        //��װ��ͼ
        examListPanel.add(innerExamListPanel, new Margin(10,10,10,10));
        mainPanel.add(leftBar,"20%");
        mainPanel.add(examListPanel,"80%");

        jf.add(mainPanel);
        //��ʾ
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }


    //�������
//    public static void main(String[] args) throws IOException {
//        new InnerInterface().init(new User("δ��¼","null",1));
//    }

}
