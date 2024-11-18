package org.example.robmaguirecustomersupport.config;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Hashtable;
import java.util.Map;

@Configuration
/*@EnableScheduling
@EnableAsync(
        mode = AdviceMode.PROXY,
        order = Ordered.HIGHEST_PRECEDENCE
)*/
@EnableTransactionManagement(
        mode = AdviceMode.PROXY
)
@ComponentScan(
        basePackages = "org.example.robmaguirecustomersupport.site",
        excludeFilters = @ComponentScan.Filter(Controller.class)
)
public class RootContextConfiguration implements TransactionManagementConfigurer {
    @Bean
    public static DataSource dataSource() {
        final var dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("tomcat");
        dataSource.setPassword("password");
        dataSource.setUrl("jdbc:mysql://localhost:3306/CustomerSupport");

        return dataSource;
        //return new JndiDataSourceLookup().getDataSource("jdbc/CustomerSupport");
    }

    @Bean
    public static LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final Map<String, Object> properties = new Hashtable<>();
        properties.put("jakarta.persistence.schema-generation.database.action", "none");

        final var factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPackagesToScan("org.example.robmaguirecustomersupport.entities");

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(adapter);
        factory.setJpaPropertyMap(properties);

        factory.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        factory.setValidationMode(ValidationMode.NONE);

        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }
}
