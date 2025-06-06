package org.qy.transsdk.util;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.StandardCharsets;

/**
 * 布隆过滤器工具类
 *
 * @author qinyang
 * @date 2025/6/5 16:48
 */
public class BloomFilterUtil {

    public static BloomFilter<String> getFilter(){
        BloomFilter<String> filter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),
                1_000_000,0.0001);
        return filter;
    }
}
