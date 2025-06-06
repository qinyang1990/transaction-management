package org.qy.transservice.service;

import org.qy.transapi.model.Deposit;
import org.qy.transapi.model.Withdraw;
import org.qy.transsdk.common.ResultCodeEnum;
import org.qy.transsdk.exception.BusinessException;
import org.qy.transservice.annotation.AloneTransactional;
import org.qy.transservice.annotation.DistributedTransactional;
import org.qy.transservice.model.Hist;
import org.qy.transservice.model.IModelConverter;
import org.qy.transservice.model.TransType;
import org.qy.transservice.model.request.TransactionParam;
import org.qy.transservice.model.request.TransactionUpdateParam;
import org.qy.transservice.rpc.CoreTransactionRpcService;
import org.qy.transservice.service.component.IChecker;
import org.qy.transservice.service.component.ParamValidtor;
import org.qy.transservice.util.ConfigCentre;
import org.qy.transservice.util.SpringContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 *
 * 整体交易服务
 *
 * @author qinyang
 * @date 2025/6/5 22:19
 */
@Service
public class TransactionService implements ApplicationRunner, IModelConverter {

    @Autowired
    ParamValidtor paramValidtor;

    @Autowired
    ConfigCentre configCentre;

    // 为true时 以银行方式做， 修改、删除 其实是发起反向的第二笔交易
    // 为false时 以普遍情况做， 直接修改或删除原始记录
    boolean transModifyMode;

    private final String transModfiyModeKey = "trans.modify.mode";
    private final String transModfiyModeDefaultVal = "true";

    @Autowired
    CoreTransactionRpcService transactionRpcService;

    @Autowired
    HistService histService;

    // 指标检查器
    Collection<IChecker> checkers;

    // 更新时的新流水号后缀
    public final String updateTransIdSuffix = "-U";
    // 删除时的新流水号后缀
    public final String deleteTransIdSuffix = "-B";


    /**
     * 发起交易(存入/支取/转账)
     *
     * @param param
     * @return
     * @throws BusinessException
     */
    public String createTransaction(TransactionParam param) throws BusinessException {

        // 简单验证参数
        paramValidtor.validateTransaction(param);

        // 业务指标检查
        for (IChecker checker : checkers) {
            if (checker.matches(param)) {
                checker.check(param);
            }
        }

        // 调用对应方法
        if (TransType.Deposit.equals(param.getTransType())) {
            return doDeposit(param);
        } else if (TransType.Withdraw.equals(param.getTransType())) {
            return doWithdraw(param);
        } else if (TransType.Transfer.equals(param.getTransType())) {
            return doTransfer(param);
        }

        return "";
    }


    /**
     * 更新交易
     *
     * 计算出与原交易的差额，重新发起新的交易
     *
     * @param param
     * @return
     * @throws BusinessException
     */
    public boolean updateTransaction(TransactionUpdateParam param) throws BusinessException {
        if (transModifyMode) {
            List<Hist> hists = histService.listHist(param.getTransId(),null,null,null, 1, 2);
            if (CollectionUtils.isEmpty(hists)) {
                throw new BusinessException(ResultCodeEnum.parameter_transid);
            }

            if (param.getUpdAmount().signum() < 1) {
                throw new BusinessException(ResultCodeEnum.trans_amount_negative);
            }

            if (param.getUpdAmount().equals(hists.get(0).getAmount())) {
                throw new BusinessException(ResultCodeEnum.trans_bal_no_modify);
            }

            // 计算与原交易的差额
            BigDecimal offset = param.getUpdAmount().subtract(hists.get(0).getAmount());

            // 拼装新交易的参数
            TransactionParam transParam = buildUpdateParam(param, offset, hists);
            createTransaction(transParam);
        }
        return true;
    }


    /**
     * 删除交易
     *
     * 删除交易是金额不变，将类型(存入/支取时)或账号(转账时)对调
     *
     * @param transId
     * @return
     */
    public boolean deleteTransaction(String transId) {
        if (transModifyMode) {
            List<Hist> hists = histService.listHist(transId,null,null,null, 1, 2);
            if (CollectionUtils.isEmpty(hists)) {
                throw new BusinessException(ResultCodeEnum.transid_not_exist);
            }
            // 拼装新交易的参数
            TransactionParam param = buildDeleteParam(transId, hists);
            createTransaction(param);
        }
        return true;
    }


    /**
     * 发起远程存入rpc调用
     * 此过程可以是 单机事务
     *
     * @param param
     * @return
     * @throws BusinessException
     */
    @AloneTransactional
    public String doDeposit(TransactionParam param) throws BusinessException {
        Deposit deposit = new Deposit();
        BeanUtils.copyProperties(param, deposit);

        Boolean rpcResult = transactionRpcService.deposit(deposit, false, true);
        return rpcResult ? param.getTransId() : "";
    }

    /**
     * 发起远程支取rpc调用
     * 此过程可以是 单机事务
     *
     * @param param
     * @return
     * @throws BusinessException
     */
    @AloneTransactional
    public String doWithdraw(TransactionParam param) throws BusinessException {
        Withdraw withdraw = new Withdraw();
        BeanUtils.copyProperties(param, withdraw);

        Boolean rpcResult = transactionRpcService.withdraw(withdraw, false, true);
        return rpcResult ? param.getTransId() : "";
    }

    /**
     * 发起远程转账rpc调用 转账核心就是 存入+支取 加上额外的逻辑
     * 此过程可能得分布式事务
     *
     * @param param
     * @return
     * @throws BusinessException
     */
    @DistributedTransactional
    public String doTransfer(TransactionParam param) throws BusinessException {

        Withdraw withdraw = new Withdraw();
        BeanUtils.copyProperties(param, withdraw);
        withdraw.setAccountNo(param.getAccountNo());
        // 先支取 可以判断余额是否充足
        Boolean withdrawRpcResult = transactionRpcService.withdraw(withdraw, true, true);

        Deposit deposit = new Deposit();
        BeanUtils.copyProperties(param, deposit);
        deposit.setAccountNo(param.getToAccountNo());
        Boolean depositRpcResult = transactionRpcService.deposit(deposit, true, true);

        return depositRpcResult && withdrawRpcResult ? param.getTransId() : "";
    }

    private TransactionParam buildUpdateParam(TransactionUpdateParam param,BigDecimal offset,List<Hist> hists){
        Hist hist = hists.get(0);

        TransactionParam transParam = new TransactionParam();
        transParam.setTransId(param.getTransId() + updateTransIdSuffix);
        transParam.setAmount(offset.abs());
        transParam.setClientId(hist.getClientId());

        if (TransType.Transfer.equals(hist.getTransType())) {
            transParam.setTransType(TransType.Transfer);

            // 转账时记录了两条流水 发起方记录accountId 接收方记录toAccountId 流水号是相同的
            String accountId = hists.stream().filter(h -> StringUtils.hasLength(h.getAccountId())).findAny().get().getAccountId();
            String toAccountId = hists.stream().filter(h -> StringUtils.hasLength(h.getToAccountId())).findAny().get().getToAccountId();

            // 金额比之前大 就是继续追加
            // 否则 需要反过来 去抵消
            if (offset.signum() > 0) {
                transParam.setAccountNo(accountId);
                transParam.setToAccountNo(toAccountId);
            } else {
                transParam.setAccountNo(toAccountId);
                transParam.setToAccountNo(accountId);
            }

        } else {
            if (offset.signum() > 0) {
                transParam.setTransType(hist.getTransType());
            } else {
                TransType type = TransType.Withdraw.equals(hist.getTransType()) ? TransType.Deposit : TransType.Withdraw;
                transParam.setTransType(type);
            }
            transParam.setAccountNo(hist.getAccountId());
        }

        return transParam;
    }

    // 删除交易的参数
    private TransactionParam buildDeleteParam(String transId,List<Hist> hists){
        Hist hist = hists.get(0);
        TransactionParam param = new TransactionParam();
        param.setTransId(transId + deleteTransIdSuffix);
        param.setAmount(hist.getAmount());
        param.setClientId(hist.getClientId());

        // 转账时 将双方账号互换
        if (TransType.Transfer.equals(hist.getTransType())) {
            param.setTransType(TransType.Transfer);

            if (StringUtils.hasLength(hist.getAccountId())) {
                param.setAccountNo(hists.get(1).getToAccountId());
                param.setToAccountNo(hist.getAccountId());
            } else {
                param.setAccountNo(hist.getToAccountId());
                param.setToAccountNo(hists.get(1).getAccountId());
            }
        } else {

            // 支取/存入时 将交易类型对换
            TransType type = TransType.Withdraw.equals(hist.getTransType()) ? TransType.Deposit : TransType.Withdraw;
            param.setTransType(type);
            param.setAccountNo(hist.getAccountId());
        }
        return param;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        checkers = SpringContextUtil.getBeansOfType(IChecker.class).values();

        transModifyMode = Boolean.valueOf(configCentre.getValue(transModfiyModeKey, transModfiyModeDefaultVal));
    }
}
