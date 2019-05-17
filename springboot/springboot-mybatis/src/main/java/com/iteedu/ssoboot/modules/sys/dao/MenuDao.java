package com.iteedu.ssoboot.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iteedu.ssoboot.modules.common.dao.BaseDao;
import com.iteedu.ssoboot.modules.sys.entity.MenuEntity;

/**
 * 菜单表
 * 
 */
@Mapper
public interface MenuDao extends BaseDao<MenuEntity> {

    /**
     *根据登陆用户Id,查询出所有授权菜单
     * @param userId
     * @return
     */
    List<MenuEntity> queryByUserId(String userId);

    /**
     * 根据父菜单Id查询菜单
     * @param parenId
     * @return
     */
    List<MenuEntity> queryListParentId(String parenId);

    /**
     * 查询所有不包括按钮 的菜单
     * @return
     */
    List<MenuEntity> queryNotButtonnList(String[] types);

}
