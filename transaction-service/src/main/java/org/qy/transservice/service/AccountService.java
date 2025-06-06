package org.qy.transservice.service;

import org.qy.transservice.model.Account;
import org.qy.transservice.model.IModelConverter;
import org.qy.transservice.model.po.AccountPo;
import org.qy.transservice.repository.AccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 *
 * 账户服务
 *
 * @author qinyang
 * @date 2025/6/5 21:50
 */
@Service
@EnableCaching
public class AccountService implements IModelConverter {

    @Autowired
    AccountRepository accountRepository;


    /**
     * 目前场景账户信息改动频率很低
     * 使用spring注解配合caffeine内存缓存
     * 提升账户检查时的效率 减轻db压力
     *
     * @param accountId
     * @return
     */
    @Cacheable("accounts")
    public Account getAccount(String accountId) {

        AccountPo po = accountRepository.selectById(accountId);

        if(ObjectUtils.isEmpty(po)){
            return null;
        }

        Account account = (Account) poToBo(po);
        return account;
    }

    @Override
    public Object poToBo(Object po){
        Account bo = new Account();
        AccountPo accountPo = (AccountPo)po;

        BeanUtils.copyProperties(po, bo);

        bo.setStatus(Account.AccountStatus.values()[accountPo.getStatus()]);

        return bo;
    }

}
