package com.my.library.servlets;
import com.my.library.services.AppContext;
import com.my.library.services.ConfigurationManager;
import com.my.library.services.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig(maxFileSize=1024*1024*20)
public class ViewController extends HttpServlet implements javax.servlet.Servlet {

    private static final Logger LOGGER = LogManager.getLogger(ViewController.class.getName() );


    CommandMapper commandMapper = CommandMapper.getInstance();
    public ViewController() {
        super();
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        processRequest(req, resp);
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        processRequest(req, resp);
    }

    /**
     * Dispatch requests to appropriate servlets using CommandMapper - service based on Command pattern.
     * In case of exception throwing at the levels below - catch them,then logging them and
     * send to user info wrapped in web-page
     * @param  req      HttpServletRequest request
     * @param  resp     HttpServletResponse request
     * @see             com.my.library.servlets.CommandMapper
     * @throws          SQLException can be thrown during password validation
     * @throws ServletException  basic exception for servlet classes
     * @throws IOException   basic exception for servlet classes
     */

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String page = null;

        try {
            Command command = commandMapper.getCommand(req);
            page = command.execute(req, resp, new AppContext());
        } catch (ServletException e) {
            LOGGER.error(e.toString(), e);
            req.setAttribute("errorMessage",
                    MessageManager.getInstance().getProperty(MessageManager.SERVLET_EXCEPTION_ERROR_MESSAGE));
            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
            req.setAttribute("errorMessage",
                    MessageManager.getInstance()
                            .getProperty(MessageManager.IO_EXCEPTION_ERROR_MESSAGE));
            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        } catch (SQLException e) {
            LOGGER.error( e.toString(), e);
            req.setAttribute("errorMessage",
                    MessageManager.getInstance().getProperty(MessageManager.SQL_EXCEPTION_ERROR_MESSAGE));
            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        }
        catch (Exception e) {
            LOGGER.error( e.toString(), e);
            req.setAttribute("errorMessage",
                    MessageManager.getInstance().getProperty(MessageManager.OTHER_EXCEPTION_ERROR_MESSAGE));
            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
        dispatcher.forward(req, resp);
    }


    }



