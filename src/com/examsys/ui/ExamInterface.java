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

//�������Խ���Ĳ���
public class ExamInterface {

    private static User currentUser = new User("δ��¼","null",0);
    JFrame jf = new JFrame("����ϵͳ");
    List<int[]> answer = new ArrayList<>();

    int screenWidth = ScreenUtils.getScreenWidth();
    int screenHeight = ScreenUtils.getScreenHeight();
    final int WIDTH = screenWidth/2;
    final int HEIGHT = screenHeight/2;
    Boolean submitFlag = false;


    //��ʼ��ͼ�ν���
    public void init(User currentUser,Exam exam) throws IOException {
        init((screenWidth-WIDTH)/2,(screenHeight-HEIGHT)/2,currentUser,exam);
    }
    public void init(int x, int y, User currentUser, Exam exam) throws IOException {
        //��ȡ��Ŀ
        List<Quest> quests = exam.getQuestGroup();
        //�����ȡ��Ŀ
        Collections.shuffle(quests);
        List<Quest> randomQuests = quests.subList(0,exam.getQuestNumNeedAns());

        //���ô��ھ���
        jf.setBounds(x,y,WIDTH,HEIGHT);

        //����ͼ��
        ImageIcon icon = new ImageIcon("data/icon.png");
        jf.setIconImage(icon.getImage());

        //�������
        JPanel mainPanel = new JPanel(new HLayout());
        //�����
        JPanel leftBar = new JPanel(new VLayout());
        leftBar.setBackground(Color.white);

        Box leftBarBtn = Box.createHorizontalBox();

        JLabel uLable = new JLabel("�û���"+currentUser.getId());
        AfButton submitBtn = new AfButton("�ύ");
        AfButton quitBtn = new AfButton("����");
        leftBarBtn.add(submitBtn);
        leftBarBtn.add(quitBtn);

        //����ʱ��
        JLabel timeLable = new JLabel("����ʱ�䣺"+exam.getTimeLimit()+"����");
        //���̵߳���ʱ
        Thread timeCount = new Thread(() -> {
            int sec = 59;
            //��ȡ����ʱ��
            int min = exam.getTimeLimit() - 1;
            //��ʱ
            for (int i = 0; i < exam.getTimeLimit() * 60; i++) {
                try {
                    timeLable.setText("����ʱ�䣺" + min + ":" + sec);
                    if (min == 0 && sec == 0) {
                        JOptionPane.showMessageDialog(jf, "ʱ�䵽,�Զ��ύ����", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
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

        //��Ŀ��ת
        JPanel toQuest = new JPanel(new GridLayout((randomQuests.size()/5)+1,5));
        toQuest.setBackground(Color.white);

        leftBar.add(uLable,"10%");
        leftBar.add(timeLable,"10%");
        leftBar.add(toQuest,"auto");
        leftBar.add(leftBarBtn,"10%");


        //������
        CardLayout cardLayout = new CardLayout();
        JPanel examPanel = new JPanel(new BorderLayout());
        JPanel questPanel = new JPanel(cardLayout);
        //��ť��
        Box btnBox = Box.createHorizontalBox();
        AfButton next = new AfButton("��һ��");
        AfButton pre = new AfButton("��һ��");

        btnBox.add(Box.createHorizontalGlue());
        btnBox.add(Box.createHorizontalGlue());
        btnBox.add(pre);
        btnBox.add(next);

        //������Ŀ��Ƭ
        for (int i = 0; i < randomQuests.size(); i++) {
            questPanel.add(new QuestCard(randomQuests.get(i),this,i+1),"quest"+i+1);
        }

        //���ü�������
        //��һ��
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               cardLayout.next(questPanel);
            }
        });
        //��һ��
        pre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.previous(questPanel);
            }
        });
        //��ת�ض���Ŀ
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
        //����������ת��ť������һ����������
        for (int i = 0; i < exam.getQuestNumNeedAns(); i++) {
            MyButton btn = new MyButton(String.valueOf(i+1));
            btn.addActionListener(toTargetQuest);
            toQuest.add(btn);
        }

        //�ύ�Ծ�
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //��ȡ��ǰ�Ĵ������
                List<String> ans = getAnswer(randomQuests);

                //�洢�������
                //��ת����ҳ��
                if(!ans.contains(null)){
                    int result = JOptionPane.showConfirmDialog(jf,"�Ƿ񽻾�","��ʾ",JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION){
                        try {
                            timeCount.interrupt();
                            scoreAndReturn(exam,randomQuests,true,currentUser);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else{
                    int result = JOptionPane.showConfirmDialog(jf,"��������Ŀδ�����Ƿ񽻾�","��ʾ",JOptionPane.YES_NO_OPTION);
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

        //�Ƴ����԰�ť
        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jf,"�Ƿ��������","��ʾ",JOptionPane.YES_NO_OPTION);
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

        //��װ��ͼ
        examPanel.add(questPanel);
        examPanel.add(btnBox,BorderLayout.SOUTH);

        mainPanel.add(leftBar,"20%");
        mainPanel.add(examPanel,"80%");

        jf.add(mainPanel);
        //��ʾ
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    //�������
//    public static void main(String[] args) throws IOException {
//        new ExamInterface().init(currentUser,new ExamService().getExamList().get(0));
//    }

    //��ȡ�𰸺���
    public List<String> getAnswer(List<Quest> quests){
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < quests.size(); i++) {
            ans.add(quests.get(i).getCurrentAns());
        }
        return ans;
    }

    //��֣����沢���غ���
    public void scoreAndReturn(Exam exam,List<Quest> quests,boolean socreFlag,User currentUser) throws IOException {
        //�Զ��о�
        if(socreFlag){
            int correctNum = 0;
            for (Quest quest : quests) {
                if(quest.getCurrentAns()!=null && quest.getCurrentAns().equals(quest.getCorrectAns())){
                    correctNum++;
                }
            }
            //�����
            new ExamService().saveAns(exam.getExamName(),currentUser.getId(),quests,correctNum);
            JOptionPane.showMessageDialog(jf,"������ˣ�"+correctNum+"/"+quests.size(),"���",JOptionPane.INFORMATION_MESSAGE);
        }
        try {
            new InnerInterface().init(jf.getX(), jf.getY(), currentUser);
            jf.dispose();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
