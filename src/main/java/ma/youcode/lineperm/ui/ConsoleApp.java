package ma.youcode.lineperm.ui;

import java.util.Scanner;

import ma.youcode.lineperm.services.AuthService;

public class ConsoleApp {
    public static String choix;
    public static String prompt;

    Scanner scanner = new Scanner(System.in);
    
    public ConsoleApp() {
        if (!AuthService.isAuth && AuthService.currentUser == null) {
            start();
        } else {
            isLoginDesign();
        }
    }

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
}
