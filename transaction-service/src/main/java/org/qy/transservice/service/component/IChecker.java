package org.qy.transservice.service.component;

import org.qy.transsdk.exception.BusinessException;
import org.qy.transservice.model.request.TransactionParam;

/**
 *
 * 指标检查器
 * 不同交易 不同场景下 需要动态组装多个指标完成检查
 *
 * @author qinyang
 * @date 2025/6/5 21:06
 */
public interface IChecker {
    // 是否匹配本指标检查
    boolean matches(TransactionParam param);
    // 开始检查
    boolean check(TransactionParam param) throws BusinessException;
}
