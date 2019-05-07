package cn.edu.sicau.pfdistribution.service.kafka.receiver;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class KafkaReceiver {

    @KafkaListener(topics = "mykafka")
    public void processMessage(String msg) {

        System.out.println("从kafka读取数据" + msg);

                }
                }