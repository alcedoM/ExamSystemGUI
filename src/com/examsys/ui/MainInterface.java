package com.examsys.ui;

import com.examsys.components.LoginCard;
import com.examsys.components.RegisterCard;
import com.examsys.util.ScreenUtils;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

//����ڣ��������¼ע��Ĳ���
public class MainInterface {

    JFrame jf = new JFrame("����ϵͳ");

    //��ȡ��Ļ�ߴ�
    int screenWidth = ScreenUtils.getScreenWidth();
    int screenHeight = ScreenUtils.getScreenHeight();
    final int WIDTH = screenWidth/2;
    final int HEIGHT = screenHeight/2;


    //��ʼ��ͼ�ν���
    public void init() throws IOException {
        init((screenWidth-WIDTH)/2,(screenHeight-HEIGHT)/2);
    }
    public void init(int x, int y) throws IOException {
        //���ô��ھ���
        jf.setBounds(x,y,WIDTH,HEIGHT);

        //����ͼ��
        ImageIcon icon = new ImageIcon("data/icon.png");
        jf.setIconImage(icon.getImage());

        //���ÿ�Ƭ����
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(cardLayout);

        //�����¼/ע�ῨƬ
        mainPanel.add(new LoginCard(jf,HEIGHT,mainPanel,cardLayout),"login");
        mainPanel.add(new RegisterCard(jf,HEIGHT,mainPanel,cardLayout),"register");

        jf.add(mainPanel);

        //��ʾ
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);

    }

    //���������
    public static void main(String[] args) throws IOException {
        new MainInterface().init();
    }


}
