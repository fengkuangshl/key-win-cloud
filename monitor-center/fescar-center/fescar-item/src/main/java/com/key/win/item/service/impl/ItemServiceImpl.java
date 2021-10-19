package com.key.win.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.web.Result;
import com.key.win.item.service.ItemService;
import com.key.win.item.dao.OcpItemMapper;
import com.key.win.item.entity.OcpItem;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl extends ServiceImpl<OcpItemMapper, OcpItem> implements ItemService {
    @Override
    public Result deductInventory(String productId) throws ServiceException {
        // TODO: 2020/3/11 条件构造器
        Wrapper<OcpItem> wrapper = new QueryWrapper<OcpItem>()
                .lambda().eq(OcpItem::getProductId,productId);

        OcpItem ocpItem = baseMapper.selectOne(wrapper);
        int i = ocpItem.getResidue() - 10;
        if (i >= 0){
            ocpItem.setUsed(ocpItem.getUsed() + 10);
            ocpItem.setResidue(i);
        }else {
            throw new ServiceException("库存不足");
        }
        return Result.succeedWith(baseMapper.updateById(ocpItem),0,null);
    }
}
