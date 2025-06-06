package org.qy.transservice.service.component;

import org.qy.transsdk.common.ResultCodeEnum;
import org.qy.transsdk.exception.BusinessException;
import org.qy.transsdk.util.Logger;
import org.qy.transservice.model.Hist;
import org.qy.transservice.model.request.TransactionParam;
import org.qy.transservice.service.HistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 *
 * 交易流水号防重检查(流水号是每笔交易的唯一标识)
 *
 * @author qinyang
 * @date 2025/6/5 21:43
 */
@Component
public class TransidChecker implements IChecker {

    @Autowired
    HistService histService;

    Logger logger = Logger.INSTANCE;

    @Override
    public boolean matches(TransactionParam param) {
        return !ObjectUtils.isEmpty(param.getTransId()) ;
    }

    @Override
    public boolean check(TransactionParam param) throws BusinessException {

        logger.debug("checking transId:{}",param.getTransId());

        List<Hist> hists = histService.listHist(param.getTransId(),null,null,null, 1, 2);

        if(!CollectionUtils.isEmpty(hists)){
            throw new BusinessException(ResultCodeEnum.trans_id_exist);
        }

        return true;
    }


}
