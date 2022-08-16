package com.ydan.my_second_kill;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
class MySecondKillApplicationTests {

//    @Autowired
//    KafkaTemplate<String,String> kafkaTemplate;

    //对该主题的消息队列发送消息
    @Value("${spring.kafka.template.default-topic}")
    String kafkaTopic;

    Gson gson = new GsonBuilder().create();


    @Test
    void produceMessage() {
        Map<String,String> map= new HashMap<>();
        map.put("name","jone");
        map.put("sex","man");
        map.put("age","20");
        //对发送的消息进行序列化,并进行SECONDS-KILL-TOPIC主题的发送
//        kafkaTemplate.send(kafkaTopic,gson.toJson(map));
        System.out.println("producer send message");
    }

    //对"SECONDS-KILL-TOPIC"进行监听,以便供消费者消费
    @KafkaListener(topics = "SECONDS-KILL-TOPIC")
    public void listen(ConsumerRecord<String, String> record) throws Exception {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        // Object -> String
        String message = (String) kafkaMessage.get();
        System.out.println("consumer receive  message");
        // 反序列化
        Map<String,String> map = gson.fromJson((String) message,  Map.class);
        System.out.println("message:"+map);
    }

}
