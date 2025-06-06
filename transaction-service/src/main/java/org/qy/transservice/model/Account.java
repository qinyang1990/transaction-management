package org.qy.transservice.model;

import java.util.Date;

/**
 * 业务层账户模型
 *
 * @author qinyang
 * @date 2025/6/5 19:21
 */
public class Account {

    int id;
    String name;
    AccountStatus status;
    Date openTime;
    String clientId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public enum AccountStatus {
        Normal,
        Frozen,
        Close
    }

}
