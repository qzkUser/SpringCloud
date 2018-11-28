package com.cloudE.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DruidConfig {

    @Bean(name = "test")
    @Primary
    @Autowired
    public DataSource testDataSource(Environment env){
        AtomikosDataSourceBean aksb = new AtomikosDataSourceBean();
        Properties prop = build(env,"spring.datasource.druid.test");
        aksb.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        aksb.setUniqueResourceName("test");
        aksb.setPoolSize(5);
        aksb.setXaProperties(prop);
        return aksb;
    }

    @Bean(name = "test1")
    @Autowired
    public DataSource test1DataSource(Environment env){
        AtomikosDataSourceBean aksb = new AtomikosDataSourceBean();
        Properties prop = build(env,"spring.datasource.druid.test1");
        aksb.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        aksb.setUniqueResourceName("test1");
        aksb.setPoolSize(5);
        aksb.setXaProperties(prop);
        return aksb;
    }

    private Properties build(Environment env, String prefix) {
        Properties prop = new Properties();
        prop.put("url", env.resolvePlaceholders("${" + prefix + ".url}"));
        prop.put("username", env.resolvePlaceholders("${" + prefix + ".username}"));
        prop.put("password", env.resolvePlaceholders("${" + prefix + ".password}"));
        return prop;
    }

    @Bean(name = "atomikosTx")
    public JtaTransactionManager regJtaTransactionManager(){
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        return new JtaTransactionManager(userTransactionImp,userTransactionManager);
    }

}
