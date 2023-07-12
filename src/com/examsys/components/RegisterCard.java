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

//注册卡片组件
public class RegisterCard extends JPanel {
    public RegisterCard(JFrame jf,int HEIGHT,JPanel mainPanel,CardLayout cardLayout){
        //声明组件
        Box vBox = Box.createVerticalBox();

        //学号
        Box stdidBox = Box.createHorizontalBox();
        JLabel stdidLabel = new JLabel("学        号:");
        JTextField stdidTF = new JTextField(16);
        stdidTF.setDocument(new NumberLongLimit(13));
        //密码
        Box pwdBox = Box.createHorizontalBox();
        JLabel pwdLabel = new JLabel("密        码:");
        JPasswordField pwdTF = new JPasswordField(16);
        pwdTF.setDocument(new LongLimit(16));
        //重复密码
        Box rePwdBox = Box.createHorizontalBox();
        JLabel rePwdLabel = new JLabel("重复密码:");
        JPasswordField rePwdTF = new JPasswordField(16);
        pwdTF.setDocument(new LongLimit(16));
        //按钮
        Box btnBox = Box.createHorizontalBox();
        AfButton registBtn = new AfButton("注册");
        AfButton toLoginBtn = new AfButton("返回登录");

        //监听函数
        //注册按钮
        registBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取输入
                String studid = stdidTF.getText().trim();
                String password =  new String(pwdTF.getPassword());
                String rePassword = new String(rePwdTF.getPassword());

                //判断输入是否为空
                if(studid.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"学号不能为空","提示",JOptionPane.INFORMATION_MESSAGE);
                }else if(studid.length()!=13){
                    JOptionPane.showMessageDialog(jf,"请输入正确的13位学号","提示",JOptionPane.INFORMATION_MESSAGE);
                }else if (password.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"密码不能为空","提示",JOptionPane.INFORMATION_MESSAGE);
                }else if (rePassword.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"重复您的密码","提示",JOptionPane.INFORMATION_MESSAGE);
                }else if (! password.equals(rePassword)){
                    JOptionPane.showMessageDialog(jf,"两次输入的密码不一致","提示",JOptionPane.INFORMATION_MESSAGE);
                }else{
                    //调用登录注册类
                    try {
                        String[] res = RegisterServise.register(studid,password);
                        if(res[0].equals("1")){
                            //注册成功
                            JOptionPane.showMessageDialog(jf,"注册成功返回登录","提示",JOptionPane.INFORMATION_MESSAGE);
                            cardLayout.show(mainPanel,"login");
                            stdidTF.setText("");
                            pwdTF.setText("");
                            rePwdTF.setText("");
                        }else {
                            //注册失败
                            JOptionPane.showMessageDialog(jf,res[1],"提示",JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        //返回登录按钮
        toLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel,"login");
                stdidTF.setText("");
                pwdTF.setText("");
                rePwdTF.setText("");
            }
        });

        //组装视图
        //组装学号栏
        stdidBox.add(stdidLabel);
        stdidBox.add(Box.createHorizontalStrut(20));
        stdidBox.add(stdidTF);
        //组装密码栏
        pwdBox.add(pwdLabel);
        pwdBox.add(Box.createHorizontalStrut(20));
        pwdBox.add(pwdTF);
        //组装重复密码栏
        rePwdBox.add(rePwdLabel);
        rePwdBox.add(Box.createHorizontalStrut(20));
        rePwdBox.add(rePwdTF);
        //组装按钮栏
        btnBox.add(registBtn);
        btnBox.add(Box.createHorizontalStrut(60));
        btnBox.add(toLoginBtn);

        //组装整体
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
