package com.examsys.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

//Textfield的限制类，限制长度和数字
public class NumberLongLimit extends PlainDocument {

    private int limit;  //限制的长度

    public NumberLongLimit(int limit) {
        super(); //调用父类构造
        this.limit = limit;
    }
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if(str == null) return;

        //下面的判断条件改为自己想要限制的条件即可，这里为限制输入的长度
        if((getLength() + str.length()) <= limit) {
            if (this.isNumeric(str)) {
                super.insertString(offset, str, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

    }
    private boolean isNumeric(String var1) {
        try {
            Long.valueOf(var1);
            return true;
        } catch (NumberFormatException var3) {
            return false;
        }
    }

}
