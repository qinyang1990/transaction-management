package org.qy.transservice.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import org.qy.transservice.model.TransType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易流水持久层模型
 *
 * @author qinyang
 * @date 2025/6/5 15:07
 */
@TableName(value = "HIST")
public class HistPo {

    String transId;
    String accountId;
    // 当转账时有值,存收款方账号
    String toAccountId;
    TransType transType;
    BigDecimal amount;
    // 币种
    String ccy;
    Date transTime;
    String clientId;
    String mark;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public TransType getTransType() {
        return transType;
    }

    public void setTransType(TransType transType) {
        this.transType = transType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
