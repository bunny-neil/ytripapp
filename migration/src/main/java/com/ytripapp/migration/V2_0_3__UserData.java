package com.ytripapp.migration;

import com.ytripapp.migration.utils.Mappper;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class V2_0_3__UserData implements SpringJdbcMigration {

    JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;
        getAllOldUsers().stream().forEach(row -> {
            Long oldId = (Long)row.get("id");
            String creationMethod = (String) row.get("creation_method");
            String emailAddress = (String) row.get("email");
            if (emailAddress != null) {
                emailAddress = emailAddress.toLowerCase();
            }
            String password = (String) row.get("password");
            String firstName = (String) row.get("first_name");
            String lastName = (String) row.get("last_name");
            String gender = Mappper.mapGender((String) row.get("gender"));
            String phoneNo = (String) row.get("telephone");
            String occupation = (String) row.get("occupation");
            String introduction = (String) row.get("introduction");
            Date dateCreated = (Date) row.get("date_created");
            Date dateLastSignIn = (Date) row.get("date_last_login");
            Date dateUpdated = (Date) row.get("last_updated");
            boolean enabled = (Boolean) row.get("enabled");
            String facebookId = (String) row.get("facebook_id");
            String wechatId = (String) row.get("wechat_id");
            String stripeCustId = (String) row.get("stripe_customer_id");
            String nickname = (String) row.get("display_name");
            String wechatUnionId = (String) row.get("wechat_union_id");
            String iosDeviceId = (String) row.get("ios_device_token");
            String groupName = Mappper.mapGroupName((String) row.get("groupName"));
            String portraitUri = (String) row.get("portraitUri");
            Long newId = insertNewUser(
                enabled,
                dateCreated,
                dateUpdated,
                dateLastSignIn,
                groupName,
                emailAddress,
                password,
                nickname,
                gender,
                portraitUri,
                phoneNo,
                firstName,
                lastName,
                occupation,
                introduction,
                stripeCustId,
                iosDeviceId
            );
            if (newId != null) {
                insertNewAuthorities(newId, groupName);
                if (!StringUtils.isEmpty(facebookId)) {
                    insertSocialConnection("Facebook", facebookId, newId);
                }
                if (!StringUtils.isEmpty(wechatUnionId)) {
                    insertSocialConnection("Wechat", wechatUnionId, newId);
                }
                else if (!StringUtils.isEmpty(wechatId)) {
                    insertSocialConnection("Wechat", wechatId, newId);
                }
            }
        });

        addGatewayUser();
    }

    void insertSocialConnection(String providerName, String connectionId, Long userId) {
        if (jdbcTemplate.queryForObject(
            "SELECT count(*) FROM ytripapp2.social_connections WHERE connection_id = '" + connectionId + "'", Long.class) == 0) {
            jdbcTemplate.update(
                "INSERT INTO ytripapp2.social_connections(connection_id, provider_name, user_id) VALUES (?, ?, ?)",
                connectionId, providerName, userId
            );
        }
    }

    void insertNewAuthorities(Long userId, String grouopName) {
        String authority = null;
        if ("Guest".equals(grouopName)) {
            authority = "ROLE_GUEST";
        }
        if ("Editor".equals(grouopName)) {
            authority = "ROLE_EDITOR";
        }
        if ("Host".equals(grouopName)) {
            authority = "ROLE_HOST";
        }
        if ("Gateway".equals(grouopName)) {
            authority = "ROLE_GATEWAY";
        }

        jdbcTemplate.update("INSERT INTO ytripapp2.users_authorities(user_id, authority) VALUES (?, ?)", userId, authority);
    }

    Long insertNewUser(boolean enabled,
                       Date dateCreated,
                       Date dateUpdated,
                       Date dateLastSignIn,
                       String groupName,
                       String emailAddress,
                       String password,
                       String nickname,
                       String gender,
                       String portraitUri,
                       String phoneNo,
                       String firstName,
                       String lastName,
                       String occupation,
                       String introduction,
                       String stripeCustId,
                       String apnsDeviceToken) {
        if (jdbcTemplate.queryForObject(
                "SELECT count(id) FROM ytripapp2.users WHERE email_address = '" + emailAddress + "'", Long.class) == 0) {
            jdbcTemplate.update("INSERT INTO ytripapp2.users(" +
                    "version, " +
                    "enabled, " +
                    "date_created, " +
                    "date_updated, " +
                    "date_last_sign_in, " +
                    "group_name, " +
                    "email_address, " +
                    "`password`, " +
                    "nickname, " +
                    "gender, " +
                    "portrait_uri, " +
                    "phone_no, " +
                    "first_name, " +
                    "last_name, " +
                    "occupation, " +
                    "introduction, " +
                    "stripe_cust_id, " +
                    "apns_device_token) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                0L,
                enabled,
                dateCreated,
                dateUpdated,
                dateLastSignIn,
                groupName,
                emailAddress,
                password,
                nickname,
                gender,
                portraitUri,
                phoneNo,
                firstName,
                lastName,
                occupation,
                introduction,
                stripeCustId,
                apnsDeviceToken);
            return jdbcTemplate.queryForObject("SELECT max(id) FROM ytripapp2.users;", Long.class);
        }
        return null;
    }

    List<Map<String, Object>> getAllOldUsers() {
        return jdbcTemplate.queryForList(
            "SELECT u.*, g.name AS groupName, i.path AS portraitUri " +
                "FROM ytripapp.users u INNER JOIN ytripapp.groups g ON u.group_id = g.id " +
                "                      LEFT JOIN ytripapp.images i ON u.portrait_image_id = i.id " +
                "WHERE g.name IN ('GROUP_GUEST', 'GROUP_HOST', 'GROUP_EDITOR')"
        );
    }

    void addGatewayUser() {
        Long newId = insertNewUser(
                true,
                new Date(),
                null,
                null,
                "Gateway",
                "gateway@ytripapp.com",
                "$2a$08$OejaVvoDWSYv97UZOWgZOeo7axINLcksxMMoXta90HQfJcnC/KVte", // bcrypt('2TOIQIZEoBVu3sCgPJvZ')
                "gateway",
                "Unspecified",
                null,
                null,
                "Gateway",
                "Gateway",
                null,
                null,
                null,
                null
        );
        insertNewAuthorities(newId, "Gateway");
    }
}