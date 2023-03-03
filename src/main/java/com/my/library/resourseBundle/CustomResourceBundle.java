package com.my.library.resourseBundle;

import com.my.library.db.entities.Entity;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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
            Map<String, String> resourcesMap  = new HashMap<>() ;
            InputStream in;
            in = new Entity().getClass().getResourceAsStream("/text.properties");
            Properties prop = new Properties();
            prop.load(in);
            Enumeration properties = prop.keys();
            Object[][] return_array = new Object[prop.size()][2];
            int i = 0;
            while(properties.hasMoreElements()){
                String key = (String) properties.nextElement();
                String property = (String) prop.get(key);
                String encodedProperty =new String(property.getBytes(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"));
                return_array[i][0] = key;
                return_array[i][1] = encodedProperty;
                i++;
            }
            System.out.println(return_array[0][0] +" "+ return_array[0][1]);
            return return_array;
        }

     public static String convert(String value, String fromEncoding, String toEncoding) throws
            UnsupportedEncodingException {
                return new String(value.getBytes(fromEncoding), toEncoding);
            }

            public static String charset(String value, String charsets[]) throws UnsupportedEncodingException {
                String probe = StandardCharsets.UTF_8.name();
                for(String c : charsets) {
                    Charset charset = Charset.forName(c);
                    if(charset != null) {
                        if(value.equals(convert(convert(value, charset.name(), probe), probe, charset.name()))) {
                            return c;
                        }
                    }
                }
                return StandardCharsets.UTF_8.name();
            }

            // String subj = new String(subject.getBytes(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"));

     public static void main(String[] args) throws IOException {
            CustomResourceBundle cr =new CustomResourceBundle();
            cr.loadResources();
     }


        }






