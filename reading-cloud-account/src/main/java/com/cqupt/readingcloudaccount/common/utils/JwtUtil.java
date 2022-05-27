package com.cqupt.readingcloudaccount.common.utils;

import com.cqupt.readingcloudaccount.vo.UserVO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import static com.cqupt.readingcloud.common.constant.JwtConstant.SECRET_KEY;

import java.util.Date;

public class JwtUtil {
    public static String buildJwt(Date expire, UserVO user){
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setExpiration(expire)
                .claim("loginName", user.getLoginName())
                .claim("nickName", user.getNickName())
                .claim("phoneNumber", user.getPhoneNumber())
                .claim("headImgUrl", user.getHeadImgUrl())
                .claim("uuid", user.getUuid())
                .claim("id", user.getId())
                .compact();

        return jwt;
    }
}
