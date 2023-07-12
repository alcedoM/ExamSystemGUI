package com.examsys.components;

import af.swing.layout.FreeLayout;
import af.swing.layout.Margin;
import com.examsys.modules.Quest;
import com.examsys.ui.ExamInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

//题目卡片
public class QuestCard extends JPanel {
    public QuestCard(Quest quest, ExamInterface examInterface,int questindex) {
        setLayout(new FreeLayout());

        Box mainbox = Box.createVerticalBox();

        String questTip = questindex +"."+quest.getQuestStem();
        if(quest.getQuestType()==Quest.MULTI_CHOICE){
            questTip = questTip +"(多选)";
        }else if(quest.getQuestType()==Quest.JUDEG_TF){
            questTip = questTip +"(判断)";
        }else if(quest.getQuestType()==Quest.Fill_BLANK){
            questTip = questTip +"(填空)";
        }

        JLabel questStem = new JLabel(questTip);
        String[] choice = quest.getChoice();

        //设置题干
        mainbox.add(Box.createVerticalStrut(10));
        mainbox.add(questStem);

        //根据不同题型设置选项
        if (quest.getQuestType()==Quest.CHOICE){
            ButtonGroup group = new ButtonGroup();
            JRadioButton a = new JRadioButton(choice[0]);
            JRadioButton b = new JRadioButton(choice[1]);
            JRadioButton c = new JRadioButton(choice[2]);
            JRadioButton d = new JRadioButton(choice[3]);
            group.add(a);
            group.add(b);
            group.add(c);
            group.add(d);

            mainbox.add(a);
            mainbox.add(b);
            mainbox.add(c);
            mainbox.add(d);

            ActionListener getRadio = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (a.isSelected()){
                        quest.setCurrentAns("a");
                    }else if(b.isSelected()){
                        quest.setCurrentAns("b");
                    }else if(c.isSelected()){
                        quest.setCurrentAns("c");
                    }else if(d.isSelected()){
                        quest.setCurrentAns("d");
                    }

                }
            };
            a.addActionListener(getRadio);
            b.addActionListener(getRadio);
            c.addActionListener(getRadio);
            d.addActionListener(getRadio);
        }else if(quest.getQuestType()==Quest.MULTI_CHOICE){
            JCheckBox a = new JCheckBox(choice[0]);
            JCheckBox b = new JCheckBox(choice[1]);
            JCheckBox c = new JCheckBox(choice[2]);
            JCheckBox d = new JCheckBox(choice[3]);
            mainbox.add(a);
            mainbox.add(b);
            mainbox.add(c);
            mainbox.add(d);

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
                    quest.setCurrentAns(Arrays.toString(ans));

                }
            };
            a.addActionListener(getMulti);
            b.addActionListener(getMulti);
            c.addActionListener(getMulti);
            d.addActionListener(getMulti);
        }else if(quest.getQuestType()==Quest.JUDEG_TF){
            ButtonGroup group = new ButtonGroup();
            JRadioButton a = new JRadioButton("正确");
            JRadioButton b = new JRadioButton("错误");
            group.add(a);
            group.add(b);

            mainbox.add(a);
            mainbox.add(b);

            ActionListener getRadio = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (a.isSelected()){
                        quest.setCurrentAns("a");
                    }else if(b.isSelected()){
                        quest.setCurrentAns("b");
                    }

                }
            };
            a.addActionListener(getRadio);
            b.addActionListener(getRadio);
        }else if(quest.getQuestType()==Quest.Fill_BLANK){
            JTextField fill = new JTextField();
            mainbox.add(fill);
            fill.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    quest.setCurrentAns(fill.getText());
                }
            });
        }

        add(mainbox, new Margin(10,10,10,10));
    }
}
