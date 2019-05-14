package  cn.edu.sicau.pfdistribution;

import cn.edu.sicau.pfdistribution.dao.yxt.YxtCalcInterface;
import cn.edu.sicau.pfdistribution.service.DoWork;
import cn.edu.sicau.pfdistribution.service.kafka.receiver.KafkaReceiver;
import cn.edu.sicau.pfdistribution.service.kspdistribution.calculateBase;
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
public class SparkApp implements CommandLineRunner {

  @Autowired
  DoWork doWork;

  @Autowired
  YxtCalcInterface yxtCalcInterface;

  @Autowired
  KafkaReceiver kafkaReceiver;

  @Autowired
  calculateBase calBase;

  public void main(String[] args){
    SpringApplication app = new SpringApplication(SparkApp.class);
    app.setBannerMode(Banner.Mode.OFF);
    app.run(args);
  }


  @Override
  public void run(String... args) throws Exception {
      doWork.work();
//    System.out.println(userDao.getUserById(1));
  }
}
