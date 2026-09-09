package ma.youcode.lineperm.ui;

import java.util.Scanner;

import ma.youcode.lineperm.exceptions.UserAlreadyExisteException;
import ma.youcode.lineperm.services.AuthService;
import ma.youcode.lineperm.services.FileService;
import ma.youcode.lineperm.services.UserService;

public class ConsoleApp {
    public static String choix;
    public static String prompt;

    public final AuthService authService;
    public final UserService userService;
    public final FileService fileService;

    public ConsoleApp(AuthService authService , UserService userService , FileService fileService) {
        this.authService = authService;
        this.userService = userService;
        this.fileService = fileService;
    }

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

    public boolean notAuthDesign() {
        userService.loadUsers();
        start();

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
                return false;

            
            default:
                System.out.println("Choix don't existe.");
                break;
        }
        return true;
    }

    public void isAuthDesign() {
        System.out.print(AuthService.currentUser + "@lineperm> ");
        prompt = scanner.nextLine();

        String[] parts = ConsoleApp.prompt.trim().split(" ");

        String p = parts[0];

        switch (p) {
            case "logout":
                authService.logout();
                break;

            case "ls":
                fileService.ls();
                break;

            case "touch":
                fileService.touch(parts[1]);
                break;

            case "cat":
                fileService.cat(parts[1]);
                break;
        
            default:
                System.out.println("Commande note existe");
                break;
        }
    }
}
