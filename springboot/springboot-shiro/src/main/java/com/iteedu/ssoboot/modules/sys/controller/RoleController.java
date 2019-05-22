package com.iteedu.ssoboot.modules.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iteedu.ssoboot.modules.common.Constant;
import com.iteedu.ssoboot.modules.common.controller.BaseController;
import com.iteedu.ssoboot.modules.common.utils.PageUtils;
import com.iteedu.ssoboot.modules.common.utils.Query;
import com.iteedu.ssoboot.modules.common.utils.Result;
import com.iteedu.ssoboot.modules.sys.entity.RoleEntity;
import com.iteedu.ssoboot.modules.sys.service.RoleMenuService;
import com.iteedu.ssoboot.modules.sys.service.RoleService;


/**
 * 角色表
 * 
 */
@RestController
@RequestMapping("sys/role")
public class RoleController extends BaseController {
	@Autowired
	private RoleService roleService;
	@Autowired
    private RoleMenuService roleMenuService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:role:list")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<RoleEntity> roleList = roleService.queryList(query);
		int total = roleService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(roleList, total, query.getLimit(), query.getPage());
		
		return Result.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:role:info")
	public Result info(@PathVariable("id") String id){
		RoleEntity role = roleService.queryObject(id);
        List<String> organIdList = roleService.queryOrganRoleByRoleId(id);
        role.setOrganIdList(organIdList);
        List<String> menuIds = roleMenuService.queryListByRoleId(id);
        role.setMenuIdList(menuIds);
        return Result.ok().put("role", role);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:role:update")
	public Result save(@RequestBody RoleEntity role){
        Result result = Result.ok();
        try {
			roleService.save(role);
		} catch (Exception e) {
            result=Result.error();
            e.printStackTrace();
		}
		return result;
	}

	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	public Result select(){
		Map<String, Object> map = new HashMap<>();

		//如果不是超级管理员，则只查询自己所拥有的角色列表
		if(getUserId() != Constant.SUPERR_USER){
			map.put("createUserId", getUserId());
		}
		List<RoleEntity> list = roleService.queryList(map);

		return Result.ok().put("list", list);
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public Result update(@RequestBody RoleEntity role){
		roleService.update(role);
		
		return Result.ok();
	}
	
	/**
	 * 禁用
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public Result delete(@RequestBody String[] ids){
        Result result = Result.ok();
        try {
            roleService.updateBatchStatus(ids,Constant.ABLE_STATUS.NO.getValue());
        } catch (Exception e) {
            result=result.error();
            e.printStackTrace();
        }
        return result;
	}

	/**
	 * 启用
	 */
	@RequestMapping("/enabled")
	@RequiresPermissions("sys:role:enabled")
	public Result enabled(@RequestBody String[] ids){
		Result result = Result.ok();
		try {
			roleService.updateBatchStatus(ids,Constant.ABLE_STATUS.YES.getValue());
		} catch (Exception e) {
			result=result.error();
			e.printStackTrace();
		}
		return result;
	}
	
}
