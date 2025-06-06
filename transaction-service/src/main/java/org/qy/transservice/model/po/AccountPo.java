package org.qy.transservice.model.po;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 账户持久层模型
 *
 * @author qinyang
 * @date 2025/6/5 16:47
 */
@TableName(value = "ACCOUNT")
public class AccountPo {

    int id;
    String name;
    // 账户状态
    int status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
}
