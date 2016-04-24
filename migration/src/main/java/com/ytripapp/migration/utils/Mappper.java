package com.ytripapp.migration.utils;

import org.springframework.util.StringUtils;

public class Mappper {

    public static String mapGender(String oldGender) {
        if (StringUtils.isEmpty(oldGender) || "UNKNOWN".equals(oldGender)) {
            return "Unspecified";
        }

        if ("MALE".equals(oldGender)) {
            return "Male";
        }

        return "Female";
    }

    public static String mapGroupName(String oldName) {
        if ("GROUP_GUEST".equals(oldName)) {
            return "Guest";
        }

        if ("GROUP_EDITOR".endsWith(oldName)) {
            return "Editor";
        }

        return "Host";
    }
}
