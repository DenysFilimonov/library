package com.my.library.resourseBundle;

import com.my.library.db.entities.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.ListResourceBundle;
import java.util.Properties;

public class LoadBundle{

        public static Object[][] loadResources(String resourceName) throws IOException {
            InputStream in;
            in = Entity.class.getResourceAsStream(resourceName);
            Properties prop = new Properties();
            prop.load(in);
            Enumeration<Object> properties = prop.keys();
            Object[][] return_array = new Object[prop.size()][2];
            int i = 0;
            while(properties.hasMoreElements()){
                String key = (String) properties.nextElement();
                String property = (String) prop.get(key);
                String encodedProperty =new String(property.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                return_array[i][0] = key;
                return_array[i][1] = encodedProperty;
                i++;
            }
            return return_array;
        }
}






