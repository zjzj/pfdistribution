package  cn.edu.sicau.pfdistribution;

import cn.edu.sicau.pfdistribution.dao.yxt.YxtCalcInterface;
import cn.edu.sicau.pfdistribution.service.kafka.receiver.KafkaReceiver;
import cn.edu.sicau.pfdistribution.service.kspdistribution.CalculateBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author Zj
 * @date 2019/4/17 7:55
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootApplication
public class PfAllocationApp implements CommandLineRunner {

  @Autowired
  YxtCalcInterface yxtCalcInterface;

  @Autowired
  KafkaReceiver kafkaReceiver;

  @Autowired
  CalculateBase calBase;

  public void main(String[] args){
    SpringApplication app = new SpringApplication(PfAllocationApp.class);
    app.setBannerMode(Banner.Mode.OFF);
    app.run(args);
  }


  @Override
  public void run(String... args) throws Exception {

//    System.out.println(userDao.getUserById(1));
  }
}
