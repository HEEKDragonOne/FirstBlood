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
import com.tothefor.pojo.dto.PieData;
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
import java.util.*;

/**
 * 物品controller
 * @Author DragonOne
 * @Date 2022/3/4 13:28
 * @墨水记忆 www.tothefor.com
 */

@RestController
@RequestMapping("/item")
public class itemController {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ItemTypeMapper itemTypeMapper;

    @Autowired
    private ItemTypeServiceImpl itemTypeService;


    /**
     * @Author DragonOne
     * @Date 2022/3/23
     * @墨水记忆 www.tothefor.com
     * @方法名称 getTypeName
     * @方法描述 获取类型的全部名称
     * @参数说明
     * @返回值
     */
    @GetMapping("/getTypeName")
    public R getTypeName(){
        QueryWrapper<ItemType> qw = new QueryWrapper();
        qw.ne("type_id",0);
        List<ItemType> itemTypes = itemTypeMapper.selectList(qw);

        Map<String,Object> map = new HashMap<>();
        List<Integer> listi = new ArrayList<>();
        List<String> lists = new ArrayList<>();
        for(ItemType it: itemTypes){
            lists.add(it.getTypename());

            Long typeId = it.getTypeId();
            QueryWrapper<Item> qw2 = new QueryWrapper<>();
            qw2.eq("typeID",typeId);
            List<Item> items = itemMapper.selectList(qw2);
            listi.add(items.size());
        }
        map.put("x",CollUtil.newArrayList(lists));
        map.put("y",CollUtil.newArrayList(listi));

        return R.SUCCESS(map);
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/13
     * @墨水记忆 www.tothefor.com
     * @方法名称 getPieData
     * @方法描述 获取每个类型种类的对应数量
     * @参数说明
     * @返回值
     */
    @PostMapping("/getPie")
    public R getPieData(){
        System.out.println("=========<>><>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<+++:进入");
        List<PieData> pieData = new ArrayList<>();
        List<ItemType> itemTypes = itemTypeMapper.selectList(null);
        for(ItemType it : itemTypes){
            if(it.getTypeId()==0) itemTypes.remove(it);
        }
        for(ItemType it : itemTypes){
            Long typeId = it.getTypeId();
            QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("typeID",typeId);
            int typeCnt = itemMapper.selectList(queryWrapper).size();
            PieData mpd = new PieData();
            mpd.setName(it.getTypename());
            mpd.setValue(typeCnt);
            pieData.add(mpd);
        }
        System.out.println("=========<>><>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<+++:"+pieData.toString());
        return R.SUCCESS(pieData);
    }


    /**
     * @Author DragonOne
     * @Date 2022/3/13
     * @墨水记忆 www.tothefor.com
     * @方法名称 getItemCnt
     * @方法描述 获取物品总数
     * @参数说明
     * @返回值
     */
    @PostMapping("/getItemCnt")
    public R getItemCnt(){
        List<Item> items = itemMapper.selectList(null);
        int size = items.size();
        return R.SUCCESS(size);
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/13
     * @墨水记忆 www.tothefor.com
     * @方法名称 getItemTypeCnt
     * @方法描述 获取物品类型总数
     * @参数说明
     * @返回值
     */
    @PostMapping("/getItemTypeCnt")
    public R getItemTypeCnt(){
        List<ItemType> itemTypes = itemTypeMapper.selectList(null);
        int size = itemTypes.size();
       // size--;
        return R.SUCCESS(size);
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
    @GetMapping("/itemAll")
    public R<List<Item>> itemAll(){

        return R.SUCCESS(itemMapper.selectList(null));
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
    public R itemSave(@RequestBody Item item){


//        QueryWrapper<ItemType> qw = new QueryWrapper<>();
//        qw.eq("type_id",item.getTypeid());
//        ItemType itemType = itemTypeMapper.selectOne(qw);
//        item.setTypeName(itemType.getTypename());

        if(itemService.saveOrUpdate(item)){
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
    public R itemDelete(@PathVariable Integer id){
        if(itemMapper.deleteById(id)==1){
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
    public R itemBatchDelete(@RequestBody List<Integer> ids){
        int len = ids.size();
        if(itemMapper.deleteBatchIds(ids)==len){
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
    public R<IPage<Item>> PageItemAll(@RequestParam("pageNum") Integer pageNum,
                                   @RequestParam("PageSize") Integer pageSize,
                                   @RequestParam(defaultValue = "") String itemname,
                                   @RequestParam(defaultValue = "0") Integer searchtype){


        IPage<Item> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
//        if(!StringUtils.hasText(itemname)||itemtype==0){
//            if(!StringUtils.hasText(itemname)&&itemtype==0){
//                queryWrapper=null;
//            }
//            else{
//                if(!StringUtils.hasText(itemname)){
//                    queryWrapper.eq("typeID",itemtype);
//                }
//                if(itemtype==0){
//                    queryWrapper.like("name",itemname);
//                }
//            }
//        }else{
//            queryWrapper.like("name",itemname);
//            queryWrapper.eq("typeID",itemtype);
//        }
        if(StringUtils.hasText(itemname)){
            queryWrapper.like("name",itemname);
        }
        if(searchtype!=0){
            queryWrapper.eq("typeID",searchtype);
        }
        queryWrapper.orderByDesc("id");


        IPage<Item> page1 = itemService.page(page, queryWrapper);
        List<Item> records = page1.getRecords();
        for(Item it: records){
            Long typeid = it.getTypeid();
            QueryWrapper<ItemType> qw = new QueryWrapper();
            qw.eq("type_id",typeid);
            ItemType itemType = itemTypeMapper.selectOne(qw);
            it.setTypeName(itemType.getTypename());
        }
        return R.SUCCESS(page1);
    }

    /**
     * @Author DragonOne
     * @Date 2022/3/9 16:28
     * @墨水记忆 www.tothefor.com
     * @方法 itemShow
     * @作用 修改物品状态
     * @参数说明
     * @return
     */
    @PostMapping("/changeShow")
    public R itemShow(@RequestBody Item id){
        if(itemMapper.updateById(id)==1){
           return R.SUCCESS();
        }else {
           return R.FAIL();
        }
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
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        String token = request.getHeader("token");

        List<Map<String, Object>> list = CollUtil.newArrayList();
        List<Item> all = itemMapper.selectList(null);

        for(Item it : all){
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("物品唯一标识符",it.getId());
            row1.put("物品编号",it.getItemId());
            row1.put("物品名称",it.getName());
            row1.put("物品类型",it.getTypeName());
            row1.put("使用状态",it.getIsShow());
            row1.put("物品加入时间",it.getItemAddTime());
            row1.put("物品最后修改时间",it.getItemUpdateTime());
            row1.put("物品规格",it.getSize());
            row1.put("物品描述",it.getDescription());
            row1.put("物品数量",it.getItemCount());

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
        System.out.println("==========>>>>><<<<<<>>>>>>+++筋骨肉1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Item> items = CollUtil.newArrayList();
        try {

            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);

            List<List<Object>> list = reader.read(1);

            System.out.println(list.size());
            for (List<Object> row : list) {
                Item item = new Item();
                item.setItemId(row.get(0).toString());
                item.setName(row.get(1).toString());
                item.setTypeid(Long.valueOf(row.get(2).toString()));
                item.setSize(row.get(3).toString());
                item.setDescription(row.get(4).toString());
                item.setItemCount(Long.valueOf(row.get(5).toString()));

                items.add(item);
            }
        }catch (Exception e){
            return R.FAIL();
        }

        if(itemService.saveBatch(items)){
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
        Item it = new Item();
        it.setItemId("这里填写物品编号,任意");
        it.setName("这里填写物品名称,物品类型请填写数据库中已经存在的类型编号");
        it.setTypeid(2L);
        it.setSize("这里填写物品的规格");
        it.setDescription("物品的一些其他描述备注");
        it.setItemCount(23L);

            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("物品编号",it.getItemId());
            row1.put("物品名称",it.getName());
            row1.put("物品类型",it.getTypeid());
            row1.put("物品规格",it.getSize());
            row1.put("物品描述",it.getDescription());
            row1.put("物品数量",it.getItemCount());

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
