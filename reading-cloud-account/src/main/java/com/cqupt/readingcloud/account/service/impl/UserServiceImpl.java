package com.cqupt.readingcloud.account.service.impl;

import com.cqupt.readingcloud.common.pojo.account.User;
import com.cqupt.readingcloud.common.result.Result;
import com.cqupt.readingcloud.common.result.ResultUtil;
import com.cqupt.readingcloud.common.utils.CommonUtil;
import com.cqupt.readingcloud.account.bo.UserBO;
import com.cqupt.readingcloud.account.common.utils.JwtUtil;
import com.cqupt.readingcloud.account.common.utils.UserUtil;
import com.cqupt.readingcloud.account.dao.UserMapper;
import com.cqupt.readingcloud.account.service.UserService;
import com.cqupt.readingcloud.account.vo.AuthVO;
import com.cqupt.readingcloud.account.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.cqupt.readingcloud.common.constant.JwtConstant.EXPIRE_DAY;

import java.util.Calendar;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册用户信息
     * @param userBO
     * @return
     */
    @Override
    public Result register(UserBO userBO) {
        User dbUser = this.userMapper.selectByLoginName(userBO.getLoginName());
        if(dbUser != null)
            return ResultUtil.verificationFailed().buildMessage(userBO.getLoginName() + "已存在，请使用其它登录名进行注册！");

        User user = new User();
        BeanUtils.copyProperties(userBO, user);

        //加盐，重新赋值密码
        String encryptPwd = UserUtil.getUserEncryptPassword(userBO.getLoginName(), userBO.getUserPwd());
        user.setUserPwd(encryptPwd);

        try{
            //设置默认头像
            user.setHeadImgUrl("http://xjcode.cn/default.jpg");
            //设置唯一UUID
            user.setUuid(CommonUtil.getUUID());
            this.userMapper.insert(user);
        }
        catch(Exception e){
            //注册失败写入日志
            LOGGER.error("用户注册失败！{}; user:{}", e, user);
            return ResultUtil.fail().buildMessage("注册失败！");
        }

        return ResultUtil.success().buildMessage("注册成功！请重新登陆~");

    }

    /**
     * 登陆
     * @param loginName
     * @param password
     * @return
     *
     */
    @Override
    public Result<AuthVO> login(String loginName, String password) {
        try{
            //查询loginName信息，没有则返回无该用户
            User user = this.userMapper.selectByLoginName(loginName);
            if(null == user)
                return ResultUtil.notFound().buildMessage("登陆失败，用户不存在");

            //验证密码
            String encryptPwd = UserUtil.getUserEncryptPassword(loginName, password);
            if(!user.getUserPwd().equals(encryptPwd))
                return ResultUtil.verificationFailed().buildMessage("登陆失败，密码输入错误！");

            AuthVO vo = new AuthVO();
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            vo.setToken(JwtUtil.buildJwt(this.getLoginExpre(), userVO));
            vo.setUser(userVO);
            return ResultUtil.success(vo);
        }
        catch (Exception e){
            LOGGER.error("登陆失败{};loginName:{}", e, loginName);
            return ResultUtil.fail().buildMessage("登陆失败，请重试！");
        }
    }

    private Date getLoginExpre() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, EXPIRE_DAY);
        return calendar.getTime();
    }

    @Override
    public Result logout(String loginName) {
        return null;
    }

    @Override
    public Result update(UserBO userBO) {
        return null;
    }

    @Override
    public Result updatePassword(UserBO userBO) {
        return null;
    }
}
