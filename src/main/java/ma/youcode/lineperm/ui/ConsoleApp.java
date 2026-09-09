package ma.youcode.lineperm.ui;

import java.util.Scanner;

import ma.youcode.lineperm.exceptions.UserAlreadyExisteException;
import ma.youcode.lineperm.services.AuthService;

public class ConsoleApp {
    public static String choix;
    public static String prompt;

    Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("====================== LinePerm ====================");

        System.out.println("Non Connecte ? Commandes : signup | login | help | exit\n");

        System.out.print("lineperm> ");
        choix = scanner.nextLine();
    }

    public String[] singUpChoix() {
        System.out.println("Entrer votre information.");

        System.out.print("username : ");
        String username = scanner.nextLine();

        System.out.print("password : ");
        String password = scanner.nextLine();

        return new String[] {username , password};
    }

    public String[] loginChoix() {
        System.out.println("Entrer votre information.");

        System.out.print("username : ");
        String username = scanner.nextLine();

        System.out.print("password : ");
        String password = scanner.nextLine();

        return new String[] {username , password};
    }

    public void isLoginDesign() {
        if (AuthService.isAuth) {
            System.out.print(AuthService.currentUser + "@lineperm> ");
            prompt = scanner.nextLine();
        }
    }

    public void notAuthDesign(boolean running) {
        switch (ConsoleApp.choix) {
            case "signup":
                try {
                    String[] singupInformations = singUpChoix();

                    String signUpUsername = singupInformations[0];
                    String signUpPassword = singupInformations[1];

                    authService.singUp(signUpUsername, signUpPassword);
                } catch (UserAlreadyExisteException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "login":
                try {
                    String[] loginInformations = loginChoix();

                    String loginUsername = loginInformations[0];
                    String loginPassword = loginInformations[1];

                    authService.login(loginUsername, loginPassword);
                } catch (UserAlreadyExisteException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "exit":
                System.out.println("Au revoir.");
                running = false;
                break;

            
            default:
                System.out.println("Choix don't existe.");
                break;
        }
    }
}
