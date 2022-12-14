package com.ydan.my_second_kill.controller;

import com.ydan.my_second_kill.StockWithRedis.RedisLimit;
import com.ydan.my_second_kill.StockWithRedis.StockWithRedis;
import com.ydan.my_second_kill.pojo.Stock;
import com.ydan.my_second_kill.service.api.OrderService;
import com.ydan.my_second_kill.service.api.StockService;
import com.ydan.my_second_kill.util.ResBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author : ydan
 * @date : 2022/6/15
 **/

@Slf4j
@Api("测试接口")
@RestController
public class MyController {

    @Resource(name = "OrderService")
    OrderService orderService;

    @Resource(name = "StockService")
    StockService stockService;

    @Autowired
    StockWithRedis stockWithRedis;

    @Autowired
    RedisLimit redisLimit;

    /**
     * 初始化redis和mysql数据库,这里默认对id为1的商品的初始库存量为5000.
     *
     * @return
     */
    @ApiOperation(value = "初始化redis和mysql数据库", notes = "重置数据,方便测试")
    @GetMapping("/initDBAndRedis")
    public ResBean initDBAndRedis() {

        try {
            //初始化库存里面的商品值,库存为5000,sale =0,name=tomato;
            stockService.initDatabaseById(1L, 5000L);
            //清除订单记录
            orderService.delOrderDBBefore();
            //重置redis里面的缓存
            Stock stock = new Stock();
            stock.setCount(5000L);
            stock.setSale(0L);
            stock.setId(1L);
            stock.setName("土豆");
            stockWithRedis.resetRedis(stock);
        } catch (Exception e) {
            return ResBean.Error("初始化数据库和重置redis缓存失败");
        }

        return ResBean.OK("初始化数据库和重置redis缓存成功");
    }

    /**
     * reids+mysql来进行秒杀
     *
     * @param sid
     * @return
     */
    @ApiOperation(value = "使用redis缓存秒杀", notes = "无")
    @PostMapping("/createOrderWithRedis")
    public ResBean createOrderWithRedis(@RequestParam("sid") Long sid) {
        System.out.println("enter createOrderWithRedis");
        try {
            orderService.createOrderWithRedis(sid);

        } catch (Exception e) {
            return ResBean.Error("订单下达失败");
        }
        return ResBean.OK("订单下单成功");
    }

    /**
     * reids+mysql+限流
     *
     * @param sid
     * @return
     */
    @ApiOperation(value = "redis缓存+限流来进行秒杀", notes = "无")
    @PostMapping("/createOrderWithLimitAndRedis")
    public ResBean createOrderWithLimitAndRedis(@RequestParam("sid") Long sid) {
        //进行限流
        //限流成功后,在进行库存的判断删减

        try {
            if (redisLimit.limit()) {
                orderService.createOrderWithRedis(sid);
            } else {
                throw new RuntimeException();
            }

//            if(!stockWithRedis.limit(5000L)){
//              throw new RuntimeException();
//            }
//            orderService.createOrderWithRedis(sid);
        } catch (Exception e) {
            log.error("message:" + e.getMessage());
            return ResBean.Error("订单下达失败");
        }
        return ResBean.OK("订单下单成功");
    }


    /**
     * redis+mysql+异步下单
     *
     * @param sid
     * @return
     */
    @ApiOperation(value = "redis缓存+异步下单来进行秒杀", notes = "无")
    @PostMapping("/createOrderWithLimitAndRedisAndKafaka")
    public ResBean createOrderWithRedisAndKafaka(@RequestParam("sid") Long sid) {
        try {
            orderService.createOrderWithRedisAndKafaka(sid);
        } catch (Exception e) {
            return ResBean.Error("订单下达失败");
        }
        return ResBean.OK("订单下单成功");
    }

}
