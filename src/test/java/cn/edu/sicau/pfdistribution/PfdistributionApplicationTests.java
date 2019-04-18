package cn.edu.sicau.pfdistribution;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PfdistributionApplicationTests {

  @Autowired
  private JdbcTemplate jdbcTemplate;//创建jdbctemplate对象，并使用spring的自动注入完成实例化

  @Test
  public void updateTest() {
    System.out.println("hello");

  }
}
