package cn.edu.sicau.pfdistribution.service.kafka.receiver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

//接收分配结果
@Component
public class KafkaPfAllocationResultReceiver {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Gson gson;

    @KafkaListener(topics = "yourkafka")
    public void processMessage(String msg) {
        Map<String, String> result = gson.fromJson(msg,new TypeToken<Map<String,String>>(){}.getType());
        for (String key : result.keySet()) {
            log.info("从kafka读取处理结果数据:" + key + ">>>>>>>>" + result.get(key));
        }
    }
}