package com.examsys.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

//Textfield�������࣬���Ƴ���
public class LongLimit extends PlainDocument {

    private int limit;  //���Ƶĳ���

    public LongLimit(int limit) {
        super(); //���ø��๹��
        this.limit = limit;
    }
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if(str == null) return;

        //��������ĳ���
        if((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }

    }

}
