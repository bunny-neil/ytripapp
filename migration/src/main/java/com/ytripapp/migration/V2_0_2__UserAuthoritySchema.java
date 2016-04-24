package com.ytripapp.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V2_0_2__UserAuthoritySchema implements SpringJdbcMigration {

    JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;
        createSocialConnectionsTable();
        createForeignKey();
    }

    void createSocialConnectionsTable() {
        jdbcTemplate.execute(
            "CREATE TABLE ytripapp2.users_authorities (" +
                "  user_id          BIGINT NOT NULL, " +
                "  authority        VARCHAR(31) NOT NULL" +
                ")"
        );
    }

    void createForeignKey() {
        jdbcTemplate.execute(
            "ALTER TABLE ytripapp2.users_authorities " +
                "ADD CONSTRAINT users_authorities_fk_user_id FOREIGN KEY (user_id) REFERENCES users(id)"
        );
    }
}
