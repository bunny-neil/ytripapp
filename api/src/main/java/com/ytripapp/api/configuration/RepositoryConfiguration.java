package com.ytripapp.api.configuration;

import com.ytripapp.domain.User;
import com.ytripapp.repository.SearchableRepositoryBase;
import com.ytripapp.repository.UserRepository;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackageClasses = User.class)
@EnableJpaRepositories(basePackageClasses = UserRepository.class, repositoryBaseClass = SearchableRepositoryBase.class)
@Import({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class RepositoryConfiguration {
}
