package com.ytripapp.api.configuration;

import com.ytripapp.domain.User;
import com.ytripapp.repository.SearchableRepositoryBase;
import com.ytripapp.repository.UserRepository;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@Configuration
@EntityScan(basePackageClasses = User.class)
@EnableJpaRepositories(basePackageClasses = UserRepository.class, repositoryBaseClass = SearchableRepositoryBase.class)
@Import({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class RepositoryConfiguration implements InitializingBean, DisposableBean {

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
