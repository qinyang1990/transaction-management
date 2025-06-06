package org.qy.transservice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.hash.BloomFilter;
import org.qy.transsdk.common.ResultCodeEnum;
import org.qy.transsdk.exception.BusinessException;
import org.qy.transsdk.util.BloomFilterUtil;
import org.qy.transservice.model.Hist;
import org.qy.transservice.model.IModelConverter;
import org.qy.transservice.model.po.HistPo;
import org.qy.transservice.model.response.HistDto;
import org.qy.transservice.repository.HistRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 交易流水服务
 *
 * @author qinyang
 * @date 2025/6/5 22:13
 */
@Service
public class HistService implements IModelConverter, InitializingBean {

    @Autowired
    HistRepository histRepository;

    // 布隆过滤器 加快交易号不存在情况的判断速度
    BloomFilter<String> filter;

    int maxPageSize = 50;

    public List<HistDto> listHistDto(String transId,String accountId, String clientId, BigDecimal amount, Integer pageNum, Integer pageSize){
        List<Hist> hists = listHist(transId, accountId, clientId, amount, pageNum, pageSize);
        if (CollectionUtils.isEmpty(hists)) {
            return new ArrayList<>();
        }
        return hists.stream().map(bo->(HistDto)boToDto(bo)).collect(Collectors.toList());
    }

    public List<Hist> listHist(String transId, String accountId, String clientId, BigDecimal amount, Integer pageNum, Integer pageSize){

        if (!ObjectUtils.isEmpty(amount) && amount.signum() < 1){
            throw new BusinessException(ResultCodeEnum.trans_amount_negative);
        }

        if (ObjectUtils.isEmpty(pageNum) || pageNum < 0){
            throw new BusinessException(ResultCodeEnum.parameter_page);
        }

        if (ObjectUtils.isEmpty(pageSize) || pageSize > maxPageSize){
            throw new BusinessException(ResultCodeEnum.page_size_too_large);
        }

        // 在增加流水时 先填充布隆过滤器
        // 如果不存在 那肯定就不存在了 无需查db
        if(StringUtils.hasLength(transId)){
            boolean contain = filter.mightContain(transId);
            if(!contain){
                return null;
            }
        }

        // 多个条件以or的关系组合
        LambdaQueryWrapper<HistPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasLength(transId),HistPo::getTransId, transId);
        queryWrapper.eq(StringUtils.hasLength(accountId),HistPo::getAccountId, accountId);
        queryWrapper.eq(StringUtils.hasLength(clientId),HistPo::getClientId, clientId);
        queryWrapper.eq(!ObjectUtils.isEmpty(amount),HistPo::getAmount, amount);

        // 以交易时间倒序
        queryWrapper.orderByDesc(HistPo::getTransTime);

        Page<HistPo> page = new Page<>(pageNum, pageSize);
        Page<HistPo> histPoPage = histRepository.selectPage(page, queryWrapper);

        if(ObjectUtils.isEmpty(histPoPage) || CollectionUtils.isEmpty(histPoPage.getRecords())){
            return null;
        }

        return histPoPage.getRecords().stream().map(po->(Hist)poToBo(po)).collect(Collectors.toList());
    }

    public boolean record(Hist hist) throws BusinessException {

        Object po = boToPo(hist);

        // 填充布隆过滤器 方便后续判断
        filter.put(hist.getTransId());
        int insert = histRepository.insert((HistPo) po);

        return insert > 0;
    }

    @Override
    public Object boToPo(Object bo){

        HistPo po = new HistPo();
        BeanUtils.copyProperties(bo,po);

        return po;
    }

    @Override
    public Object boToDto(Object bo){
        HistDto dto = new HistDto();
        BeanUtils.copyProperties(bo,dto);
        return dto;
    }

    @Override
    public Object poToBo(Object po){
        Hist hist = new Hist();
        BeanUtils.copyProperties(po,hist);
        return hist;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        filter = BloomFilterUtil.getFilter();
    }
}
