package org.qy.transservice.repository;

import org.qy.transservice.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


/**
 * h2使用的内存模式，每次服务启动，都自动读取脚本建表 索引 初始数据
 * @author qinyang
 * @date 2025/6/5 20:32
 */
@Component
@AutoConfigureAfter(DataSource.class)
public class DbTableInit implements ApplicationRunner {

    @Autowired
    DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource resource = SpringContextUtil.getApplicationContext().getResource("classpath:db/db.sql");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
    }
}
