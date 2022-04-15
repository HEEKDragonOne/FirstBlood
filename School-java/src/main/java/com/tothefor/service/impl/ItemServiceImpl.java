package com.tothefor.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tothefor.mapper.ItemMapper;
import com.tothefor.pojo.entity.Item;
import com.tothefor.service.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 物品实现类
 * @Author DragonOne
 * @Date 2022/3/4 13:28
 * @墨水记忆 www.tothefor.com
 */
@Service("ItemServiceImpl")
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

}

