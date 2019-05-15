package cn.edu.sicau.pfdistribution;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;

public class orclTest {
    private static final String DBDRIVER = "oracle.jdbc.driver.OracleDriver";

    private static final String DBURL = "jdbc:oracle:thin:@localhost:1521:myorcl";

    private static final String DBUSER = "system";

    private static final String PASSWORD = "password";

    public static void main(String[] args) throws Exception {

        Connection conn = null; // 每一个Connection对象都表示一个连接

        Class.forName(DBDRIVER); // 加载数据库驱动程序

        conn = DriverManager.getConnection(DBURL, DBUSER, PASSWORD); // 连接数据库

        String deleteNotcare = "delete   \n" +
                "from \"SCOTT\".\"ceshi_copy1\"\n" +
                "WHERE \"票卡种类\" not like '%票%' and \"票卡种类\" not like '%卡%'\n";

        String distinct="select DISTINCT*   \n" +
                "from \"SCOTT\".\"ceshi_copy1\"";

        String creTestod="CREATE TABLE \"SCOTT\".\"test_od\" (   \n" +
                "  \"票卡号\" CHAR(20) ,\n" +
                "  \"进站点\" CHAR(4) ,\n" +
                "  \"进站时间\" TIMESTAMP ,\n" +
                "  \"出站点\" CHAR(4) ,\n" +
                "  \"出站时间\" TIMESTAMP \n" +
                ")";

        String creTestin="CREATE TABLE \"SCOTT\".\"test_in\" (   \n" +
                "  \"票卡号\" CHAR(20) ,\n" +
                "  \"进站点\" CHAR(4) ,\n" +
                "  \"进站时间\" TIMESTAMP \n" +
                ")";

        String creTestout="CREATE TABLE \"SCOTT\".\"test_out\" (   \n" +
                "  \"票卡号\" CHAR(20) ,\n" +
                "  \"出站点\" CHAR(4) ,\n" +
                "  \"出站时间\" TIMESTAMP \n" +
                ")";

        String insertIn="insert into \"SCOTT\".\"test_in\"(\"票卡号\",\"进站点\",\"进站时间\")  \n" +
                "select \"票卡号\",\"交易车站\",\"数据接收时间\"\n" +
                "from \"SCOTT\".\"ceshi_copy1\"\n" +
                "where \"交易事件\"='进站'";

        String insertOut="insert into \"SCOTT\".\"test_out\"(\"票卡号\",\"出站点\",\"出站时间\")  \n" +
                "select \"票卡号\",\"交易车站\",\"数据接收时间\"\n" +
                "from \"SCOTT\".\"ceshi_copy1\"\n" +
                "where \"交易事件\"='出站'";

        String insertOd="insert into \"SCOTT\".\"test_od\"        --查找OD数据并插入到OD结果数据表\n" +
                "SELECT \"SCOTT\".\"test_in\".\"票卡号\",\"SCOTT\".\"test_in\".\"进站点\",\"SCOTT\".\"test_in\".\"进站时间\",\"SCOTT\".\"test_out\".\"出站点\",\"SCOTT\".\"test_out\".\"出站时间\"\n" +
                "FROM \"SCOTT\".\"test_in\"\n" +
                "INNER JOIN \"SCOTT\".\"test_out\"\n" +
                "ON \"SCOTT\".\"test_in\".\"票卡号\"=\"SCOTT\".\"test_out\".\"票卡号\"\n" +
                "ORDER BY \"SCOTT\".\"test_in\".\"进站时间\" ASC";

        String deleteNot="delete \n" +
                "from \"SCOTT\".\"test_od\"\n" +
                "where TO_CHAR(\"出站时间\")<=TO_CHAR(\"进站时间\") ";
        Statement sql = conn.createStatement();
        sql.executeUpdate(deleteNotcare);
        sql.executeUpdate(distinct);
        sql.executeUpdate(creTestod);
        sql.executeUpdate(creTestin);
        sql.executeUpdate(creTestout);
        sql.executeUpdate(insertIn);
        sql.executeUpdate(insertOut);
        sql.executeUpdate(insertOd);
        sql.executeUpdate(deleteNot);

        System.out.println(conn);   // 如果不为null表示已连接

        conn.close() ;

    }
}
