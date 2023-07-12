package com.examsys.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

//Textfield的限制类，限制长度
public class LongLimit extends PlainDocument {

    private int limit;  //限制的长度

    public LongLimit(int limit) {
        super(); //调用父类构造
        this.limit = limit;
    }
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if(str == null) return;

        //限制输入的长度
        if((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }

    }

}
