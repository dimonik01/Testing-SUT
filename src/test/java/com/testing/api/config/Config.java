package com.testing.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static final Properties PROPERTIES = new Properties();

    static {
        try(InputStream input = Config.class.getResourceAsStream("/config.properties")){
            PROPERTIES.load(input);
        } catch (Exception e) {
            System.out.println("Ошибка при чтении конфигурации " + e.getMessage());
        }
    }

    public static String getProperties(String property){
        if (property == null) {
            System.out.println("Missing config property");
        }
        return PROPERTIES.getProperty(property);
    }
}
