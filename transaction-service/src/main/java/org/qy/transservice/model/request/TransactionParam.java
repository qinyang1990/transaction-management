package org.qy.transservice.model.request;

import org.qy.transapi.model.TransInfo;
import org.qy.transservice.model.TransType;

import java.math.BigDecimal;

/**
 * 创建交易的接入参数层模型
 *
 * @author qinyang
 * @date 2025/6/5 16:21
 */
public class TransactionParam extends TransInfo {

    TransType transType;
    String accountNo;
    BigDecimal amount;
    String currency;

    String toAccountNo;


    public TransType getTransType() {
        return transType;
    }

    public void setTransType(TransType transType) {
        this.transType = transType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getToAccountNo() {
        return toAccountNo;
    }

    public void setToAccountNo(String toAccountNo) {
        this.toAccountNo = toAccountNo;
    }
}
