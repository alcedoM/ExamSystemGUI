/*
原为AfButton
改了一下，调用监听函数时，e.getActionCommand()返回按钮的label
返回同JButton
 */
package com.examsys.components;

import af.swing.style.AfBorderStyle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

public class MyButton extends JPanel {
    public com.examsys.components.MyButton.Style normal;
    public com.examsys.components.MyButton.Style hover;
    public int margin;
    public int radius;
    public int padding;
    private String text;
    private boolean isHover;
    private ActionListener clickListener;

    public MyButton(String text) {
        this.normal = new com.examsys.components.MyButton.Style();
        this.hover = new com.examsys.components.MyButton.Style();
        this.margin = 2;
        this.radius = 10;
        this.padding = 4;
        this.isHover = false;
        this.text = text;
        this.setOpaque(false);
        this.enableEvents(16L);
        this.normal.bgColor = new Color(16645629);
        this.normal.textColor = new Color(3158064);
        this.normal.border = new AfBorderStyle(new Color(12632256));
        this.normal.font = new Font("微软雅黑", 0, 14);
        this.hover.bgColor = new Color(16777215);
        this.hover.textColor = new Color(7372944);
        this.hover.border = new AfBorderStyle(new Color(7372944));
    }

    public MyButton() {
        this("");
    }

    public void setText(String text) {
        this.text = text;
        this.repaint();
    }

    public String getText() {
        return this.text;
    }

    public void addActionListener(ActionListener l) {
        this.clickListener = l;
    }

    public Dimension getPreferredSize() {
        FontMetrics fm = this.getFontMetrics(this.normal.font);
        int fontSize = fm.getHeight();
        int textWidth = fm.stringWidth(this.text);
        int w = textWidth + this.padding * 2 + this.margin * 2 + 4;
        int h = fontSize + this.padding * 2 + this.margin * 2 + 4;
        return new Dimension(w, h);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = this.getWidth();
        int h = this.getHeight();
        Rectangle rect = new Rectangle(w, h);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rect.grow(-this.margin, -this.margin);
        Color bgColor = this.normal.bgColor;
        AfBorderStyle border = this.normal.border;
        Font font = this.normal.font;
        Color textColor = this.normal.textColor;
        if (this.isHover) {
            bgColor = this.hover.bgColor;
            if (bgColor == null) {
                bgColor = this.normal.bgColor;
            }

            border = this.hover.border;
            if (border == null) {
                border = this.normal.border;
            }

            font = this.hover.font;
            if (font == null) {
                font = this.normal.font;
            }

            textColor = this.hover.textColor;
            if (textColor == null) {
                textColor = this.normal.textColor;
            }
        }

        rect.grow(-1, -1);
        Shape bg = new RoundRectangle2D.Double((double)rect.x, (double)rect.y, (double)rect.width, (double)rect.height, (double)this.radius, (double)this.radius);
        if (bgColor != null) {
            g2d.setPaint(bgColor);
            g2d.fill(bg);
        }

        if (border != null) {
            g2d.setStroke(new BasicStroke(1.2F));
            if (border.color != null) {
                g2d.setPaint(border.color);
            }

            g2d.draw(bg);
        }

        if (this.text != null && font != null) {
            Rectangle r = new Rectangle(rect);
            r.grow(-this.padding, -this.padding);
            FontMetrics fm = g2d.getFontMetrics(font);
            int fontSize = fm.getHeight();
            int textWidth = fm.stringWidth(this.text);
            int leading = fm.getLeading();
            int descent = fm.getDescent();
//            int x = false;
//            int y = false;
            int x = r.x + (r.width - textWidth) / 2;
            int y = r.y + r.height / 2 + (fontSize - leading) / 2 - descent;
            if (font != null) {
                g2d.setFont(font);
            }

            if (textColor != null) {
                g2d.setPaint(textColor);
            }

            g2d.drawString(this.text, x, y);
        }

    }

    protected void processMouseEvent(MouseEvent e) {
        int eventID = e.getID();
        if (eventID == 502) {
            Rectangle r = new Rectangle(this.getWidth(), this.getHeight());
            if (!r.contains(e.getPoint())) {
                return;
            }

            if (this.clickListener != null) {
                ActionEvent ae = new ActionEvent(this, eventID, getText());
                this.clickListener.actionPerformed(ae);
            }
        }

        if (eventID == 504) {
            this.isHover = true;
            this.repaint();
        } else if (eventID == 505) {
            this.isHover = false;
            this.repaint();
        }
    }

    public static class Style {
        public AfBorderStyle border;
        public Color bgColor;
        public Font font;
        public Color textColor;

        public Style() {
        }
    }
}
