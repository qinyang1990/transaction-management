package org.qy.transsdk.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 业务系统持久层基类
 *
 * @author qinyang
 * @date 2025/6/5 14:58
 */
public interface BusiRepository<T> extends BaseMapper<T> {

    int insertBatchSomeColumn(List<T> entityList);

}
