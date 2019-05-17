package com.iteedu.ssoboot.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iteedu.ssoboot.modules.common.dao.BaseDao;
import com.iteedu.ssoboot.modules.sys.entity.UserRoleEntity;

/**
 * 用户角色关系表
 * 
 */
@Mapper
public interface UserRoleDao extends BaseDao<UserRoleEntity> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<String> queryRoleIdList(String userId);

    /**
     * 根据用户list批量删除用户角色中间表
     * @param users
     * @return
     */
    int deleteBatchByUserId(String[] users);

    /**
     * 根据角色list批量删除用户角色中间表
     * @param roles
     * @return
     */
    int deleteBatchByRoleId(String[] roles);

	
}
