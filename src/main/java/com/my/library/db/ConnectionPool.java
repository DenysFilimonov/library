package com.my.library.db;


//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
import com.my.library.db.entities.Entity;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConnectionPool2 {

    public static BasicDataSource dataSource = null;

        static {
            InputStream in = new Entity().getClass().getResourceAsStream("/db.properties");
            Properties prop = new Properties();
            try {
                prop.load(in);
                Class.forName( "org.postgresql.Driver" );
            } catch (IOException | ClassNotFoundException | RuntimeException ex) {
                System.out.println(ex);
            }
            dataSource = new BasicDataSource();
            dataSource.setUrl(prop.getProperty("jdbc.url"));
            dataSource.setUsername(prop.getProperty("jdbc.username"));
            dataSource.setPassword(prop.getProperty("jdbc.password"));
            dataSource.setMinIdle(5);
            dataSource.setMaxIdle(10);
            dataSource.setMaxTotal(25);
            System.out.println("Connection Successful");

    }
}




