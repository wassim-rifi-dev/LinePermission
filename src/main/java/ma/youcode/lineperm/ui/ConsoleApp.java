package ma.youcode.lineperm.ui;

import java.util.Scanner;

public class ConsoleApp {
    String choix;

    Scanner scanner = new Scanner(System.in);
    
    public ConsoleApp() {
        start();
    }

    public void start() {
        System.out.println("====================== LinePerm ====================");

        System.out.println("Non Connecte ? Commandes : singup | login | help | exit\n");

        System.out.print("lineperm> ");
        choix = scanner.nextLine();
    }
}
