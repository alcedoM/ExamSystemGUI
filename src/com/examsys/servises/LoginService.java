package com.examsys.servises;

import com.alibaba.fastjson.JSON;
import com.examsys.modules.User;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class LoginService {
    //返回字符串数组res
    // 0:登录是否成成功:"1":成功;"0":失败;
    // 1:是否是管理员:"1":是;"0":否;
    // 2:返回消息
    public static String[] loginCheck(String id, String pwd) throws IOException {
        //读取用户文件
        Path path = Paths.get("data\\users.txt");
        String data = Files.readString(path);
        List<User> users = JSON.parseArray(data, User.class);

        //判断用户密码匹配
        for (User loopUser : users) {
            if (loopUser.getId().equals(id)) {
                if (loopUser.getPassword().equals(String.valueOf(pwd.hashCode()))) {
                    return new String[]{"1", String.valueOf(loopUser.getIs_admin()), "登录成功"};
                } else {
                    return new String[]{"0", "null", "密码错误"};
                }
            }
        }
        return new String[]{"0", "null","用户不存在"};

    }
}
