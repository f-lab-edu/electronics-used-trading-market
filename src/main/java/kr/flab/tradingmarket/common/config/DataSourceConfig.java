package kr.flab.tradingmarket.common.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
public class DataSourceConfig {

    private static final String SOURCE = "SOURCE";
    private static final String REPLICA = "REPLICA";

    @Bean
    @Qualifier(SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.source")
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create()
            .build();
    }

    @Bean
    @Qualifier(REPLICA)
    @ConfigurationProperties(prefix = "spring.datasource.replica")
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create()
            .build();
    }

    @Bean
    public DataSource routingDataSource(@Qualifier(SOURCE) DataSource sourceDataSource,
        @Qualifier(REPLICA) DataSource replicaDataSource) {

        RoutingDataSource routingDataSource = new RoutingDataSource();
        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("source", sourceDataSource);
        dataSourceMap.put("replica", replicaDataSource);
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(sourceDataSource);
        return routingDataSource;

    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource(sourceDataSource(), replicaDataSource()));
    }

}