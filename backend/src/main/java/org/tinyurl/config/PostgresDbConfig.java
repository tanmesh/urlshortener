package org.tinyurl.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostgresDbConfig {
    private String driverClass;
    private String user;
    private String url;

    public PostgresDbConfig() {
    }

    public PostgresDbConfig(String driverClass, String user, String url) {
        this.driverClass = driverClass;
        this.user = user;
        this.url = url;
    }
}
