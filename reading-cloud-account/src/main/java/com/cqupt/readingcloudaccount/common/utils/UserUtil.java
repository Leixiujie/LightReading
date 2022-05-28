package com.cqupt.readingcloudaccount.common.utils;

import com.cqupt.readingcloud.common.utils.MD5Util;

public class UserUtil {
    /**
     * 获取用户盐值，对用户密码加密
     * @param loginName
     * @return
     *
     */
    public static String getUserSalt(String loginName){
        String[] salts = {"sun","moon","star","sky","cloud","fog","rain","wind","rainbow"};
        //加盐
        int hashCode = loginName.hashCode() + 159;
        int mod = Math.abs(hashCode % 9);
        return salts[mod];
    }

    public static String getUserEncryptPassword(String loginName, String password){
        String pwdAndSalt = password + getUserSalt(loginName);
        return MD5Util.MD5Encode(pwdAndSalt, "utf8");
    }

    public static void main(String[] args) {
        String admin = getUserEncryptPassword("admin", "111");
        System.out.println("encrypted password:" + admin);
    }
}
