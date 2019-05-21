package cn.edu.sicau.pfdistribution.service.kafka.receiver;

import cn.edu.sicau.pfdistribution.service.kafka.sender.KafkaPfAllocationMessageSender;
import cn.edu.sicau.pfdistribution.service.kspdistribution.MainDistribution;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import scala.Predef;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.JavaConverters$;
import scala.collection.Seq;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//接收命令触发客流分配过程计算
@Component
public class KafkaPfAllocationCmdReceiver {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    MainDistribution distribution;

    @Autowired
    KafkaPfAllocationMessageSender sender;

    private Gson gson = new GsonBuilder().create();

    public static scala.collection.immutable.Map<String, String> toScalaImmutableMap(java.util.Map<String, String> javaMap) {
        final java.util.List<scala.Tuple2<String, String>> list = new java.util.ArrayList<>(javaMap.size());
        for (final java.util.Map.Entry<String, String> entry : javaMap.entrySet()) {
            list.add(scala.Tuple2.apply(entry.getKey(), entry.getValue()));
        }
        final scala.collection.Seq<Tuple2<String, String>> seq = scala.collection.JavaConverters.asScalaBufferConverter(list).asScala().toSeq();
        return (scala.collection.immutable.Map<String, String>) scala.collection.immutable.Map$.MODULE$.apply(seq);
    }

    //topics = "PF-Allocation-CMD"
    @KafkaListener(topics = "mykafka")
    public void processMessage(String msg) {
        Map<String, String> message = gson.fromJson(msg,new TypeToken<Map<String,String>>(){}.getType());
        log.info("从kafka读取数据" + message);
        final java.util.List<scala.Tuple2<String, String>> list = new java.util.ArrayList<>(message.size());
        for (final java.util.Map.Entry<String, String> entry : message.entrySet()) {
            list.add(scala.Tuple2.apply(entry.getKey(), entry.getValue()));
        }
        final scala.collection.Seq<Tuple2<String, String>> seq = scala.collection.JavaConverters.asScalaBufferConverter(list).asScala().toSeq();
        scala.collection.immutable.Map<String, String> abc = (scala.collection.immutable.Map<String, String>) scala.collection.immutable.Map$.MODULE$.apply(seq);
        //distribution.triggerTask(message);
        sender.send("yourkafka",distribution.triggerTask(abc));
    }
}