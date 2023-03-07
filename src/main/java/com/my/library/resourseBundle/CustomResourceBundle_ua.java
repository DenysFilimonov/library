package com.my.library.resourseBundle;
import java.io.IOException;
import java.util.*;

public class CustomResourceBundle_ua extends ListResourceBundle {
    Object[][] resources = null;

        @Override
        synchronized protected Object[][] getContents() {
            if(resources==null) {
                try {
                    System.out.println("load resources");
                    resources = LoadBundle.loadResources("/text_ua.properties");
                } catch (IOException e) {
                    System.out.println("Cant load resources");
                }
            }
            return resources;
        }
}






