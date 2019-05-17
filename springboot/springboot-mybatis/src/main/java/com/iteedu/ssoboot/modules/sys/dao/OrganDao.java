package com.iteedu.ssoboot.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iteedu.ssoboot.modules.common.dao.BaseDao;
import com.iteedu.ssoboot.modules.sys.entity.OrganEntity;
import com.iteedu.ssoboot.modules.sys.entity.UserWindowDto;

/**
 * 组织表
 * 
 */
@Mapper
public interface OrganDao extends BaseDao<OrganEntity> {
    /**
     * 根据实体条件查询
     * @return
     */
    List<UserWindowDto> queryPageByDto(UserWindowDto userWindowDto);
}
