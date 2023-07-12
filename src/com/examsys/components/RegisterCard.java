package com.examsys.components;

import af.swing.AfButton;
import com.examsys.servises.RegisterServise;
import com.examsys.util.LongLimit;
import com.examsys.util.NumberLongLimit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//ע�ῨƬ���
public class RegisterCard extends JPanel {
    public RegisterCard(JFrame jf,int HEIGHT,JPanel mainPanel,CardLayout cardLayout){
        //�������
        Box vBox = Box.createVerticalBox();

        //ѧ��
        Box stdidBox = Box.createHorizontalBox();
        JLabel stdidLabel = new JLabel("ѧ        ��:");
        JTextField stdidTF = new JTextField(16);
        stdidTF.setDocument(new NumberLongLimit(13));
        //����
        Box pwdBox = Box.createHorizontalBox();
        JLabel pwdLabel = new JLabel("��        ��:");
        JPasswordField pwdTF = new JPasswordField(16);
        pwdTF.setDocument(new LongLimit(16));
        //�ظ�����
        Box rePwdBox = Box.createHorizontalBox();
        JLabel rePwdLabel = new JLabel("�ظ�����:");
        JPasswordField rePwdTF = new JPasswordField(16);
        pwdTF.setDocument(new LongLimit(16));
        //��ť
        Box btnBox = Box.createHorizontalBox();
        AfButton registBtn = new AfButton("ע��");
        AfButton toLoginBtn = new AfButton("���ص�¼");

        //��������
        //ע�ᰴť
        registBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //��ȡ����
                String studid = stdidTF.getText().trim();
                String password =  new String(pwdTF.getPassword());
                String rePassword = new String(rePwdTF.getPassword());

                //�ж������Ƿ�Ϊ��
                if(studid.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"ѧ�Ų���Ϊ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
                }else if(studid.length()!=13){
                    JOptionPane.showMessageDialog(jf,"��������ȷ��13λѧ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
                }else if (password.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"���벻��Ϊ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
                }else if (rePassword.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"�ظ���������","��ʾ",JOptionPane.INFORMATION_MESSAGE);
                }else if (! password.equals(rePassword)){
                    JOptionPane.showMessageDialog(jf,"������������벻һ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
                }else{
                    //���õ�¼ע����
                    try {
                        String[] res = RegisterServise.register(studid,password);
                        if(res[0].equals("1")){
                            //ע��ɹ�
                            JOptionPane.showMessageDialog(jf,"ע��ɹ����ص�¼","��ʾ",JOptionPane.INFORMATION_MESSAGE);
                            cardLayout.show(mainPanel,"login");
                            stdidTF.setText("");
                            pwdTF.setText("");
                            rePwdTF.setText("");
                        }else {
                            //ע��ʧ��
                            JOptionPane.showMessageDialog(jf,res[1],"��ʾ",JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        //���ص�¼��ť
        toLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel,"login");
                stdidTF.setText("");
                pwdTF.setText("");
                rePwdTF.setText("");
            }
        });

        //��װ��ͼ
        //��װѧ����
        stdidBox.add(stdidLabel);
        stdidBox.add(Box.createHorizontalStrut(20));
        stdidBox.add(stdidTF);
        //��װ������
        pwdBox.add(pwdLabel);
        pwdBox.add(Box.createHorizontalStrut(20));
        pwdBox.add(pwdTF);
        //��װ�ظ�������
        rePwdBox.add(rePwdLabel);
        rePwdBox.add(Box.createHorizontalStrut(20));
        rePwdBox.add(rePwdTF);
        //��װ��ť��
        btnBox.add(registBtn);
        btnBox.add(Box.createHorizontalStrut(60));
        btnBox.add(toLoginBtn);

        //��װ����
        vBox.add(Box.createVerticalStrut(HEIGHT/4));
        vBox.add(stdidBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pwdBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(rePwdBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);

        add(vBox);
    }
}
