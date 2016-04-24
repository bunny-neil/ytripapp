package com.ytripapp.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V2_0_1__SocialConnectionSchema implements SpringJdbcMigration {

    JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;
        createSocialConnectionsTable();
        createForeignKey();
    }

    void createSocialConnectionsTable() {
        jdbcTemplate.execute(
            "CREATE TABLE ytripapp2.social_connections (" +
                "  id               BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "  connection_id    VARCHAR(63) UNIQUE," +
                "  provider_name    VARCHAR(31) NOT NULL," +
                "  user_id          BIGINT NOT NULL" +
            ")"
        );
    }

    void createForeignKey() {
        jdbcTemplate.execute(
            "ALTER TABLE ytripapp2.social_connections " +
                "ADD CONSTRAINT social_connections_fk_user_id FOREIGN KEY (user_id) REFERENCES users(id)"
        );
    }
}
