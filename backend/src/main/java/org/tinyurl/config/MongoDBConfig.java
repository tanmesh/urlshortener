package org.tinyurl.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongoDBConfig {
    private String dbName;
    private int port;
    private String host;
    private String user;
    private String password;

    public MongoDBConfig(String dbName, int port, String host) {
        this.dbName = dbName;
        this.port = port;
        this.host = host;
    }

    public MongoDBConfig() {
    }
}
