
package  cn.edu.sicau.pfdistribution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootApplication
@EnableAsync
public class PfAllocationApp {
    public static void main(String[] args) {
      ConfigurableApplicationContext context = SpringApplication.run(PfAllocationApp.class, args);
    }
    //主类
}
