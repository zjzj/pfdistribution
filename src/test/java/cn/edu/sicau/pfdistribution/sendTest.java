package cn.edu.sicau.pfdistribution;

import cn.edu.sicau.pfdistribution.config.ConfigJDBC;
import cn.edu.sicau.pfdistribution.controller.KafkaTriggerController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class sendTest {

  @Autowired
  private ConfigJDBC dataSource;//创建jdbctemplate对象，并使用spring的自动注入完成实例化

  @Autowired
  public KafkaTriggerController test;

  static final Logger logger = LoggerFactory.getLogger(sendTest.class);

  @Test
  public void sendTest() {

    test.trigger("dynamic","2018-09-01 09:00:19","60","15");

  }
}
