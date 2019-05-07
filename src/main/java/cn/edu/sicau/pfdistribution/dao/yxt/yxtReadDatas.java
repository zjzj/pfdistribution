package cn.edu.sicau.pfdistribution.dao.yxt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Arrays;

@Repository
public class yxtReadDatas
{

    @Autowired
    private JdbcTemplate jdbcTemplate;  //这个是系统自带的


    //读入运行图基础数据
    public boolean ReadDatas()
    {
        return jdbcTemplate.execute(new ConnectionCallback<Boolean>() {
            public Boolean doInConnection(Connection conn) {
                //车站数据
                boolean result = true;
                result = result && readCz(conn);
                //区间数据
                result = result && readQj(conn);
                //线路数据
                result = result && readXl(conn);
                //特征车次数据
                result = result && readTzcc(conn);
                //换乘车站接续时间数据
                result = result && readHczjxsj(conn);
                //路径说明
                result = result && readLjsm(conn);
                //路径定义
                result = result && readLclj(conn);
                //线路车站字典
                result = result && readXlcz(conn);
                //线路区间字典
                result = result && readXlqj(conn);
                //线路运行种类字典
                result = result && readXlyxzl(conn);
                //区间运行标尺
                result = result && readQjyxzl(conn);
                //列车数据
                result = result && readLctz(conn);
                return result;
            }
        });
    }

    //读入车站信息
    public boolean readCz(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from dic_station";//SQL语句
        int nczh = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                def_cz czUnit = new def_cz();
                czUnit.Initial();
                nczh = rs.getInt("CZ_ID");
                czUnit.czbh = nczh;
                czUnit.czm = rs.getString("CZ_NAME");
                if(czUnit.czm.isEmpty())
                    continue;
                czUnit.czxz =rs.getString("CZ_XZ");
                czUnit.fjkbz = rs.getInt("FJZ_FLAG");
                czUnit.ljm =rs.getString("LJM");
                czUnit.sxdfxs = rs.getInt("CZ_SXDFX");
                czUnit.xxdfxs = rs.getInt("CZ_XXDFX");

                YxtMain.cz[nczh] = czUnit;
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    //上下行转换
    public short getQjsxx(String sxx)
    {
        if(sxx == null)
            return 0;

        if(sxx.equals("下行") == true)
            return 1;
        else if(sxx.equals("上行") == true)
            return 2;
        return 0;
    }

    //闭塞方式转换
    public short getQjbsfs(String bsfs)
    {
        if(bsfs == null)
            return 0;

        if(bsfs.equals("自动闭塞") == true)
            return 1;
        else if(bsfs.equals("半自动闭塞") == true)
            return 2;
        return 0;
    }

    //区间性质转换
    public short getQjxz(String qjxz)
    {
        if(qjxz == null)
            return 0;

        if(qjxz.equals("双线") == true)
            return 1;
        else if(qjxz.equals("单线") == true)
            return 2;
        return 0;
    }

    //读入区间信息
    public boolean readQj(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from dic_section";//SQL语句
        int nqjh = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                def_qj qjUnit = new def_qj();
                qjUnit.Initial();
                nqjh = rs.getInt("QJ_ID");
                qjUnit.qjbh = nqjh;
                if(qjUnit.qjbh<=0||qjUnit.qjbh>YxtMain.qjzs)
                    continue;
                qjUnit.czh1 = rs.getInt("CZ1_ID");
                qjUnit.czm1 = rs.getString("CZ1_NAME");
                qjUnit.czh2 = rs.getInt("CZ2_ID");
                qjUnit.czm2 = rs.getString("CZ2_NAME");
                qjUnit.sxx = getQjsxx(rs.getString("QJ_SXX"));
                qjUnit.qjbs = getQjbsfs(rs.getString("QJ_BSFS"));
                qjUnit.qjxz = getQjxz(rs.getString("QJ_YXXZ"));
                qjUnit.ljm = rs.getString("QJ_LJM");
                qjUnit.qjcd = rs.getDouble("QJ_LENGTH");

                YxtMain.qj[nqjh] = qjUnit;
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    //读入线路信息
    public boolean readXl(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from dic_line";//SQL语句
        int nxlh = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                def_xldmzd xlUnit = new def_xldmzd();
                xlUnit.Initial();
                nxlh = rs.getInt("LINE_ID");
                xlUnit.xlh = nxlh;
                xlUnit.xlm = rs.getString("LINE_NAME");
                if(xlUnit.xlm.isEmpty())
                    continue;
                xlUnit.xlqzm = rs.getString("LINE_START_CZM");
                xlUnit.xlzzm = rs.getString("LINE_END_CZM");
                xlUnit.xlcd = rs.getDouble("LINELENGTH");
                xlUnit.xltype = rs.getString("LINE_TYPE");

                YxtMain.xl[nxlh] = xlUnit;
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    //读入列车信息
    public boolean readLctz(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from base_lctz";//SQL语句
        int nlcxh = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                def_lctz lctzUnit = new def_lctz();
                lctzUnit.Initial();
                nlcxh = rs.getInt("TRAIN_XH");
                lctzUnit.lcxh = nlcxh;
                lctzUnit.cc = rs.getString("TRAIN_CC");
                if(lctzUnit.cc.isEmpty())
                    continue;
                lctzUnit.qzfd = "";
                lctzUnit.dzdd = "";
                lctzUnit.qzh = rs.getInt("START_STA_ID");
                lctzUnit.sfczm = rs.getString("START_STATION");
                lctzUnit.btqzid = rs.getInt("SEC_START_STA_ID");
                lctzUnit.btqz = rs.getString("SECTION_START_STA");
                lctzUnit.zzh = rs.getInt("END_STA_ID");
                lctzUnit.zdczm = rs.getString("END_STATION");
                lctzUnit.btzzid = rs.getInt("SEC_END_STA_ID");
                lctzUnit.btzz = rs.getString("SECTION_END_STA");
                lctzUnit.kxgl = rs.getString("KXGL");

                YxtMain.lctz[nlcxh] = lctzUnit;
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        //读入客货时刻数据
        int i = 0;
        for(i=1;i<=YxtMain.lczs;i++)
        {
            if(YxtMain.lctz[i] == null||
            YxtMain.lctz[i].cc.isEmpty())
                continue;

            readKhsk(conn, i);
        }

        return false;
    }

    //读入客货时刻数据
    public boolean readKhsk(Connection conn, int nlcxh)
    {
        if(nlcxh<=0||nlcxh>YxtMain.lczs)
            return false;

        PreparedStatement pst = null;
        String selectsql = String.format("select * from base_khsk where LCXH = %s order by ID asc",nlcxh);
        int nxh = 0;
        int nczh = 0;
        String svalue = "";

        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                nxh = rs.getInt("XH");
                nczh = rs.getInt("CZ_ID");
                if(nxh<=0||nxh>YxtMain.ljcd)
                    continue;
                if(nczh<=0||nczh>YxtMain.czzs)
                    continue;

                svalue = rs.getString("ARR_TIME");//到达时刻
                YxtMain.lctz[nlcxh].ddsk[nxh] = timeConvert.ConvertTimeToInt(svalue);
                svalue = rs.getString("DEP_TIME");//出发时刻
                YxtMain.lctz[nlcxh].cfsk[nxh] = timeConvert.ConvertTimeToInt(svalue);
                YxtMain.lctz[nlcxh].yyts[nxh] = rs.getShort("YYTS");
                YxtMain.lctz[nlcxh].jsts[nxh] = rs.getShort("JSTS");
                YxtMain.lctz[nlcxh].qjh[nxh] = rs.getInt("QJ_ID");
                YxtMain.lctz[nlcxh].yxzlh[nxh] = rs.getShort("YXZLH");
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    //读入特征车次信息
    public boolean readTzcc(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from dic_tzcc";//SQL语句
        int nlctzh = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                if(nlctzh<=0||nlctzh> YxtMain.lctzs)
                    break;
                def_tzcc tzccUnit = new def_tzcc();
                nlctzh = rs.getInt("TZH");
                tzccUnit.lctzh = nlctzh;
                if(tzccUnit.lctzh<=0||tzccUnit.lctzh>YxtMain.lctzs)
                    continue;
                tzccUnit.qscc = rs.getString("QSCC");
                tzccUnit.zzcc = rs.getString("ZZCC");
                tzccUnit.lctzm = rs.getString("TZM");
                tzccUnit.jc = rs.getString("JC");

                YxtMain.tzcc[nlctzh] = tzccUnit;
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    //读入换乘站接续时间信息
    public boolean readHczjxsj(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from dic_hczjxsj";//SQL语句
        int nxh = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                 nxh = rs.getInt("XH");
                 if(nxh<=0||nxh>YxtMain.hcczs)
                     continue;
                 def_hcczzd hcczUnit = new def_hcczzd();
                 hcczUnit.hcxh = nxh;
                 hcczUnit.outczm = rs.getString("OUT_CZM");
                 hcczUnit.outxlm = rs.getString("OUT_XLM");
                 hcczUnit.outfxm = rs.getString("OUT_FX");
                 hcczUnit.inczm = rs.getString("IN_CZM");
                 hcczUnit.inxlm = rs.getString("IN_XLM");
                 hcczUnit.infxm = rs.getString("IN_FX");
                 hcczUnit.hcjxsj = rs.getInt("HCJXSJ");

                 YxtMain.hcczzd[nxh] = hcczUnit;
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    //读入路径说明
    public boolean readLjsm(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from dic_jlsm";//SQL语句
        int nljxh = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                nljxh = rs.getInt("JL_ID");
                if(nljxh<=0||nljxh>YxtMain.lcljs)
                    continue;

                def_ljsm ljsmUnit = new def_ljsm();
                ljsmUnit.ljh = nljxh;
                ljsmUnit.ljsm = rs.getString("JLSM");
                ljsmUnit.jlqzm = rs.getString("JL_QZM");
                ljsmUnit.jlzzm = rs.getString("JL_ZZM");
                ljsmUnit.dxjlh = rs.getInt("JL_DXID");

                YxtMain.ljsm[nljxh] = ljsmUnit;
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    //读入列车路径
    public boolean readLclj(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from dic_lcjl order by JL_ID,XH asc";//SQL语句
        int nljbh = 0;
        int nxh = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                nljbh = rs.getInt("JL_ID");
                if(nljbh<=0||nljbh>YxtMain.lcljs)
                    continue;
                nxh = rs.getInt("XH");
                if(nxh<=0||nxh>YxtMain.ljcd)
                    continue;

                def_lclj lcljUnit = new def_lclj();
                lcljUnit.ljbh = nljbh;
                lcljUnit.ljxh = nxh;
                lcljUnit.qjh = rs.getInt("QJ_ID");
                lcljUnit.qjqzm = rs.getString("QJ_QZM");
                lcljUnit.qjzzm = rs.getString("QJ_ZZM");

                YxtMain.lclj[nljbh][nxh] = lcljUnit;

            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    //读入线路车站字典
    public boolean readXlcz(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from dic_linestation order by LINE_ID,CZ_SN asc";//SQL语句
        int nxlh = 0,nczsn = 0,nczh  = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                nxlh = rs.getInt("LINE_ID");
                nczsn = rs.getInt("CZ_SN");
                nczh = rs.getInt("CZ_ID");

                if(nxlh<=0||nxlh>YxtMain.xls)
                    continue;
                if(nczsn<=0||nczsn>YxtMain.xlczs)
                    continue;
                if(nczh<=0||nczh>YxtMain.czzs)
                    continue;

                YxtMain.xlczzd[nxlh][nczsn] = nczh;
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    //读入线路区间字典
    public boolean readXlqj(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from dic_linesection order by LINE_ID,QJ_SN asc";//SQL语句
        int nxlh = 0,nqjsn = 0,nqjh  = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                nxlh = rs.getInt("LINE_ID");
                nqjsn = rs.getInt("QJ_SN");
                nqjh = rs.getInt("QJ_ID");

                if(nxlh<=0||nxlh>YxtMain.xls)
                    continue;
                if(nqjsn<=0||nqjsn>YxtMain.xlqjs)
                    continue;
                if(nqjh<=0||nqjh>YxtMain.qjzs)
                    continue;

                YxtMain.xlqjzd[nxlh][nqjsn] = nqjh;
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    //读入线路运行种类字典
    public boolean readXlyxzl(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from dic_yxzl order by RUN_TYPE_ID,LINE_ID asc";//SQL语句
        short nyxzlh = 0;
        int   nxlh = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                nyxzlh = rs.getShort("RUN_TYPE_ID");
                nxlh = rs.getInt("LINE_ID");
                if(nyxzlh<=0||nyxzlh>YxtMain.yxzls)
                    continue;
                if(nxlh<=0||nxlh>YxtMain.xls)
                    continue;

                def_xlyxzl xlyxzlUnit = new def_xlyxzl();
                xlyxzlUnit.yxzlh = nyxzlh;
                xlyxzlUnit.xlh = nxlh;
                xlyxzlUnit.yxzl = rs.getString("RUN_TYPE");
                xlyxzlUnit.xlm = rs.getString("LINE_NAME");
                xlyxzlUnit.jx = rs.getString("LOCO_TYPE");
                xlyxzlUnit.qyds = Double.parseDouble(rs.getString("WEIGHT"));

                YxtMain.xlyxzl[nxlh][nyxzlh] = xlyxzlUnit;
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    //读入区间运行种类字典
    public boolean readQjyxzl(Connection conn)
    {
        PreparedStatement pst = null;
        String selectsql = "select * from dic_yxzlsf order by RUN_TYPE_ID,LINE_ID asc";//SQL语句
        int nqjh = 0,nfxqjh = 0;
        short nyxzlh = 0;
        try {
            pst = conn.prepareStatement(selectsql);//准备执行语句
            ResultSet rs = pst.executeQuery();//执行语句，得到结果集

            while (rs.next())
            {
                nyxzlh = rs.getShort("RUN_TYPE_ID");
                nqjh = rs.getInt("QJ_ID");
                if(nqjh<=0||nqjh>YxtMain.qjzs)
                    continue;
                if(nyxzlh<=0||nyxzlh>YxtMain.yxzls)
                    continue;

                def_yxzlzd yxzlzdUnit = new def_yxzlzd();
                yxzlzdUnit.yxzlh = nyxzlh;
                yxzlzdUnit.xlh = rs.getInt("LINE_ID");
                yxzlzdUnit.xlm = rs.getString("LINE_NAME");
                yxzlzdUnit.qjh = nqjh;
                //下行
                if(nqjh%2==1)
                {
                    //下行区间
                    yxzlzdUnit.yxsj = rs.getShort("XXYXSJ");
                    yxzlzdUnit.qcfj = rs.getShort("XXQDSJ");
                    yxzlzdUnit.tcfj = rs.getShort("XXTZSJ");
                    //区间运行种类
                    YxtMain.yxzlzd[nqjh][nyxzlh] = yxzlzdUnit;

                    //上行区间
                    nfxqjh = nqjh +1;
                    yxzlzdUnit.qjh = nfxqjh;
                    yxzlzdUnit.yxsj = rs.getShort("SXYXSJ");
                    yxzlzdUnit.qcfj = rs.getShort("SXQDSJ");
                    yxzlzdUnit.tcfj = rs.getShort("SXTZSJ");
                    //区间运行种类
                    YxtMain.yxzlzd[nfxqjh][nyxzlh] = yxzlzdUnit;
                }
                else//上行
                {
                    //上行区间
                    yxzlzdUnit.yxsj = rs.getShort("SXYXSJ");
                    yxzlzdUnit.qcfj = rs.getShort("SXQDSJ");
                    yxzlzdUnit.tcfj = rs.getShort("SXTZSJ");
                    //区间运行种类
                    YxtMain.yxzlzd[nqjh][nyxzlh] = yxzlzdUnit;

                    //下行区间
                    nfxqjh = nqjh-1;
                    yxzlzdUnit.qjh = nfxqjh;
                    yxzlzdUnit.yxsj = rs.getShort("XXYXSJ");
                    yxzlzdUnit.qcfj = rs.getShort("XXQDSJ");
                    yxzlzdUnit.tcfj = rs.getShort("XXTZSJ");
                    //区间运行种类
                    YxtMain.yxzlzd[nfxqjh][nyxzlh] = yxzlzdUnit;
                }
            }
            rs.close();
            pst.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }
}
