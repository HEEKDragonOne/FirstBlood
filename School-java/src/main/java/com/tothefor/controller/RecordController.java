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
import com.tothefor.mapper.RecordMapper;
import com.tothefor.pojo.dto.PieData;
import com.tothefor.pojo.entity.Item;
import com.tothefor.pojo.entity.ItemType;
import com.tothefor.pojo.entity.Record;
import com.tothefor.resultR.R;
import com.tothefor.service.impl.ItemServiceImpl;
import com.tothefor.service.impl.ItemTypeServiceImpl;
import com.tothefor.service.impl.RecordServiceImpl;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品controller
 * @Author DragonOne
 * @Date 2022/3/4 13:28
 * @墨水记忆 www.tothefor.com
 */

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private RecordServiceImpl recordService;


    /**
     * @Author DragonOne
     * @Date 2022/3/6 17:01
     * @墨水记忆 www.tothefor.com
     * @方法 itemAll
     * @作用 查找所有物品信息接口
     * @参数说明
     * @return
     */
    @GetMapping("/recordAll")
    public R<List<Record>> itemAll(){

        return R.SUCCESS(recordMapper.selectList(null));
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
    public R itemSave(@RequestBody Record item){

        if(recordService.saveOrUpdate(item)){
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
        if(recordMapper.deleteById(id)==1){
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
        if(recordMapper.deleteBatchIds(ids)==len){
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
    public R<IPage<Record>> PageItemAll(@RequestParam("pageNum") Integer pageNum,
                                   @RequestParam("PageSize") Integer pageSize,
                                   @RequestParam(defaultValue = "") String itemname){


        IPage<Record> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(itemname)){
            queryWrapper.like("record_name",itemname);
        }
        queryWrapper.orderByDesc("id");


        IPage<Record> page1 = recordService.page(page, queryWrapper);
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
    public R itemShow(@RequestBody Record id){
        if(recordMapper.updateById(id)==1){
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
        List<Record> all = recordMapper.selectList(null);

        for(Record it : all){
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("借出唯一标识符",it.getId());
            row1.put("借出单名称",it.getRecordName());
            row1.put("领取者",it.getRecordMan());
            row1.put("添加时间",it.getRecordAddTime());
            row1.put("最后修改时间",it.getRecordUpdateTime());
            row1.put("描述",it.getDescription());

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


}
