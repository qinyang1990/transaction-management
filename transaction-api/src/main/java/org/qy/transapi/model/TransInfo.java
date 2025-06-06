package org.qy.transapi.model;

/**
 *
 * 交易中的公共信息
 *
 * @author qinyang
 * @date 2025/6/4 17:08
 */
public class TransInfo {

    // 交易流水号 每笔交易全局唯一
    String transId;

    // 操作人员
    String userId;
    String branchId;
    // 客户号
    String clientId;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
