package com.examsys.components;

import af.swing.AfButton;
import com.examsys.modules.User;
import com.examsys.servises.LoginService;
import com.examsys.ui.InnerInterface;
import com.examsys.util.LongLimit;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//登录卡片组件
public class LoginCard extends JPanel {
    public LoginCard(JFrame jf,int HEIGHT,JPanel mainPanel,CardLayout cardLayout){
        //声明组件
        Box vBox = Box.createVerticalBox();
        //学号
        Box stdidBox = Box.createHorizontalBox();
        JLabel stdidLabel = new JLabel("学         号:");
        JTextField stdidTF = new JTextField(16);
        stdidTF.setDocument(new LongLimit(13));
        //密码
        Box pwdBox = Box.createHorizontalBox();
        JLabel pwdLabel = new JLabel("密         码:");
        JPasswordField pwdTF = new JPasswordField(16);
        pwdTF.setDocument(new LongLimit(16));
        //按钮
        Box btnBox = Box.createHorizontalBox();
        AfButton loginBtn = new AfButton("登录");
        AfButton toRegistBtn = new AfButton("前往注册");

        //设置交互逻辑，监听函数
        //登录按钮
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取输入
                String studid = stdidTF.getText().trim();
                String password =  new String(pwdTF.getPassword());

                //判断输入是否为空
                if(studid.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"学号不能为空","提示",JOptionPane.INFORMATION_MESSAGE);
                }else if (password.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"密码不能为空","提示",JOptionPane.INFORMATION_MESSAGE);
                }else {
                    try {
                        //调用登录检查类
                        //返回字符串数组res 0:登录是否成成功;1:是否是管理员;2:返回消息
                        String[] res = LoginService.loginCheck(studid,password);
                        if(res[0].equals("1")){
                            //跳转主页面
                            InnerInterface innerInterface = new InnerInterface();
                            innerInterface.init(jf.getX(),jf.getY(),new User(studid,"null",Integer.parseInt(res[1])));
                            jf.dispose();
                        }else{
                            JOptionPane.showMessageDialog(jf,res[2],"登录失败",JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        //注册按钮
        toRegistBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel,"register");
                stdidTF.setText("");
                pwdTF.setText("");
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
        //组装按钮栏
        btnBox.add(loginBtn);
        btnBox.add(Box.createHorizontalStrut(60));
        btnBox.add(toRegistBtn);

        //组装整体
        vBox.add(Box.createVerticalStrut(HEIGHT/3));
        vBox.add(stdidBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pwdBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);

        add(vBox);
    }

}
