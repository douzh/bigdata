package com.iteedu.ssoboot.modules.common.utils;


import com.iteedu.ssoboot.modules.common.Constant;
import com.iteedu.ssoboot.modules.sys.entity.UserEntity;


/**
 * 类的功能描述.
 * 用户工具类
 */

public class UserUtils {

    /**
     * 获取当前登陆用户
     * @return
     */
    public static UserEntity getCurrentUser(){
        UserEntity user = ShiroUtils.getUserEntity();
        return user;
    }

    /**
     * 获取当前登陆用户 待完善缓存
     * @return
     */
    public static String getCurrentUserId(){
        UserEntity user = ShiroUtils.getUserEntity();
        return user.getId();
    }


    /**
     * 获取机构部门数据权限
     * @param type 1=部门权限，2=机构权限，3=部门机构权限
     * @return
     */
    public static String getDateAuth(String type){
        UserEntity user = UserUtils.getCurrentUser();
        if (user ==null){
            return null;
        }
        if(Constant.DataAuth.BA_DATA.getValue().equals(type)){
            return user.getBaids();
        }
        if(Constant.DataAuth.BAP_DATA.getValue().equals(type)){
            return user.getBapids();
        }
        if(Constant.DataAuth.ALL_DATA.getValue().equals(type)){
            return user.getBaids()+user.getBapids();
        }
        return null;
    }
}
