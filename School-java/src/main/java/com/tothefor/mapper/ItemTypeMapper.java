package com.tothefor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tothefor.pojo.entity.ItemType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author DragonOne
 * @Date 2022/3/9 15:30
 * @墨水记忆 www.tothefor.com
 */

@Component
@Repository
@Mapper
public interface ItemTypeMapper extends BaseMapper<ItemType> {
}
