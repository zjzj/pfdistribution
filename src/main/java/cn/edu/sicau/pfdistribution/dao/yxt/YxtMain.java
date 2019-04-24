package cn.edu.sicau.pfdistribution.dao.yxt;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Arrays;

@Component
public class YxtMain implements InitializingBean
{
    @Autowired
    yxtReadDatas yxtRead;

    //车站总数
    public  static final int czzs = 2500;
    //区间总数
    public static final int qjzs = 5000;
    //线路总数
    public  static final int xls = 500;
    //列车总数
    public  static final int lczs = 5000;
    //线路区间数
    public  static final int xlqjs = 500;
    //线路车站数
    public  static final int xlczs = 500;
    //运行种类数
    public  static final int yxzls = 60;
    //慢行种类数
    public  static final int mxzls = 60;
    //特征数
    public  static final int lctzs = 600;
    //换乘车站数
    public  static final int hcczs = 100;
    //列车路径数
    public static final int lcljs = 100;
    //列车路径长度
    public  static final int ljcd = 100;

    //车站数组
    public static def_cz[]cz = new def_cz[czzs+1];
    //区间数组
    public static def_qj []qj = new def_qj[qjzs+1];
    //线路
    public static def_xldmzd []xl = new def_xldmzd[xls+1];
    //列车+客货时刻
    public static def_lctz []lctz = new def_lctz[lczs+1];
    //特征车次
    public static def_tzcc []tzcc = new def_tzcc[lctzs+1];
    //换乘车站字典
    public static def_hcczzd []hcczzd = new def_hcczzd[hcczs+1];
    //列车路径说明
    public static def_ljsm []ljsm = new def_ljsm[lcljs+1];
    //列车路径定义
    public static def_lclj [][]lclj = new def_lclj[lcljs+1][ljcd+1];
    //线路运行种类字典
    public static def_xlyxzl[][]xlyxzl = new def_xlyxzl[xls+1][yxzls+1];
    //运行种类字典
    public static def_yxzlzd [][]yxzlzd = new def_yxzlzd[qjzs+1][yxzls+1];
    //线路车站
    public static int [][]xlqjzd = new int[xls+1][xlczs+1];
    //线路区间
    public static int [][]xlczzd = new int[xls+1][xlqjs+1];

    //根据车站号获取区间号
    public int GetQjhByCzh(int nczh1,int nczh2)
    {
        int nqjh = 0;
        int i = 0;
        if(nczh1<=0||nczh1>czzs)
            return 0;
        if(nczh2<=0||nczh2>czzs)
            return 0;
        String czm1 = cz[nczh1].czm;
        String czm2 = cz[nczh2].czm;

        for(i=1;i<=qjzs;i++)
        {
            if(qj[i].czh1==nczh1&&
            qj[i].czh2==nczh2)
            {
                nqjh = i;
                break;
            }
        }

        return nqjh;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!yxtRead.ReadDatas())
        {
            throw new Exception("YXT-Data init failed!");
        }
    }
}
