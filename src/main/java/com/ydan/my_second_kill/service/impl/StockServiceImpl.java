package com.ydan.my_second_kill.service.impl;


import com.ydan.my_second_kill.mapper.StockMapper;
import com.ydan.my_second_kill.pojo.Stock;
import com.ydan.my_second_kill.service.api.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ydan
 * @date : 2022/6/15
 **/
@Service(value = "StockService")
public class StockServiceImpl implements StockService {

    @Autowired
    private StockMapper stockMapper;

    @Override
    public Stock selectStockById(int id) {
        return stockMapper.selectStockById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStockById(Long id) {
        stockMapper.updateStockById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initDatabaseById(Long id,Long count) {
        stockMapper.initDatabaseById(id,count);
    }
}
