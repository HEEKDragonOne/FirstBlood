package com.tothefor.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tothefor.mapper.ItemMapper;
import com.tothefor.mapper.ItemTypeMapper;
import com.tothefor.pojo.entity.Item;
import com.tothefor.pojo.entity.ItemType;
import com.tothefor.resultR.R;
import com.tothefor.service.impl.ItemServiceImpl;
import com.tothefor.service.impl.ItemTypeServiceImpl;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author DragonOne
 * @Date 2022/3/9 15:34
 * @墨水记忆 www.tothefor.com
 */

@RestController
@RequestMapping("/itemType")
public class itemTypeController {

    @Autowired
    private ItemTypeMapper itemTypeMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ItemTypeServiceImpl itemTypeService;


    /**
     * @Author DragonOne
     * @Date 2022/3/9 15:44
     * @墨水记忆 www.tothefor.com
     * @方法 itemTypeAll
     * @作用 查询全部类型
     * @参数说明
     * @return
     */
    @GetMapping("/itemTypeAll")
    public R<List<ItemType>> itemTypeAll(){
        return R.SUCCESS(itemTypeMapper.selectList(null));
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
    public R itemTypeSave(@RequestBody ItemType item){
        Long newTypeId = item.getTypeId();
        Long oldTypeID = null;
        Long id = item.getId();
        if(id!=null){
            QueryWrapper<Item> qw = new QueryWrapper<>();

            ItemType itemType = itemTypeMapper.selectById(id);
            oldTypeID = itemType.getTypeId();

            qw.eq("typeID",oldTypeID);
            List<Item> items = itemMapper.selectList(qw);

            for(Item it : items){
                it.setTypeid(newTypeId);
                itemService.updateById(it);
            }

        }



        if(itemTypeService.saveOrUpdate(item)){

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
    public R itemTypeDelete(@PathVariable Integer id){
        if(itemTypeMapper.deleteById(id)==1){
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
    public R itemTypeBatchDelete(@RequestBody List<Integer> ids){
        int len = ids.size();
        if(itemTypeMapper.deleteBatchIds(ids)==len){
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
    public R<IPage<ItemType>> PageItemTypeAll(@RequestParam("pageNum") Integer pageNum,
                                          @RequestParam("PageSize") Integer pageSize,
                                          @RequestParam(defaultValue = "") String itemname){

        IPage<ItemType> page = new Page<>(pageNum,pageSize);
        QueryWrapper<ItemType> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(itemname)){
            queryWrapper.like("typename",itemname);
        }
       // queryWrapper.ne("id",1);
        queryWrapper.orderByDesc("id");
        return R.SUCCESS(itemTypeService.page(page, queryWrapper));
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
    public void exportType(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        String token = request.getHeader("token");

        List<Map<String, Object>> list = CollUtil.newArrayList();
        List<ItemType> all = itemTypeMapper.selectList(null);

        for(ItemType it : all){
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("类型唯一标识符",it.getId());
            row1.put("类型编号",it.getTypeId());
            row1.put("类型名称",it.getTypename());
            row1.put("类型描述",it.getDescription());

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
        List<ItemType> items = CollUtil.newArrayList();
        try {

            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);

            List<List<Object>> list = reader.read(1);

            System.out.println(list.size());
            for (List<Object> row : list) {
                ItemType item = new ItemType();
                item.setTypeId(Long.valueOf(row.get(0).toString()));
                item.setTypename(row.get(1).toString());
                item.setDescription(row.get(2).toString());

                items.add(item);
            }
        }catch (Exception e){
            return R.FAIL();
        }

        if(itemTypeService.saveBatch(items)){
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
        ItemType it = new ItemType();
        it.setTypeId(2L);
        it.setTypename("这里填写类型名称");
        it.setDescription("类型描述");


        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("物品编号",it.getTypeId());
        row1.put("物品名称",it.getTypename());
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
