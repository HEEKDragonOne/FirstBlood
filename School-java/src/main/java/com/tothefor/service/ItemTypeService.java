package com.tothefor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tothefor.pojo.entity.ItemType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 物品类型service
 * @Author DragonOne
 * @Date 2022/3/4 13:28
 * @since 2022-03-09 15:31:07
 */
@Service
public interface ItemTypeService extends IService<ItemType> {

}

