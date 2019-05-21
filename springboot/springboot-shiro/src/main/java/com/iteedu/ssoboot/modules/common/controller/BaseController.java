package com.iteedu.ssoboot.modules.common.controller;



import com.iteedu.ssoboot.modules.common.utils.UserUtils;
import com.iteedu.ssoboot.modules.sys.entity.UserEntity;

/**
 * 类的功能描述.
 * 公共的控件器，可在类中实现一些共同的方法和属性 持续更新中
 */
public class BaseController {

    /**
     * 获取当前登陆用户
     * @return
     */
    public UserEntity getUser(){
        return UserUtils.getCurrentUser();
    }

    /**
     * 获取当前登陆用户Id
     * @return
     */
    public String getUserId(){
        UserEntity user = getUser();
        if(null != user && null !=user.getId()){
            return user.getId();
        }
        return "";
    }


}
