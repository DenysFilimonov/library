package com.my.library.resourseBundle;

import com.my.library.db.entities.Entity;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CustomResourceBundle extends ListResourceBundle {

        Object[][] resources = null;

        @Override
        synchronized protected Object[][] getContents() {
            if(resources==null) {
                try {
                    resources = loadResources();
                } catch (IOException e) {
                    System.out.println("Cant load resources");
                }
            }
            return resources;
        }

        private Object[][] loadResources() throws IOException {
            InputStream in;
            in = Entity.class.getResourceAsStream("/text.properties");
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
            System.out.println(return_array[0][0] +" "+ return_array[0][1]);
            return return_array;
        }
     }






