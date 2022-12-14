package com.ydan.my_second_kill.service.api;


import com.ydan.my_second_kill.pojo.Stock;

/**
 * @author : ydan
 * @date : 2022/6/15
 **/

public interface OrderService {

    /**
     * 清空订单表
     */
    int delOrderDBBefore();


    /**
     * redis秒杀成功后,在进行下单的操作(库存减1,创建订单)
     * @param id
     * @throws Exception
     */
    void createOrderWithRedis(Long id) throws Exception;

    /**
     * redis秒杀成功后,使用异步下单,并直接返回响应给用户
     * @param id
     * @throws Exception
     */
    void createOrderWithRedisAndKafaka(Long id) throws Exception;

    /**
     *下单操作:mysql中的商品库存减1,并生成
     * @param stock
     * @throws Exception
     */
     void createOrder(Stock stock) throws Exception;

}
