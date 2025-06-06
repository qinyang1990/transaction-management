package org.qy.transservice.service.component;

import org.qy.transsdk.common.ResultCodeEnum;
import org.qy.transsdk.exception.BusinessException;
import org.qy.transsdk.util.Logger;
import org.qy.transservice.model.Account;
import org.qy.transservice.model.request.TransactionParam;
import org.qy.transservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 *
 * 账户指标检查
 * 账号是否合法/账户状态是否正常
 *
 * @author qinyang
 * @date 2025/6/5 21:31
 */
@Component
public class AccountChecker implements IChecker {

    @Autowired
    AccountService accountService;

    Logger logger = Logger.INSTANCE;

    @Override
    public boolean matches(TransactionParam param) {
        return !ObjectUtils.isEmpty(param.getAccountNo()) || !ObjectUtils.isEmpty(param.getToAccountNo());
    }

    @Override
    public boolean check(TransactionParam param) throws BusinessException {
        boolean check = check(param.getAccountNo());
        boolean checkTo = check(param.getToAccountNo());
        return check && checkTo;
    }

    private boolean check(String accountId) throws BusinessException{
        logger.debug("check account:{}",accountId);
        if(ObjectUtils.isEmpty(accountId)){
            return true;
        }

        logger.debug("getting account soon....");
        Account account = accountService.getAccount(accountId);
        if(ObjectUtils.isEmpty(account)){
            throw new BusinessException(ResultCodeEnum.accountid_not_exist);
        }
        return Account.AccountStatus.Normal.equals(account.getStatus());

    }
}
