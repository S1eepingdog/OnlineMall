package com.ydan.my_second_kill.service.api;


import com.ydan.my_second_kill.pojo.Stock;

/**
 * @author : ydan
 * @date : 2022/6/15
 **/
public interface StockService {


    /**
     * 根据 id 查询剩余库存信息
     * @param id
     * @return stock
     */
    Stock selectStockById(int id);

    /**
     * 根据 id 更新库存信息
     * @param id
     * @return int
     */
    void updateStockById(Long id);

    /**
     * 乐观锁更新库存，解决超卖问题
     */
//    int updateStockByOptimistic(Stock stock) throws Exception;

    /**
     * 初始化数据库
     */
    void  initDatabaseById(Long id,Long count);
}
