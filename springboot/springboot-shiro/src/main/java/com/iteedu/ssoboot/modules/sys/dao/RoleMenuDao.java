package com.iteedu.ssoboot.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 权限角色表
 * 
 */
@Mapper
public interface RoleMenuDao {
    int delete(Map<String, Object> map);

    int deleteBatch(Object[] ids);

    int delete(Object id);

    void save(Map<String, Object> map);

    List<String> queryListByRoleId(String roleId);
}
