package cn.edu.sicau.pfdistribution.service.kafka.sender;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import cn.edu.sicau.pfdistribution.service.kafka.data.Message;

import java.util.Date;

@Component
@Slf4j
public class KafkaSender {
    Logger log = LoggerFactory.getLogger(getClass());

    private KafkaTemplate<String, String> kafkaTemplate;

    //自动注入
    @Autowired
    public KafkaSender(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private Gson gson = new GsonBuilder().create();

    //发送消息方法
    public void send(String msg) {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(msg);
        message.setSendTime(new Date());
        System.out.println("存进去一个数据");
        log.info("+++++++++++++++++++++  message = {}", gson.toJson(message));
        kafkaTemplate.send("mykafka", gson.toJson(message));
    }
}