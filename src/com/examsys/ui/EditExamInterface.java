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


//������༭���ԵĲ���
public class EditExamInterface {

    JFrame jf = new JFrame("����ϵͳ");
    private static User currentUser = new User("δ��¼","null",1);
    List<int[]> answer = new ArrayList<>();

    int screenWidth = ScreenUtils.getScreenWidth();
    int screenHeight = ScreenUtils.getScreenHeight();
    final int WIDTH = screenWidth/2;
    final int HEIGHT = screenHeight/2;


    //��ʼ��ͼ�ν���
    public void init(User currentUser,Exam exam,List<Exam> examList) throws IOException {
        init((screenWidth-WIDTH)/2,(screenHeight-HEIGHT)/2,currentUser,exam,examList);
    }
    public void init(int x, int y, User currentUser, Exam exam,List<Exam> examList) throws IOException {
        List<Quest> quests = exam.getQuestGroup();

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
        AfButton saveBtn = new AfButton("����");
        AfButton quitBtn = new AfButton("����");
        AfButton newBtn = new AfButton("�½���Ŀ");
        leftBarBtn.add(saveBtn);
        leftBarBtn.add(quitBtn);

        //��������
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLable = new JLabel("�������ƣ�");
        JTextField nameTF = new JTextField(String.valueOf(exam.getExamName()));
        nameBox.add(nameLable);
        nameBox.add(nameTF);

        //����ʱ��
        Box timeBox = Box.createHorizontalBox();
        JLabel timeLable = new JLabel("����ʱ��(����)��");
        JTextField timeTF = new JTextField(String.valueOf(exam.getTimeLimit()));
        timeBox.add(timeLable);
        timeBox.add(timeTF);

        //��Ҫ���������
        Box needAnsBox = Box.createHorizontalBox();
        JLabel needAnsLable = new JLabel("����������");
        JTextField needAnsTF = new JTextField(String.valueOf(exam.getQuestNumNeedAns()));
        needAnsBox.add(needAnsLable);
        needAnsBox.add(needAnsTF);

        //��װ�����
        leftBar.add(uLable,"10%");
        leftBar.add(nameBox,"10%");
        leftBar.add(timeBox,"10%");
        leftBar.add(needAnsBox,"10%");

        JPanel toQuest = new JPanel();
        if(quests.size()!=0){
            //��Ŀ��ת
            toQuest.setLayout(new GridLayout((quests.size() / 5) + 1, 5));
            toQuest.setBackground(Color.white);
            leftBar.add(toQuest,"auto");
        }

        leftBar.add(newBtn,"10%");
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
        if(quests.size()!=0){
            for (int i = 0; i < quests.size(); i++) {
                questPanel.add(new EditQuestCard(jf,quests.get(i),i+1,exam,examList,currentUser),"quest"+i+1);
            }
        }else {
            questPanel.add(new JLabel("��û����Ŀ"));
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
                for (int i = 0; i < quests.size(); i++) {
                    if(e.getActionCommand().equals(String.valueOf(i+1))){
                        cardLayout.show(questPanel,"quest"+i+1);
                    }
                }
            }
        };
        //����������ת��ť������һ����������
        if(quests.size()!=0){
            for (int i = 0; i < quests.size(); i++) {
                MyButton btn = new MyButton(String.valueOf(i+1));
                btn.addActionListener(toTargetQuest);
                toQuest.add(btn);
            }
        }

        //�½���Ŀ
        newBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object result = JOptionPane.showInputDialog(jf,"","ѡ����Ŀ����",JOptionPane.DEFAULT_OPTION,null,new String[]{"��ѡ","��ѡ","�ж�","���"},"��ѡ");
                String[] blankChoice = {"","","",""};
                if(result==null){
                }else if(result.toString().equals("��ѡ")){
                    Quest newQuest = new Quest(exam.getQuestIdPoint(),Quest.CHOICE,"",blankChoice,"");
                    quests.add(newQuest);
                    exam.setQuestIdPoint(exam.getQuestIdPoint()+1);//idָ������
                }else if(result.toString().equals("��ѡ")){
                    Quest newQuest = new Quest(exam.getQuestIdPoint(),Quest.MULTI_CHOICE,"",blankChoice,"");
                    quests.add(newQuest);
                    exam.setQuestIdPoint(exam.getQuestIdPoint()+1);//idָ������
                }else if(result.toString().equals("�ж�")){
                    Quest newQuest = new Quest(exam.getQuestIdPoint(),Quest.JUDEG_TF,"",blankChoice,"");
                    quests.add(newQuest);
                    exam.setQuestIdPoint(exam.getQuestIdPoint()+1);//idָ������
                }else if(result.toString().equals("���")){
                    Quest newQuest = new Quest(exam.getQuestIdPoint(),Quest.Fill_BLANK,"",blankChoice,"");
                    quests.add(newQuest);
                    exam.setQuestIdPoint(exam.getQuestIdPoint()+1);//idָ������
                }

                try {
                    new ExamService().saveExam(examList);
                    new EditExamInterface().init(jf.getX(), jf.getY(), currentUser,exam,examList);
                    jf.dispose();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(jf,"error","����",JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });

        //�����Ծ�
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                exam.setExamName(nameTF.getText());
                exam.setQuestNumNeedAns(Integer.parseInt(needAnsTF.getText()));
                //��ȡʱ������
                if(isNumeric(timeTF.getText())){
                    int timeLimit = Integer.parseInt(timeTF.getText());
                    exam.setTimeLimit(timeLimit);
                }else {
                    JOptionPane.showMessageDialog(jf,"���ÿ���ʱ����������������","��ʾ",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //�ж�����Ƿ����Ҫ��
                if(quests.size()<exam.getQuestNumNeedAns()){
                    JOptionPane.showMessageDialog(jf,"��Ŀ��������Ҫ������","��ʾ",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // �洢�༭���
                //��ת����ҳ��
                int result = JOptionPane.showConfirmDialog(jf,"�Ƿ񱣴�","��ʾ",JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    try {
                        exam.setQuestGroup(quests);
                        new ExamService().saveExam(examList);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    int returnRes = JOptionPane.showConfirmDialog(jf,"����ɹ��Ƿ񷵻�������","����",JOptionPane.YES_NO_OPTION);
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
        //�˳���ť
        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jf,"�Ƿ񷵻������棬����ʧȥδ���������","��ʾ",JOptionPane.YES_NO_OPTION);
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
//        List<Exam> examList = new ExamService().getExamList();
//        new EditExamInterface().init(currentUser,examList.get(0),examList);
//    }

    //�ж��Ƿ�Ϊ���ֺ���
    private boolean isNumeric(String var1) {
        try {
            Long.valueOf(var1);
            return true;
        } catch (NumberFormatException var3) {
            return false;
        }
    }

}
