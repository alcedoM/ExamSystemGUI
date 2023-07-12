package com.examsys.components;

import af.swing.layout.FreeLayout;
import af.swing.layout.Margin;
import com.examsys.modules.Exam;
import com.examsys.modules.Quest;
import com.examsys.modules.User;
import com.examsys.servises.ExamService;
import com.examsys.ui.EditExamInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//编辑题目卡片
public class EditQuestCard extends JPanel {
    public EditQuestCard(JFrame jf, Quest quest, int questindex, Exam exam,List<Exam> examList, User currentUser) {

        setLayout(new FreeLayout());

        Box mainbox = Box.createVerticalBox();
        Box choiceBox = Box.createHorizontalBox();
        String[] choice = quest.getChoice();
        String questTip = "第"+questindex+"题";

        if (quest.getQuestType()==Quest.MULTI_CHOICE){
            questTip = questTip +"(多选)";
        }else if(quest.getQuestType()==Quest.JUDEG_TF){
            questTip = questTip +"(判断)";
        }else if(quest.getQuestType()==Quest.Fill_BLANK){
            questTip = questTip +"(填空)";
        }

        JLabel questIndexLabel = new JLabel(questTip);
        MyButton delete = new MyButton("删除");

        Box stemBox = Box.createHorizontalBox();
        Box aBox = Box.createHorizontalBox();
        Box bBox = Box.createHorizontalBox();
        Box cBox = Box.createHorizontalBox();
        Box dBox = Box.createHorizontalBox();

        JTextField questStemTF = new JTextField(quest.getQuestStem());
        stemBox.add(new JLabel("题干："));
        stemBox.add(questStemTF);

        //设置选项
        JTextField aTF = new JTextField(choice[0]);
        JTextField bTF = new JTextField(choice[1]);
        JTextField cTF = new JTextField(choice[2]);
        JTextField dTF = new JTextField(choice[3]);
        aBox.add(new JLabel("A.    "));
        aBox.add(aTF);
        bBox.add(new JLabel("B.    "));
        bBox.add(bTF);
        cBox.add(new JLabel("C.    "));
        cBox.add(cTF);
        dBox.add(new JLabel("D.    "));
        dBox.add(dTF);


        //选择正确答案
        if(quest.getQuestType()==Quest.CHOICE){
            ButtonGroup group = new ButtonGroup();
            JLabel correctLabel = new JLabel("选择正确答案:");
            JRadioButton a = new JRadioButton("A");
            JRadioButton b = new JRadioButton("B");
            JRadioButton c = new JRadioButton("C");
            JRadioButton d = new JRadioButton("D");
            group.add(a);
            group.add(b);
            group.add(c);
            group.add(d);
            switch (quest.getCorrectAns()) {
                case "a" -> a.setSelected(true);
                case "b" -> b.setSelected(true);
                case "c" -> c.setSelected(true);
                case "d" -> d.setSelected(true);
                default -> {
                }
            }
            choiceBox.add(correctLabel);
            choiceBox.add(a);
            choiceBox.add(b);
            choiceBox.add(c);
            choiceBox.add(d);

            //选择正确答案
            ActionListener getRadio = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (a.isSelected()){
                        quest.setCorrectAns("a");
                    }else if(b.isSelected()){
                        quest.setCorrectAns("b");
                    }else if(c.isSelected()){
                        quest.setCorrectAns("c");
                    }else if(d.isSelected()){
                        quest.setCorrectAns("d");
                    }

                }
            };
            a.addActionListener(getRadio);
            b.addActionListener(getRadio);
            c.addActionListener(getRadio);
            d.addActionListener(getRadio);
        }else if(quest.getQuestType()==Quest.MULTI_CHOICE){
            JLabel correctLabel = new JLabel("选择正确答案:");
            JCheckBox a = new JCheckBox("A");
            JCheckBox b = new JCheckBox("B");
            JCheckBox c = new JCheckBox("C");
            JCheckBox d = new JCheckBox("D");

            if(!quest.getCorrectAns().isEmpty()){
                String[] strArr = quest.getCorrectAns().substring(1, quest.getCorrectAns().length() - 1).split(", ");
                int[] corAns = new int[strArr.length];
                for (int i = 0; i < strArr.length; i++) {
                    corAns[i] = Integer.parseInt(strArr[i]);
                }
                if (corAns[0]==1){
                    a.setSelected(true);
                }
                if (corAns[1]==1){
                    b.setSelected(true);
                }
                if (corAns[2]==1){
                    c.setSelected(true);
                }
                if (corAns[3]==1){
                    d.setSelected(true);
                }
            }

            choiceBox.add(correctLabel);
            choiceBox.add(a);
            choiceBox.add(b);
            choiceBox.add(c);
            choiceBox.add(d);

            //选择正确答案
            ActionListener getMulti = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] ans = {0,0,0,0};
                    if (a.isSelected()){
                        ans[0] = 1;
                    }
                    if(b.isSelected()){
                        ans[1] = 1;
                    }
                    if(c.isSelected()){
                        ans[2] = 1;
                    }
                    if(d.isSelected()){
                        ans[3] = 1;
                    }
                    quest.setCorrectAns(Arrays.toString(ans));
                }
            };
            a.addActionListener(getMulti);
            b.addActionListener(getMulti);
            c.addActionListener(getMulti);
            d.addActionListener(getMulti);
        }else if(quest.getQuestType()== Quest.JUDEG_TF){
            ButtonGroup group = new ButtonGroup();
            JLabel correctLabel = new JLabel("选择正确答案:");
            JRadioButton a = new JRadioButton("正确");
            JRadioButton b = new JRadioButton("错误");
            group.add(a);
            group.add(b);
            switch (quest.getCorrectAns()) {
                case "a" -> a.setSelected(true);
                case "b" -> b.setSelected(true);
                default -> {}
            }
            choiceBox.add(correctLabel);
            choiceBox.add(a);
            choiceBox.add(b);

            //选择正确答案
            ActionListener getRadio = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (a.isSelected()){
                        quest.setCorrectAns("a");
                    }else if(b.isSelected()){
                        quest.setCorrectAns("b");
                    }

                }
            };
            a.addActionListener(getRadio);
            b.addActionListener(getRadio);
        }else if(quest.getQuestType()== Quest.Fill_BLANK){
            JTextField fill = new JTextField(quest.getCorrectAns());
            JLabel correctLabel = new JLabel("填写正确答案:");
            choiceBox.add(correctLabel);
            choiceBox.add(fill);

            fill.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    quest.setCorrectAns(fill.getText());
                }
            });
        }

        //总装视图
        mainbox.add(Box.createVerticalStrut(10));
        mainbox.add(questIndexLabel);
        mainbox.add(stemBox);
        if(quest.getQuestType()==Quest.MULTI_CHOICE || quest.getQuestType()==Quest.CHOICE ){
            mainbox.add(aBox);
            mainbox.add(bBox);
            mainbox.add(cBox);
            mainbox.add(dBox);
        }
        mainbox.add(choiceBox);
        mainbox.add(delete);

        //题干监听
        questStemTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                quest.setQuestStem(questStemTF.getText());
            }
        });
        //选项监听
        aTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                choice[0] = aTF.getText();
                quest.setChoice(choice);
            }
        });
        bTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                choice[1] = bTF.getText();
                quest.setChoice(choice);
            }
        });
        cTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                choice[2] = cTF.getText();
                quest.setChoice(choice);
            }
        });
        dTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                choice[3] = dTF.getText();
                quest.setChoice(choice);
            }
        });

        //删除题目按钮
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(jf,"是否删除该题","提示",JOptionPane.YES_NO_OPTION);
                if (result==JOptionPane.YES_OPTION){
                    exam.getQuestGroup().remove(quest);
                    try {
                        new ExamService().saveExam(examList);
                        new EditExamInterface().init(currentUser,exam,examList);
                        jf.dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        add(mainbox, new Margin(10,10,10,10));
    }
}
