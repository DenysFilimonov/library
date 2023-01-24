package com.my.library.servlets;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public class RequestHelper {
    private static RequestHelper instance = null;
    HashMap<String, Command> commands =
            new HashMap<String, Command>();
    private RequestHelper() {
        commands.put("login", new LoginCommands());
    }
    public Command getCommand(HttpServletRequest request) {
        String action = request.getParameter("command");
        Command command = commands.get(action);
        if (command == null) {
            command = new NoCommand();
        }
        return command;
    }
    public static RequestHelper getInstance() {
        if (instance == null) {
            instance = new RequestHelper();
        }
        return instance;
    }
}