package cn.edu.sicau.pfdistribution.service.kafka.sender;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.execution.columnar.MAP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;

@Component
@Slf4j
public class KafkaPfAllocationMessageSender {

    private KafkaTemplate<String, String> kafkaTemplate;

    //自动注入
    @Autowired
    public KafkaPfAllocationMessageSender(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private Gson gson = new GsonBuilder().create();

    //发送消息方法
    public ListenableFuture<SendResult<String, String>> send(String topic, Object message) {//Object message
        return kafkaTemplate.send(topic, gson.toJson(message));
    }
}