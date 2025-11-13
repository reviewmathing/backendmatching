package com.hunko.missionmatching.core;

import java.util.Arrays;

public enum Authorities {
    ADMIN("ROLE"),
    USER("ROLE");

    private final String type;

    Authorities(String role) {
        this.type = role;
    }

    public static Authorities findByName(String value) {
        String upperCase = value.toUpperCase();
        return Arrays.stream(Authorities.values())
                .filter(a -> a.name().equals(upperCase) || a.toSpringAuth().equals(upperCase)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String toSpringAuth() {
        return type + "_" + this.name();
    }
}
