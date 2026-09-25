package ma.youcode.lineperm;

import java.util.Scanner;

import ma.youcode.lineperm.dao.modelsDAO.LogDAO;
import ma.youcode.lineperm.services.AuthService;
import ma.youcode.lineperm.services.DAOService;
import ma.youcode.lineperm.services.FileService;
import ma.youcode.lineperm.services.LogsService;
import ma.youcode.lineperm.services.UserService;
import ma.youcode.lineperm.ui.ConsoleApp;

public class LinePermissionMain {
    private final ConsoleApp consoleApp;
    private final AuthService authService;
    private final UserService userService;
    private final LogsService logsService;

    LinePermissionMain(ConsoleApp consoleApp , AuthService authService , UserService userService , Scanner scanner, LogsService logsService) {
        this.consoleApp = consoleApp;
        this.authService = authService;
        this.userService = userService;
        this.logsService = logsService;
    }

    public static void main(String[] args) {
        AuthService authService = new AuthService();
        UserService userService = new UserService();
        LogDAO logDAO = new LogDAO();
        FileService fileService = new FileService(new Scanner(System.in));
        LogsService logsService = new LogsService(new Scanner(System.in) , logDAO);
        DAOService daoService = new DAOService();

        ConsoleApp consoleApp = new ConsoleApp(authService , userService , fileService, logsService , daoService);

        boolean running = true;

        while (running) {
            if (!AuthService.isAuth) {
                running = consoleApp.notAuthDesign();
            } else {
                consoleApp.isAuthDesign();
            }
        }
    }
}