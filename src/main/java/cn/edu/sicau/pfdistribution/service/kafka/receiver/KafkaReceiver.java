package cn.edu.sicau.pfdistribution.service.kafka.receiver;
import cn.edu.sicau.pfdistribution.SparkApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class KafkaReceiver {

    @KafkaListener(topics = "mykafka")
    public void processMessage(String msg) {
        String str = "static";

        System.out.println("从kafka读取数据" + msg);

    }

}