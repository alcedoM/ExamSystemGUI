package com.examsys.ui;

import com.examsys.components.LoginCard;
import com.examsys.components.RegisterCard;
import com.examsys.util.ScreenUtils;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

//主入口，负责处理登录注册的部分
public class MainInterface {

    JFrame jf = new JFrame("考试系统");

    //获取屏幕尺寸
    int screenWidth = ScreenUtils.getScreenWidth();
    int screenHeight = ScreenUtils.getScreenHeight();
    final int WIDTH = screenWidth/2;
    final int HEIGHT = screenHeight/2;


    //初始化图形界面
    public void init() throws IOException {
        init((screenWidth-WIDTH)/2,(screenHeight-HEIGHT)/2);
    }
    public void init(int x, int y) throws IOException {
        //设置窗口居中
        jf.setBounds(x,y,WIDTH,HEIGHT);

        //设置图标
        ImageIcon icon = new ImageIcon("data/icon.png");
        jf.setIconImage(icon.getImage());

        //设置卡片布局
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(cardLayout);

        //插入登录/注册卡片
        mainPanel.add(new LoginCard(jf,HEIGHT,mainPanel,cardLayout),"login");
        mainPanel.add(new RegisterCard(jf,HEIGHT,mainPanel,cardLayout),"register");

        jf.add(mainPanel);

        //显示
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);

    }

    //程序主入口
    public static void main(String[] args) throws IOException {
        new MainInterface().init();
    }


}
