package com.ytripapp.api;

import com.ytripapp.domain.User;
import com.ytripapp.repository.SearchableRepositoryBase;
import com.ytripapp.repository.UserRepository;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@Configuration
@EnableAutoConfiguration
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

    @Profile("cloud")
    @EnableDiscoveryClient
    @Configuration
    static class CloudConfiguration {
    }

}
