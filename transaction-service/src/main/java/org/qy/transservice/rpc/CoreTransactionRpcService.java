package org.qy.transservice.rpc;

import org.qy.transapi.api.ICoreTransaction;
import org.qy.transapi.model.Deposit;
import org.qy.transapi.model.Withdraw;
import org.qy.transsdk.exception.RpcException;
import org.qy.transservice.model.Hist;
import org.qy.transservice.model.TransType;
import org.qy.transservice.service.BalanceService;
import org.qy.transservice.service.HistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *
 * 底层核心rpc接口
 * 交易先经过检查 一系列指标及处理后 才会调到此层
 *
 * @author qinyang
 * @date 2025/6/5 21:22
 */
@Component
public class CoreTransactionRpcService implements ICoreTransaction {

    @Autowired
    BalanceService balanceService;

    @Autowired
    HistService histService;


    /**
     * 支取
     * 目前主要操作余额及交易流水
     *
     * @param draw
     * @param transfer 是否为转账(转账是对存入和支取的拼装)
     * @param recordHist 是否记录交易流水
     * @return
     * @throws RpcException
     */
    @Override
    public Boolean withdraw(Withdraw draw, Boolean transfer, Boolean recordHist) throws RpcException {

        balanceService.changeBalance(draw.getAccountNo(), TransType.Withdraw, draw.getAmount());

        if(recordHist){
            Hist hist = new Hist();
            hist.setTransId(draw.getTransId());
            hist.setAccountId(draw.getAccountNo());
            hist.setAmount(draw.getAmount());
            hist.setCcy(draw.getCurrency());
            hist.setTransTime(new Date());
            hist.setClientId(draw.getClientId());

            if(transfer){
                hist.setMark("转账支取");
                hist.setTransType(TransType.Transfer);
            }else {
                hist.setMark("支取");
                hist.setTransType(TransType.Withdraw);
            }
            histService.record(hist);
        }


        return true;

    }

    /**
     * 存入
     * 目前主要操作余额及交易流水
     *
     * @param deposit
     * @param transfer 是否为转账(转账是对存入和支取的拼装)
     * @param recordHist 是否记录交易流水
     * @return
     * @throws RpcException
     */
    @Override
    public Boolean deposit(Deposit deposit, Boolean transfer, Boolean recordHist) throws RpcException {

        balanceService.changeBalance(deposit.getAccountNo(), TransType.Deposit, deposit.getAmount());

        if(recordHist){
            Hist hist = new Hist();
            hist.setTransId(deposit.getTransId());
            hist.setAccountId(deposit.getAccountNo());
            hist.setAmount(deposit.getAmount());
            hist.setCcy(deposit.getCurrency());
            hist.setTransTime(new Date());
            hist.setClientId(deposit.getClientId());

            if(transfer){
                hist.setMark("转账存入");
                hist.setTransType(TransType.Transfer);
                hist.setToAccountId(deposit.getAccountNo());
                hist.setAccountId(null);
            }else {
                hist.setMark("存入");
                hist.setTransType(TransType.Deposit);
            }
            histService.record(hist);
        }

        return true;
    }
}
