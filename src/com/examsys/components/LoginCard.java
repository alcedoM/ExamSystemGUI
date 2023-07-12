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

//��¼��Ƭ���
public class LoginCard extends JPanel {
    public LoginCard(JFrame jf,int HEIGHT,JPanel mainPanel,CardLayout cardLayout){
        //�������
        Box vBox = Box.createVerticalBox();
        //ѧ��
        Box stdidBox = Box.createHorizontalBox();
        JLabel stdidLabel = new JLabel("ѧ         ��:");
        JTextField stdidTF = new JTextField(16);
        stdidTF.setDocument(new LongLimit(13));
        //����
        Box pwdBox = Box.createHorizontalBox();
        JLabel pwdLabel = new JLabel("��         ��:");
        JPasswordField pwdTF = new JPasswordField(16);
        pwdTF.setDocument(new LongLimit(16));
        //��ť
        Box btnBox = Box.createHorizontalBox();
        AfButton loginBtn = new AfButton("��¼");
        AfButton toRegistBtn = new AfButton("ǰ��ע��");

        //���ý����߼�����������
        //��¼��ť
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //��ȡ����
                String studid = stdidTF.getText().trim();
                String password =  new String(pwdTF.getPassword());

                //�ж������Ƿ�Ϊ��
                if(studid.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"ѧ�Ų���Ϊ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
                }else if (password.isEmpty()){
                    JOptionPane.showMessageDialog(jf,"���벻��Ϊ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
                }else {
                    try {
                        //���õ�¼�����
                        //�����ַ�������res 0:��¼�Ƿ�ɳɹ�;1:�Ƿ��ǹ���Ա;2:������Ϣ
                        String[] res = LoginService.loginCheck(studid,password);
                        if(res[0].equals("1")){
                            //��ת��ҳ��
                            InnerInterface innerInterface = new InnerInterface();
                            innerInterface.init(jf.getX(),jf.getY(),new User(studid,"null",Integer.parseInt(res[1])));
                            jf.dispose();
                        }else{
                            JOptionPane.showMessageDialog(jf,res[2],"��¼ʧ��",JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        //ע�ᰴť
        toRegistBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel,"register");
                stdidTF.setText("");
                pwdTF.setText("");
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
        //��װ��ť��
        btnBox.add(loginBtn);
        btnBox.add(Box.createHorizontalStrut(60));
        btnBox.add(toRegistBtn);

        //��װ����
        vBox.add(Box.createVerticalStrut(HEIGHT/3));
        vBox.add(stdidBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pwdBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);

        add(vBox);
    }

}
