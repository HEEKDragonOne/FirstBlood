package com.tothefor.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tothefor.mapper.MenuMapper;
import com.tothefor.mapper.RoleMenuMapper;
import com.tothefor.mapper.UserMapper;
import com.tothefor.mapper.UserRoleMapper;
import com.tothefor.pojo.dto.CheckURL;
import com.tothefor.pojo.dto.LoginDTO;
import com.tothefor.pojo.dto.RequestReturnInfo;
import com.tothefor.pojo.dto.UserPassword;
import com.tothefor.pojo.entity.*;
import com.tothefor.resultR.R;
import com.tothefor.service.impl.MenuServiceImpl;
import com.tothefor.service.impl.RoleMenuServiceImpl;
import com.tothefor.service.impl.UserRoleServiceImpl;
import com.tothefor.service.impl.UserServiceImpl;
import com.tothefor.utils.JWTUtils;
import com.tothefor.utils.MD5Utils;
import com.tothefor.utils.RequestInfoUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author DragonOne
 * @Date 2022/3/4 15:31
 * @墨水记忆 www.tothefor.com
 */

@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuServiceImpl menuService;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleMenuServiceImpl roleMenuService;



    @PostMapping("/updateOther")
    public R updateOther(@RequestBody UserPassword userPassword){
        Long userid = userPassword.getUserid();
        User user = userMapper.selectById(userid);
        if(user==null){
            return R.FAIL();
        }else {
            String newP = userPassword.getNewP();
            newP = MD5Utils.getMD5(newP);
            user.setPassword(newP);
            userMapper.updateById(user);
            return R.SUCCESS();
        }
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/13
     * @墨水记忆 www.tothefor.com
     * @方法名称 updatePassword
     * @方法描述 个人修改密码
     * @参数说明
     * @返回值
     */
    @PostMapping("/updateP")
    public R updatePassword(@RequestBody UserPassword userPassword){
        Long userid = userPassword.getUserid();
        String oldP = userPassword.getOldP();
        oldP = MD5Utils.getMD5(oldP);
        User user = userMapper.selectById(userid);
        if(user.getPassword().equals(oldP)){ //旧密码正确,进行密码的修改
            String newP = userPassword.getNewP();
            newP = MD5Utils.getMD5(newP);
            user.setPassword(newP);
            userMapper.updateById(user);
            return R.SUCCESS();
        }else {
            return R.FAIL();
        }
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/13
     * @墨水记忆 www.tothefor.com
     * @方法名称 getUserIdByToken
     * @方法描述 通过token获取用户id
     * @参数说明
     * @返回值
     */
    @PostMapping("/getUserId")
    public R getUserIdByToken(@RequestBody String token){
        String idByToken = JWTUtils.getIdByToken(token);
        Long integer = Long.valueOf(idByToken);
        return R.SUCCESS(integer);
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/13
     * @墨水记忆 www.tothefor.com
     * @方法名称 getT
     * @方法描述 获取请求用户的系统信息
     * @参数说明
     * @返回值
     */
    @GetMapping("/getUserInfo")
    public R getT(HttpServletRequest request){
        String ip = RequestInfoUtils.getIp(request);
        String browserName = RequestInfoUtils.getBrowserName(request);
        String browserVersion = RequestInfoUtils.getBrowserVersion(request);
        String osName = RequestInfoUtils.getOsName(request);
        RequestReturnInfo requestReturnInfo = new RequestReturnInfo();
        requestReturnInfo.setIp(ip);
        requestReturnInfo.setBrowserName(browserName);
        requestReturnInfo.setBrowserVersion(browserVersion);
        requestReturnInfo.setOsName(osName);
        return R.SUCCESS(requestReturnInfo);
    }

    /**
     * 通过token获取当前登录的用户名
     * @param token
     * @return
     */
    @PostMapping("/getUserNameByToken")
    public R getUserName(@RequestBody String token){
        return R.SUCCESS(JWTUtils.getNameByToken(token));
    }

    /**
     * 检验用户是否存在访问权限
     */
    @PostMapping("/checkURL")
    public R checkUserURL(@RequestBody CheckURL checkURL){
        String userToken = checkURL.getToken();
        String nameByToken = JWTUtils.getNameByToken(userToken);
        String urlpath = checkURL.getUrlpath();
        List<Menu> menus = userService.menuByName(nameByToken);
        for(Menu it : menus){
            List<Menu> children = it.getChildren();
            for(Menu subit : children){
                if(subit.getMenuPath().equals(urlpath)){
                    return R.SUCCESS();
                }
            }
        }
        return R.FAIL();
    }

    /**
     * 通过token获取用户权限菜单
     */
    @PostMapping("/getMenu")
    public R getMenus(@RequestBody String token){
        String nameByToken = JWTUtils.getNameByToken(token);
        List<Menu> menus = userService.menuByName(nameByToken);
        return R.SUCCESS(menus);
    }

    /**
     * 检验token
     */
    @PostMapping("/checkToken")
    public R checkToken(@RequestBody String token){
        if(JWTUtils.checkToken(token)){
            return R.SUCCESS();
        }else{
            return R.FAIL();
        }
    }


    /**
     * @Author DragonOne
     * @Date 2022/3/12 20:01
     * @墨水记忆 www.tothefor.com
     * @方法 itemAll
     * @作用 用户登录
     * @参数说明
     * @return
     */
    @PostMapping("/login")
    public R userLogin(@RequestBody LoginDTO loginDTO){
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        password = MD5Utils.getMD5(password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userMapper.selectOne(queryWrapper);
        if(user.getPassword().equals(password)){ //登录成功
            //设置token
            Map<String,String> hm = new HashMap<>();
            hm.put("username",username);
            hm.put("userid",String.valueOf(user.getId()));
            String token = JWTUtils.getToken(hm);
            loginDTO.setUserToken(token);

            loginDTO.setMenus(userService.menuByName(username));
            return R.SUCCESS(loginDTO);
        }else {
            return R.FAIL();
        }

    }


    /**
     * @Author DragonOne
     * @Date 2022/3/6 17:01
     * @墨水记忆 www.tothefor.com
     * @方法 itemAll
     * @作用 查找所有物品信息接口
     * @参数说明
     * @return
     */
    @GetMapping("/userAll")
    public R<List<User>> userAll(){

        return R.SUCCESS(userMapper.selectList(null));
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/6 17:02
     * @墨水记忆 www.tothefor.com
     * @方法 itemSave
     * @作用 插入和修改接口
     * @参数说明
     * @return
     */
    @PostMapping("/save")
    public R userSave(@RequestBody User item){

        if(userService.saveOrUpdate(item)){
            return R.SUCCESS();
        }else {
            return R.FAIL();
        }
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/6 17:06
     * @墨水记忆 www.tothefor.com
     * @方法 itemDelete
     * @作用 根据id删除信息
     * @参数说明
     * @return
     */
    @DeleteMapping("/del/{id}")
    public R userDelete(@PathVariable Integer id){
        if(userMapper.deleteById(id)==1){
            return R.SUCCESS();
        }else{
            return R.FAIL();
        }
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/6 17:11
     * @墨水记忆 www.tothefor.com
     * @方法 itemBatchDelete
     * @作用 批量删除
     * @参数说明
     * @return
     */
    @PostMapping("/batch/del")
    public R userBatchDelete(@RequestBody List<Integer> ids){
        int len = ids.size();
        if(userMapper.deleteBatchIds(ids)==len){
            return R.SUCCESS();
        }else {
            return R.FAIL();
        }
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/7 12:37
     * @墨水记忆 www.tothefor.com
     * @方法 PageitemAll
     * @作用 物品的分页查询
     * @参数说明
     * @return
     */
    @GetMapping("/pageAll")
    public R<IPage<User>> PageUserAll(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("PageSize") Integer pageSize,
                                      @RequestParam(defaultValue = "") String itemname,
                                      @RequestParam(defaultValue = "0") Integer searchtype){


        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(itemname)){
            queryWrapper.like("username",itemname);
        }
        if(searchtype!=0){
            queryWrapper.eq("role_ID",searchtype);
        }
        queryWrapper.ne("id",1);
        queryWrapper.orderByDesc("id");


        IPage<User> page1 = userService.page(page, queryWrapper);
        List<User> records = page1.getRecords();
        for(User it: records){
            Long typeid = it.getRoleId();
            QueryWrapper<UserRole> qw = new QueryWrapper();
            qw.eq("id",typeid);
            UserRole userRole = userRoleMapper.selectOne(qw);
            it.setRoleName(userRole.getRolename());
        }
        return R.SUCCESS(page1);
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/9 17:13
     * @墨水记忆 www.tothefor.com
     * @方法 export
     * @作用 数据导出
     * @参数说明
     * @return
     */
    @GetMapping("/export")
    public void exportU(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        String token = request.getHeader("token");

        List<Map<String, Object>> list = CollUtil.newArrayList();
        List<User> all = userMapper.selectList(null);

        for(User it : all){
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("唯一标识符",it.getId());
            row1.put("用户编号",it.getUserid());
            row1.put("用户名称",it.getUsername());
            row1.put("用户类型",it.getRoleId());
            row1.put("用户描述",it.getDescription());

            list.add(row1);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("下载信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);


    }

    /**
     * @Author DragonOne
     * @Date 2022/3/9 17:13
     * @墨水记忆 www.tothefor.com
     * @方法 imp
     * @作用 数据导入
     * @参数说明
     * @return
     */
    @PostMapping("/import")
    public R imp(MultipartFile file) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<User> items = CollUtil.newArrayList();
        try {

            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);

            List<List<Object>> list = reader.read(1);

            System.out.println(list.size());
            for (List<Object> row : list) {
                User item = new User();
                item.setUserid(row.get(0).toString());
                item.setUsername(row.get(1).toString());
                item.setRoleId(Long.valueOf(row.get(2).toString()));
                item.setDescription(row.get(3).toString());

                items.add(item);
            }
        }catch (Exception e){
            return R.FAIL();
        }

        if(userService.saveBatch(items)){
            return R.SUCCESS();
        }else{
            return R.FAIL();
        }
    }


    /**
     * @Author DragonOne
     * @Date 2022/3/10 10:30
     * @墨水记忆 www.tothefor.com
     * @方法 exportMB
     * @作用 导入数据模板
     * @参数说明
     * @return
     */
    @GetMapping("/exportMB")
    public void exportMB(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        String token = request.getHeader("token");

        List<Map<String, Object>> list = CollUtil.newArrayList();
        User it = new User();
        it.setUserid("这里填写用户编号");
        it.setUsername("这里填写用户名称,用户类型请填写数据库中已经存在的角色编号");
        it.setRoleId(2L);
        it.setDescription("用户的一些其他描述备注");


        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("用户编号",it.getUserid());
        row1.put("用户名称",it.getUsername());
        row1.put("用户类型",it.getRoleId());
        row1.put("物品描述",it.getDescription());

        list.add(row1);


        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("导入模板", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);


    }

}
