package ma.youcode.lineperm;

import ma.youcode.lineperm.exceptions.UserAlreadyExisteException;
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

        do {
            switch (ConsoleApp.choix) {
                case "signup":
                    try {
                        String[] singupInformations = consoleApp.singUpChoix();

                        String signUpUsername = singupInformations[0];
                        String signUpPassword = singupInformations[1];

                        authService.singUp(signUpUsername, signUpPassword);
                        consoleApp.isLoginDesign();
                    } catch (UserAlreadyExisteException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "login":
                    try {
                        String[] loginInformations = consoleApp.singUpChoix();

                        String loginUsername = loginInformations[0];
                        String loginPassword = loginInformations[1];

                        authService.login(loginUsername, loginPassword);
                        consoleApp.isLoginDesign();
                    } catch (UserAlreadyExisteException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "exit":
                    System.out.println("Au revoir.");
                    break;

                
                default:
                    System.out.println("Choix don't existe.");
                    break;
            }
        } while (!ConsoleApp.choix.equals("exit"));
    }
}