package cn.edu.sicau.pfdistribution.dao.yxt;

public interface yxtCalcInterface
{
    //获取两站间里程
    public double GetDistanceOfTwoStation(int czid1,int czid2);
    //获取某区间里程
    public double GetDistanceOfSection(int qjid);

    //计算2站间运行时分
    public int getRunTimesOfTwoStation(int czid1,int czid2,int lczlh);
    //计算某区间运行时分
    public int getRunTimesOfSection(int qjid,int lczlh);

    //计算2站间行车量
    public int GetTrainNumsOfTwoStation(int czid1,int czid2,int lctzh);
    //获取2站间列车序号
    public int[] GetTrainsOfTowStation(int czid1,int czid2,int lctzh);
    //计算某区间行车量
    public int GetTrainNumsOfSection(int qjid,int lctzh);
    //获取某区间列车序号
    public int[] GetTrainsOfSection(int qjid,int lctzh);

    //判断某站是否为换乘站
    public boolean CheckStationIsHcz(int czid);
    //获取两站间换乘时间，秒
    public int   GetJxTimeOfTowStation(int czid1,String infxm,int czid2,String outfxm);

    //获取某列车在某站停站时间
    public short GetTrainStopTime(int nlcxh,int nczh);
}
