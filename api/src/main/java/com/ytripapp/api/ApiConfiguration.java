package com.ytripapp.api;

import com.ytripapp.command.validator.UserSessionCommandValidator;
import com.ytripapp.domain.User;
import com.ytripapp.repository.SearchableRepositoryBase;
import com.ytripapp.repository.UserRepository;
import com.ytripapp.service.UserSessionService;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;

@Configuration
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
public class ApiConfiguration {

    @EntityScan(basePackageClasses = User.class)
    @EnableJpaRepositories(basePackageClasses = UserRepository.class, repositoryBaseClass = SearchableRepositoryBase.class)
    @Import({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
    @Configuration
    static class RepositoryConfiguration implements InitializingBean, DisposableBean {
        @Autowired
        EntityManager em;

        FullTextEntityManager indexManager;

        @Override
        public void afterPropertiesSet() throws Exception {
            indexManager = Search.getFullTextEntityManager(em);
            indexManager.createIndexer().startAndWait();
        }

        @Override
        public void destroy() throws Exception {
            indexManager.close();
        }
    }

    @Configuration
    static class WebMvcConfiguration extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        UserSessionCommandValidator sessionCommandValidator() {
            return new UserSessionCommandValidator();
        }

        @Autowired
        @Bean
        UserSessionService userSessionService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
            return new UserSessionService(userRepository, passwordEncoder);
        }

        @Bean
        ReloadableResourceBundleMessageSource messageSource() {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setDefaultEncoding("UTF-8");
            messageSource.setBasenames("classpath:/messages");
            return messageSource;
        }
    }


    @Profile("cloud")
    @EnableDiscoveryClient
    @Configuration
    static class CloudConfiguration {
    }
}
