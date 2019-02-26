package com.example.idutil.distributedidgenerator.config;

import com.example.idutil.distributedidgenerator.core.SqlSeqUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @author : gaohui
 * @Date : 2019/2/18 13:38
 * @Description :
 */
@Configuration
public class MyApplicationListener implements ApplicationListener, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyApplicationListener.class);

    private volatile boolean inital = false;

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {

        if (inital) {
            return;
        }
        DataSource dataSource = context.getBean(DataSource.class);
        Environment environment = context.getEnvironment();
        Integer blockSize = environment.getProperty("my.sqlseq.blocksize", Integer.class);
        if (blockSize != null && blockSize > 1)
            SqlSeqUtil.setBlockSize(blockSize);
        SqlSeqUtil.initSqlSeq(dataSource);
        inital = true;

    }


}
