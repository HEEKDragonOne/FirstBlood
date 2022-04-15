package com.tothefor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tothefor.mapper.ItemTypeMapper;
import com.tothefor.pojo.entity.ItemType;
import com.tothefor.service.ItemTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 物品类型实现类
 * @Author DragonOne
 * @Date 2022/3/4 13:28
 * @since 2022-03-09 15:31:08
 */
@Service("ItemTypeServiceImpl")
public class ItemTypeServiceImpl extends ServiceImpl<ItemTypeMapper, ItemType> implements ItemTypeService {

}

