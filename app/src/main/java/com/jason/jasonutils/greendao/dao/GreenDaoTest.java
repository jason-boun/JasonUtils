package com.jason.jasonutils.greendao.dao;

import android.content.Context;
import android.util.Log;

import com.jason.jasonutils.greendao.bean.User;

import java.util.List;

/**
 * Created by jiabo on 16/8/11.
 */
public class GreenDaoTest {

    /**
     * 查询数据
     * @param context
     */
    public static void greenDaoTest(Context context){

        DBManager dbManager = DBManager.getInstance(context);
        List<User> userList = dbManager.queryUsers();
        for (User user : userList) {
            Log.e("TAG", "queryUserList--before-->" + user.getId() + "--" + user.getName() +"--"+user.getAge());
            if (user.getId() == 0) {
                dbManager.deleteUser(user);
            }
            if (user.getId() == 3) {
                user.setAge(10);
                dbManager.updateUser(user);
            }
        }
        userList = dbManager.queryUsers();
        for (User user : userList) {
            Log.e("TAG", "queryUserList--after--->" + user.getId() + "---" + user.getName()+"--"+user.getAge());
        }
    }

    /**
     *  生成数据
     * @param context
     */
    public static void insertData2DB(Context context){
        DBManager dbManager = DBManager.getInstance(context);
        if (dbManager.queryUsers() ==null || dbManager.queryUsers().size() == 0){
            for (int i = 0; i < 5; i++) {
                User user = new User();
                user.setId(i);
                user.setAge(i * 3);
                user.setName("第" + i + "人");
                dbManager.insertUser(user);
            }
            List<User> userList = dbManager.queryUsers();
            for (User user : userList) {
                Log.e("TAG", "queryUserList--after--->" + user.getId() + "---" + user.getName()+"--"+user.getAge());
            }
        }
    }

}
