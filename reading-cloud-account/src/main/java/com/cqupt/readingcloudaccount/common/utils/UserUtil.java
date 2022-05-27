package com.cqupt.readingcloudaccount.common.utils;

public class UserUtil {
    public static String getUserSalt(String loginName){
        //盐值
        String[] salts = {"sun","moon","star","sky","cloud","fog","rain","wind","rainbow"};
        int hashCode = loginName.hashCode() + 159;
        int mod = Math.abs(hashCode % 9);
        return salts[mod];

    }
}
