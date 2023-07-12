package com.examsys.servises;

import com.alibaba.fastjson.JSON;
import com.examsys.modules.User;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class LoginService {
    //�����ַ�������res
    // 0:��¼�Ƿ�ɳɹ�:"1":�ɹ�;"0":ʧ��;
    // 1:�Ƿ��ǹ���Ա:"1":��;"0":��;
    // 2:������Ϣ
    public static String[] loginCheck(String id, String pwd) throws IOException {
        //��ȡ�û��ļ�
        Path path = Paths.get("data\\users.txt");
        String data = Files.readString(path);
        List<User> users = JSON.parseArray(data, User.class);

        //�ж��û�����ƥ��
        for (User loopUser : users) {
            if (loopUser.getId().equals(id)) {
                if (loopUser.getPassword().equals(String.valueOf(pwd.hashCode()))) {
                    return new String[]{"1", String.valueOf(loopUser.getIs_admin()), "��¼�ɹ�"};
                } else {
                    return new String[]{"0", "null", "�������"};
                }
            }
        }
        return new String[]{"0", "null","�û�������"};

    }
}
