package com.ydan.my_second_kill.kafka;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ydan.my_second_kill.pojo.Stock;
import com.ydan.my_second_kill.service.api.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * @auther ydan
 * @date 6/12 19:17
 */
@Slf4j
@Component
public class ConsumerListen {

    private Gson gson = new GsonBuilder().create();

    @Autowired
    private OrderService orderService;


    @KafkaListener(topics = "SECONDS-KILL-TOPIC")
    public void listen(ConsumerRecord<String, String> record) throws Exception {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        // Object -> String
        String message = (String) kafkaMessage.get();
        // 反序列化
        Stock stock = gson.fromJson((String) message, Stock.class);
        // 创建订单
        orderService.createOrder(stock);
    }
}
