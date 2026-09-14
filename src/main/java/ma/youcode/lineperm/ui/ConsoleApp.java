package ma.youcode.lineperm.ui;

import java.util.Scanner;

import ma.youcode.lineperm.exceptions.UserAlreadyExisteException;
import ma.youcode.lineperm.services.AuthService;
import ma.youcode.lineperm.services.FileService;
import ma.youcode.lineperm.services.LogsService;
import ma.youcode.lineperm.services.UserService;

public class ConsoleApp {
    public static String choix;
    public static String prompt;
    public static int statChoix;

    public final AuthService authService;
    public final UserService userService;
    public final FileService fileService;
    public final LogsService logsService;

    public ConsoleApp(AuthService authService , UserService userService , FileService fileService, LogsService logsService) {
        this.authService = authService;
        this.userService = userService;
        this.fileService = fileService;
        this.logsService = logsService;
    }

    Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("====================== LinePerm ====================");

        System.out.println("Non Connecte ? Commandes : signup | login | stats | help | exit\n");

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

                case "stats" :
                    stats();
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

            case "nano":
                fileService.nano(parts[1]);
                break;

            case "chmod":
                fileService.chmod(parts[2] , parts[1]);
                break;
        
            default:
                System.out.println("Commande note existe");
                break;
        }
    }

    public void stats() {
        System.out.println("========= Stats =========");
        System.out.println("1) Nombre total d'actions");
        System.out.println("2) Nombre d'acces refuses");
        System.out.println("3) Utilisateurs distincts");
        System.out.println("4) Actions par utilisateur");
        System.out.println("5) Top 3 des fichiers consultes");
        System.out.println("6) Acces refuses d'un utilisateur");
        System.out.println("7) Utilisateur le plus actif");
        System.out.println("8) Repartition des action par type");
        System.out.println("0) Quitter");

        
        do {
            System.out.print("Choix : ");
            statChoix = scanner.nextInt();
            scanner.nextLine();

            switch (statChoix) {
                case 0:
                    break;

                case 1:
                    logsService.logsNumberTotal();
                    break;

                case 2:
                    logsService.refusedLogsNumber();
                    break;

                case 3:
                    logsService.distinctUsers();
                    break;

                case 4 :
                    logsService.logsNumberByUser();
                    break;

                default:
                    System.out.println("Commande note existe");
                    break;
            }
        } while (statChoix != 0);
    }
}
