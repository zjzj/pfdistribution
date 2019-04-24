package cn.edu.sicau.pfdistribution.dao.yxt;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YxtTest {

    @Autowired
    private YxtMain yxtMain;

    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

    @Test
    public void testYxtMain() {
        //显示已读数据
        int i = 0;
        for (i = 1; i < yxtMain.czzs; i++) {
            if (yxtMain.cz[i] == null)
                continue;
            System.out.println(yxtMain.cz[i].czm);
        }
    }

}
