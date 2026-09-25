package ma.youcode.lineperm;

import java.util.Scanner;

import ma.youcode.lineperm.dao.modelsDAO.LogDAO;
import ma.youcode.lineperm.services.AuthService;
import ma.youcode.lineperm.services.DAOService;
import ma.youcode.lineperm.services.FileService;
import ma.youcode.lineperm.services.LogsService;
import ma.youcode.lineperm.ui.ConsoleApp;

public class LinePermissionMain {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        LogDAO logDAO = new LogDAO();
        FileService fileService = new FileService(new Scanner(System.in));
        LogsService logsService = new LogsService(new Scanner(System.in) , logDAO);
        DAOService daoService = new DAOService();

        ConsoleApp consoleApp = new ConsoleApp(authService , fileService, logsService , daoService);

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