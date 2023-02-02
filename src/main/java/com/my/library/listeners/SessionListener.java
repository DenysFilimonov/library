package com.my.library.listeners;
import com.my.library.filters.LogingFilter;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

@WebListener
public class SessionListener implements HttpSessionListener {

    private static final Logger LOGGER = LogManager.getLogger(LogingFilter.class.getName() );

    public void sessionCreated(HttpSessionEvent sessionEvent) {

        LOGGER.info("Session with ID "+sessionEvent.getSession().getId()+" created");
    }


    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        LOGGER.info("Session with ID "+sessionEvent.getSession().getId()+" deleted");
    }
}

