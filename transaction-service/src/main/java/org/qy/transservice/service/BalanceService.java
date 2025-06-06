package org.qy.transservice.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.qy.transsdk.common.ResultCodeEnum;
import org.qy.transsdk.exception.BusinessException;
import org.qy.transservice.model.TransType;
import org.qy.transservice.model.po.BalancePo;
import org.qy.transservice.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * 余额服务
 *
 * @author qinyang
 * @date 2025/6/5 22:03
 */
@Service
public class BalanceService {

    @Autowired
    BalanceRepository balanceRepository;

    public boolean changeBalance(String accountId, TransType type, BigDecimal balOffset) throws BusinessException {

        if(!TransType.Deposit.equals(type) && !TransType.Withdraw.equals(type)){
            throw new BusinessException(ResultCodeEnum.trans_bal_trans_type);
        }

        LambdaUpdateWrapper<BalancePo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BalancePo::getAccountId, accountId);
        wrapper.set(BalancePo::getChangeTime, new Date());

        // 暂不考虑复杂的查询到修改中途 余额被并发修改的冲突
        // 使用数据库方式先  bal = bal +- <offset>
        StringBuilder balSetSqlBuilder = new StringBuilder(" BAL = BAL");
        if(TransType.Deposit.equals(type)){
            balSetSqlBuilder.append(" + ");
        }else {
            balSetSqlBuilder.append(" - ");
            // 支取时如果余额小于当前金额 表示账户余额不足(update==0)
            wrapper.ge(BalancePo::getBal, balOffset);
        }
        balSetSqlBuilder.append(balOffset);
        wrapper.setSql(balSetSqlBuilder.toString());

        int update = balanceRepository.update(wrapper);
        if(update == 0){
            throw new BusinessException(ResultCodeEnum.trans_bal_lack);
        }

        return true;
    }

}
