package com.ytripapp.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V2_0_0__UserSchema implements SpringJdbcMigration {

    JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;
        createUsersTable();
    }

    void createUsersTable() {
        jdbcTemplate.execute(
            "CREATE TABLE ytripapp2.users (" +
                "  id                   BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "  version              BIGINT NOT NULL," +
                "  enabled              TINYINT(1)," +
                "  date_created         DATETIME," +
                "  date_updated         DATETIME," +
                "  date_last_sign_in    DATETIME," +
                "  group_name           VARCHAR(31) NOT NULL," +
                "  email_address        VARCHAR(63) UNIQUE," +
                "  `password`           VARCHAR(127)," +
                "  stripe_cust_id       VARCHAR(63)," +
                "  apns_device_token    VARCHAR(255)," +
                "  nickname             VARCHAR(31) CHARACTER SET utf8mb4 NOT NULL," +
                "  gender               VARCHAR(15)," +
                "  portrait_uri         VARCHAR(511)," +
                "  phone_no             VARCHAR(31)," +
                "  first_name           VARCHAR(31) CHARACTER SET utf8mb4," +
                "  last_name            VARCHAR(31) CHARACTER SET utf8mb4," +
                "  occupation           VARCHAR(63) CHARACTER SET utf8mb4," +
                "  introduction         LONGTEXT" +
            ")"
        );
    }

}
