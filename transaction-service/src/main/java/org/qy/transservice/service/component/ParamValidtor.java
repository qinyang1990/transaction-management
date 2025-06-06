package org.qy.transservice.service.component;

import org.qy.transapi.model.TransInfo;
import org.qy.transsdk.common.ResultCodeEnum;
import org.qy.transsdk.exception.BusinessException;
import org.qy.transsdk.util.Logger;
import org.qy.transservice.model.TransType;
import org.qy.transservice.model.request.TransactionParam;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


/**
 *
 * 简单的参数校验
 * 只验证是否为空这种级别
 *
 * @author qinyang
 * @date 2025/6/5 21:40
 */
@Component
public class ParamValidtor {

    Logger logger = Logger.INSTANCE;

    private void validateBase(TransInfo info) throws BusinessException {
        if(ObjectUtils.isEmpty(info.getTransId())){
            throw new BusinessException(ResultCodeEnum.parameter_transid);
        }

        if(ObjectUtils.isEmpty(info.getClientId())){
            throw new BusinessException(ResultCodeEnum.parameter_clientid);
        }
    }

    public void validateTransaction(TransactionParam param) throws BusinessException {

        validateBase(param);

        logger.debug("validateTransaction");

        if(ObjectUtils.isEmpty(param.getTransType())){
            throw new BusinessException(ResultCodeEnum.parameter_transtype);
        }

        if(ObjectUtils.isEmpty(param.getAccountNo())){
            throw new BusinessException(ResultCodeEnum.parameter_accountid);
        }

        if(ObjectUtils.isEmpty(param.getAmount())){
            throw new BusinessException(ResultCodeEnum.parameter_amount);
        }

        // 金额必须是正数
        if(param.getAmount().signum() < 1){
            throw new BusinessException(ResultCodeEnum.trans_amount_negative);
        }

        if(TransType.Transfer.equals(param.getTransType()) && ObjectUtils.isEmpty(param.getToAccountNo())){
            throw new BusinessException(ResultCodeEnum.parameter_accountid);
        }


    }

}
