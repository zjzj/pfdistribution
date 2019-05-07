package cn.edu.sicau.pfdistribution.dao.yxt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class yxtBaseCalc implements YxtCalcInterface
{
    @Autowired
    YxtMain yxtMain;

    //获取两站间里程
    public double GetDistanceOfTwoStation(int czid1,int czid2)
    {
        double ddis = 0;
        int nqjh = yxtMain.GetQjhByCzh(czid1,czid2);
        if(nqjh<=0||nqjh>yxtMain.qjzs)
            return ddis;
        ddis = yxtMain.qj[nqjh].qjcd;
        return ddis;
    }
    //获取某区间里程
    public double GetDistanceOfSection(int qjid)
    {
        double ddis = 0;
        if(qjid<=0||qjid>yxtMain.qjzs)
            return ddis;
        ddis = yxtMain.qj[qjid].qjcd;
        return ddis;
    }

    //计算2站间运行时分
    public int getRunTimesOfTwoStation(int czid1,int czid2,int lczlh)
    {
        int nqjh = yxtMain.GetQjhByCzh(czid1,czid2);
        return getRunTimesOfSection(nqjh,lczlh);
    }
    //计算某区间运行时分
    public int getRunTimesOfSection(int qjid,int lczlh)
    {
        int qjyxsf = 0;
        if(qjid<=0||qjid>yxtMain.qjzs)
            return 0;
        if(lczlh>0&&lczlh<=yxtMain.yxzls)
        {
            qjyxsf = yxtMain.yxzlzd[qjid][lczlh].yxsj;
        }
        else
        {
            qjyxsf = yxtMain.yxzlzd[qjid][1].yxsj;
        }
        return qjyxsf;
    }

    //计算2站间行车量
    public int GetTrainNumsOfTwoStation(int czid1,int czid2,int lctzh)
    {
        int nqjh = yxtMain.GetQjhByCzh(czid1,czid2);
        int[] retvalues = GetTrainsOfSection(nqjh,lctzh);
        return retvalues.length;
    }
    //获取2站间列车序号
    public int[] GetTrainsOfTowStation(int czid1,int czid2,int lctzh)
    {
        int nqjh = yxtMain.GetQjhByCzh(czid1,czid2);

        int[] retvalues = GetTrainsOfSection(nqjh,lctzh);

        return retvalues;
    }
    //计算某区间行车量
    public int GetTrainNumsOfSection(int qjid,int lctzh)
    {
        int []retvalues = GetTrainsOfSection(qjid,lctzh);
        return retvalues.length;
    }
    //获取某区间列车序号
    public int[] GetTrainsOfSection(int qjid,int lctzh)
    {
        if(qjid<=0||qjid>yxtMain.qjzs)
        {
            return null;
        }
        int i = 0,j = 0;
        int []ninqj = new int[yxtMain.lczs+1];
        for(i=0;i<=yxtMain.lczs;i++)
            ninqj[i] = 0;

        boolean bAdded = false;
        int nsize = 0;
        for(i=1;i<=yxtMain.lczs;i++)
        {
            bAdded = false;
            if(yxtMain.lctz[i]==null||
                    yxtMain.lctz[i].cc==null||
                    yxtMain.lctz[i].cc.isEmpty())
                continue;

            for(j=1;j<=yxtMain.ljcd;j++)
            {
                if(yxtMain.lctz[i].qjh[j] != qjid)
                    continue;
                //add lcxh
                bAdded = true;
                ninqj[i] = 1;
                nsize += 1;
                break;
            }
            if(bAdded)
                continue;
        }
        int[] retvalues = new int[nsize];
        j = 0;
        for(i=1;i<=yxtMain.lczs;i++)
        {
            if(ninqj[i]<=0)
                continue;
            retvalues[j++] = i;
        }
        return retvalues;
    }

    //判断某站是否为换乘站
    public boolean CheckStationIsHcz(int czid)
    {
        int i = 0;
        if(czid<=0||czid>yxtMain.czzs)
            return false;
        String czm = yxtMain.cz[czid].czm;
        for(i=1;i<=yxtMain.hcczs;i++)
        {
            if(yxtMain.hcczzd[i].outczm.equals(czm)==true||
                    yxtMain.hcczzd[i].inczm.equals(czm)==true)
                return true;
        }
        return false;
    }
    //获取两站间换乘时间，秒
    public int   GetJxTimeOfTowStation(int czid1,String infxm,int czid2,String outfxm)
    {
        if(czid1<=0||czid1>yxtMain.czzs)
            return 0;
        if(czid2<=0||czid2>yxtMain.czzs)
            return 0;
        String czm1 = yxtMain.cz[czid1].czm;
        String czm2 = yxtMain.cz[czid2].czm;
        int i = 0;
        int hcjxsj = 0;
        for(i=1;i<=yxtMain.hcczs;i++)
        {
            if(yxtMain.hcczzd[i].outczm.equals(czm1)==true&&
                    yxtMain.hcczzd[i].inczm.equals(czm2)==true&&
                    yxtMain.hcczzd[i].infxm.equals(infxm)==true&&
                    yxtMain.hcczzd[i].outfxm.equals(outfxm)==true)
            {
                hcjxsj = yxtMain.hcczzd[i].hcjxsj;
                break;
            }
        }
        return hcjxsj;
    }

    //获取某列车在某站停站时间
    public short GetTrainStopTime(int nlcxh,int nczh)
    {
        if(nlcxh<=0||nlcxh>yxtMain.lczs)
            return 0;
        if(nczh<=0||nlcxh>yxtMain.czzs)
            return 0;
        int i = 0;
        int nqjh = 0;
        short nstoptime = 0;
        for(i=1;i<=yxtMain.ljcd;i++)
        {
            nqjh = yxtMain.lctz[nlcxh].qjh[i];
            if(nqjh<=0||nqjh>yxtMain.qjzs)
                continue;
            if(yxtMain.qj[nqjh].czh1==nczh)
            {
                nstoptime = (short)(yxtMain.lctz[nlcxh].cfsk[i] - yxtMain.lctz[nlcxh].ddsk[i]);
                break;
            }
            if(yxtMain.qj[nqjh].czh2==nczh)
            {
                nstoptime = (short)(yxtMain.lctz[nlcxh].cfsk[i+1] - yxtMain.lctz[nlcxh].ddsk[i+1]);
                break;
            }
        }

        return nstoptime;
    }
}
