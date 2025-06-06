package org.qy.transservice.service.component;

import org.qy.transsdk.exception.BusinessException;
import org.qy.transsdk.util.Logger;
import org.qy.transservice.model.request.TransactionParam;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 *
 * 模拟客户权限相关及各种限制
 *
 * @author qinyang
 * @date 2025/6/5 21:38
 */
@Component
public class AuthAndRestrictChecker implements IChecker {

    Logger logger = Logger.INSTANCE;

    @Override
    public boolean matches(TransactionParam param) {
        return !ObjectUtils.isEmpty(param.getClientId());
    }

    @Override
    public boolean check(TransactionParam param) throws BusinessException {
        logger.debug("{} check passed",getClass().getSimpleName());
        return true;
    }

}
