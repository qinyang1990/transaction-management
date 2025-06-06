package org.qy.transservice.model.po;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 余额持久层模型
 *
 * @author qinyang
 * @date 2025/6/5 16:55
 */
@TableName(value = "BALANCE")
public class BalancePo {

    int accountId;
    // 余额 总是为正
    BigDecimal bal;
    // 币种 如人民币 美元
    String ccy;
    // 余额变更时间
    Date changeTime;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }
}
