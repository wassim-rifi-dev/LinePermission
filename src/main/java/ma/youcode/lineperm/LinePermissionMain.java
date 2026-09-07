package ma.youcode.lineperm;

import ma.youcode.lineperm.services.AuthService;
import ma.youcode.lineperm.services.UserService;
import ma.youcode.lineperm.ui.ConsoleApp;

public class LinePermissionMain {
    private final ConsoleApp consoleApp;
    private final AuthService authService;
    private final UserService userService;

    LinePermissionMain(ConsoleApp consoleApp , AuthService authService , UserService userService) {
        this.consoleApp = consoleApp;
        this.authService = authService;
        this.userService = userService;
    }

    public static void main(String[] args) {
        ConsoleApp consoleApp = new ConsoleApp();
        AuthService authService = new AuthService();
        UserService userService = new UserService();

        new LinePermissionMain(consoleApp , authService , userService);

        switch (consoleApp.choix) {
            case "signup":
                String[] informations = consoleApp.singUpChoix();

                String username = informations[0];
                String password = informations[1];

                authService.singUp(username, password);
                break;
            
            default:
                System.out.println("Choix don't existe.");
                break;
        }
    }
}