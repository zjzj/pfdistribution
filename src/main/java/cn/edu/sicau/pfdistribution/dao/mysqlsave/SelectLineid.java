package cn.edu.sicau.pfdistribution.dao.mysqlsave;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SelectLineid {
    public BigDecimal LINE_ID;
    public String LINE_NAME;
    public BigDecimal CZ_SN;
    public BigDecimal CZ_ID;
    public String CZ_NAME;
}
