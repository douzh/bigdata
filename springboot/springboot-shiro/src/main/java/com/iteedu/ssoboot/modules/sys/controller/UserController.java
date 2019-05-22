package com.iteedu.ssoboot.modules.sys.controller;


import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iteedu.ssoboot.modules.common.Constant;
import com.iteedu.ssoboot.modules.common.controller.BaseController;
import com.iteedu.ssoboot.modules.common.utils.PageUtils;
import com.iteedu.ssoboot.modules.common.utils.Query;
import com.iteedu.ssoboot.modules.common.utils.Result;
import com.iteedu.ssoboot.modules.common.utils.ShiroUtils;
import com.iteedu.ssoboot.modules.sys.entity.UserEntity;
import com.iteedu.ssoboot.modules.sys.service.UserRoleService;
import com.iteedu.ssoboot.modules.sys.service.UserService;


/**
 * 系统用户表
 * 
 */
@RestController
@RequestMapping("sys/user")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;

	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<UserEntity> userList = userService.queryList(query);
		int total = userService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
		
		return Result.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:user:info")
	public Result info(@PathVariable("id") String id){
		UserEntity user = userService.queryObject(id);
		if(user != null){
			user.setPassWord("");
			user.setRoleIdList(userRoleService.queryRoleIdList(user.getId()));
		}
		return Result.ok().put("user", user);
	}

	/**
	 *
	 * 主页用户信息
	 */
	@RequestMapping("/info")
	public Result info(){
		UserEntity user = userService.queryObject(ShiroUtils.getUserId());
        return Result.ok().put("user", user);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:update")
	public Result save(@RequestBody UserEntity user){
		userService.save(user);
		return Result.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update")
	@RequiresPermissions("sys:user:update")
	public Result update(@RequestBody UserEntity user){
		user.setPassWord(null);
		userService.update(user);
		
		return Result.ok();
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
	public Result updatePassword(UserEntity user){
        int i = userService.updatePassword(user);
        if(i<1){
            return Result.error("更改密码失败");
        }
        return Result.ok("更改密码成功");
	}

	/**
	 * 禁用、
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public Result delete(@RequestBody String[] ids){
		userService.updateBatchStatus(ids, Constant.ABLE_STATUS.NO.getValue());
		return Result.ok();
	}

	/**
	 * 启用、
	 */
	@RequestMapping("/enabled")
	@RequiresPermissions("sys:user:enabled")
	public Result enabled(@RequestBody String[] ids){
		userService.updateBatchStatus(ids, Constant.ABLE_STATUS.YES.getValue());
		return Result.ok();
	}

	/**
	 * 重置密码
	 */
	@RequestMapping("/reset")
	@RequiresPermissions("sys:user:reset")
	public Result reset(@RequestBody String[] ids){
		userService.resetPassWord(ids);
		return Result.ok("重置密码成功,密码为:"+Constant.DEF_PASSWORD);
	}

}
