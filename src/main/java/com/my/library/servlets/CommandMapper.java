package com.my.library.servlets;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public class CommandMapper {
    private static CommandMapper instance = null;
    final HashMap<String, Command> commands = new HashMap<>();

    
    private CommandMapper() {

        commands.put("login", new LoginCommand());
        commands.put("catalog", new CatalogCommand());
        commands.put("orderBook", new OrderBookCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("register", new RegisterCommand());
        commands.put("subscriptions", new SubscriptionsCommand());
        commands.put("account", new AccountCommand());
        commands.put("booksManager", new BooksManagerCommand());
        commands.put("deleteBook", new DeleteBookCommand());
        commands.put("editBook", new EditeBookCommand());
        commands.put("newBook", new NewBookCommand());
        commands.put("userManager", new UserManagerCommand());
        commands.put("orders", new CurrentOrdersCommand());
        commands.put("issueOrder", new IssueOrderCommand());
        commands.put("readers", new ReadersCommand());
        commands.put("returnBook", new ReturnBookCommand());
        commands.put("noRights", new NoRightsCommand());
        commands.put("cancelOrder", new CancelOrderCommand());

    }

    /**
     * Return current commands mentioned in request
     * @param  req      HttpServletRequest request
     * @return          Command
     */
    public Command getCommand(HttpServletRequest req) {
        String action = req.getParameter("command");
        Command command = commands.get(action);
        if (command == null) {
            command = new NoCommand();
        }
        return command;
    }

    public Command getCommand(String action) {
        Command command = commands.get(action);
        if (command == null) {
            command = new NoCommand();
        }
        return command;
    }

    public static CommandMapper getInstance() {
        if (instance == null) {
            instance = new CommandMapper();
        }
        return instance;
    }
}