package org.qy.transservice.model.request;

import java.math.BigDecimal;

/**
 * 更新交易的接入参数层模型
 *
 * @author qinyang
 * @date 2025/6/5 16:49
 */
public class TransactionUpdateParam  {


    String transId;
    // 更新为多钱,差值需要和之前的交易做减法
    BigDecimal updAmount;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public BigDecimal getUpdAmount() {
        return updAmount;
    }

    public void setUpdAmount(BigDecimal updAmount) {
        this.updAmount = updAmount;
    }
}
