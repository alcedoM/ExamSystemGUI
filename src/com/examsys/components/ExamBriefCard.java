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

//InnerInterface����ʾ�Ŀ��Կ�Ƭ�������򵥵���Ϣ
public class ExamBriefCard extends JPanel {
    public ExamBriefCard(JFrame jf, Exam exam, User currentUser, List<Exam> examList){
        //��ȡ��������
        String examName = exam.getExamName();
        int questNum = exam.getQuestGroup().size();
        int needAnsNum  = exam.getQuestNumNeedAns();
        int timeLimit = exam.getTimeLimit();

        //�������
        Box box = Box.createVerticalBox();
        JLabel nameLabel = new JLabel(examName);
        JLabel needAnsLabel = new JLabel("��Ŀ������"+String.valueOf(needAnsNum));
        JLabel questLabel = new JLabel("���������"+String.valueOf(questNum));
        JLabel timeLabel = new JLabel("ʱ�����ƣ�"+String.valueOf(timeLimit)+"����");
        AfButton enter = new AfButton("����");
        Box btnBox = Box.createHorizontalBox();
        AfButton edit = new AfButton("�༭");
        MyButton publish = new MyButton("����");
        MyButton deleteExam = new MyButton("ɾ��");
        MyButton exportExam = new MyButton("����");

        //�����ļ�ѡ����
        JFileChooser chooser = new JFileChooser(".");

        //��������
        //�༭��ť
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
        //���뿼�԰�ť
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
        //����ȡ��������ť
        publish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("����")){
                    exam.setPublished(true);
                    publish.setText("ȡ������");
                    try {
                        new ExamService().savePublish(examList,exam,true);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }else {
                    exam.setPublished(false);
                    publish.setText("����");
                    try {
                        new ExamService().savePublish(examList,exam,false);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        //ɾ������
        deleteExam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jf,"�Ƿ�ɾ�����ԣ�"+exam.getExamName(),"��ʾ",JOptionPane.YES_NO_OPTION);
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

        //��������
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

        //��װ
        box.add(nameLabel);
        box.add(needAnsLabel);
        if (currentUser.getIs_admin()==1){
            box.add(questLabel);
        }
        box.add(timeLabel);

        if(currentUser.getIs_admin()==1){
            btnBox.add(edit);
            if(exam.isPublished()){
                publish.setText("ȡ������");
            }else{
                publish.setText("����");
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
