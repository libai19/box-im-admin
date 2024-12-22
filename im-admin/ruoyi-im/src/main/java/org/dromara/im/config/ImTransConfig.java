package org.dromara.im.config;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.fhs.trans.ds.DataSourceSetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * easy trans配置
 *
 * @author: Blue
 * @date: 2024-07-20
 * @version: 1.0
 */
@Configuration
public class ImTransConfig {

    @Bean
    public DataSourceSetter dataSourceSetter() {
        // 适配多数据源
        return new DataSourceSetter() {
            @Override
            public void setDataSource(String datasourceName) {
                DynamicDataSourceContextHolder.push(datasourceName);
            }

            @Override
            public Map<Object, Object> getContext() {
                return null;
            }

            @Override
            public void setContext(Map<Object, Object> context) {
            }
        };
    }

}
