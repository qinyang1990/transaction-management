package org.qy.transapi.api;

import org.qy.transapi.model.Deposit;
import org.qy.transapi.model.Withdraw;
import org.qy.transsdk.exception.RpcException;

/**
 *
 * 底层核心rpc接口 只提供存/取两个原子接口
 *
 * @author qinyang
 * @date 2025/6/4 17:32
 */
public interface ICoreTransaction {

    Boolean withdraw(Withdraw draw, Boolean transfer, Boolean recordHist) throws RpcException;

    Boolean deposit(Deposit deposit, Boolean transfer, Boolean recordHist) throws RpcException;
}
