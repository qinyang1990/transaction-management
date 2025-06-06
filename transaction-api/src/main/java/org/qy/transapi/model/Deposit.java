package org.qy.transapi.model;

import java.math.BigDecimal;

/**
 *
 * rpc层存入交易的参数
 *
 * @author qinyang
 * @date 2025/6/4 17:12
 */
public class Deposit extends TransInfo{

    String accountNo;
    BigDecimal amount;
    // 币种 如人民币 美元
    String currency;

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
}
