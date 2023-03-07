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
                    resources = LoadBundle.loadResources("/text.properties");
                } catch (IOException e) {
                    System.out.println("Cant load resources");
                }
            }
            return resources;
        }
     }






