package ma.youcode.lineperm.services;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import ma.youcode.lineperm.dao.modelsDAO.LogDAO;
import ma.youcode.lineperm.dao.modelsDAO.UserDAO;
import ma.youcode.lineperm.models.Log;
import ma.youcode.lineperm.models.enums.LogResult;

public class LogsService {
    private final Scanner scanner;
    private final LogDAO logDAO;

    public LogsService(Scanner scanner, LogDAO logDAO) {
        this.scanner = scanner;
		this.logDAO = logDAO;
    }

    public void logsNumberTotal() {
        long number = logDAO.countTotal();

        System.out.println("Nombre total d'actions : " + number);
    }

    public void refusedLogsNumber() {
        long number = logDAO.countRefusedLogTotal();

        System.out.println("Nombre refuse d'actions : " + number);
    }

    public void distinctUsers() {
        List<String> users = UserService.logs.stream()
                                        .map(log -> log.getUser().getUsername())
                                        .distinct()
                                        .toList();

        if (users.isEmpty()) {
            System.out.println("Aucune utilisateurs a des activites.");
            return;
        }

        System.out.print("Utilisateurs distincts : ");
        users.stream().forEach(user -> System.out.print(user + " | "));
        System.out.println();
    }

    public void logsNumberByUser() {
        Map<String, Long> logsNumberByUser = UserService.logs.stream()
                                                            .collect(Collectors.groupingBy(
                                                                log -> log.getUser().getUsername(),
                                                                Collectors.counting()
                                                            ));

        if (logsNumberByUser.isEmpty()) {
            System.out.println("Aucune utilisateurs a des activites.");
            return;
        }

        System.out.println("Numero de logs a chaque utilisateur : ");
        logsNumberByUser.entrySet().stream()
                        .forEach(user -> System.out.println("   - " + user.getKey() + " : " + user.getValue()));
        System.out.println();
    }

    public void topThreeFile() {
        List<String> topThreeFiles = UserService.logs.stream()
                                                .filter(log -> log.getFile() != null)
                                                .collect(Collectors.groupingBy(
                                                    log -> log.getFile().getFileName(),
                                                    Collectors.counting()
                                                ))
                                                .entrySet()
                                                .stream()
                                                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                                .limit(3)
                                                .map(Map.Entry::getKey)
                                                .toList();

        System.out.println("Le top 3 fichier : ");
        topThreeFiles.stream().forEach(f -> System.out.println("   - " + f));
    }

    public void userLogRefused() {
        UserDAO userDAO = new UserDAO();

        System.out.print("Entrer le nom d'utilisateur : ");
        String user = scanner.nextLine();

        if (userDAO.findByUsername(user) == null) {
            System.out.println("Se utilisateur n'existe pas.");
            return;
        }

        long refusedLogUser = UserService.logs.stream()
                                            .filter(log -> log.getResult() == LogResult.REFUSE)
                                            .filter(log -> log.getUser().getUsername().equals(user))
                                            .count();

        System.out.println("Nombre refuse d'actions pour " + user + " : " + refusedLogUser);
    }

    public void actifUser() {
        String user = UserService.logs.stream()
                                .collect(Collectors.groupingBy(
                                    log -> log.getUser().getUsername(),
                                    Collectors.counting()
                                ))
                                .entrySet()
                                .stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse("Aucune utilisateur n'existe.");

        System.out.println("L'utilisateur le plus actif est : " + user);
    }

    public void actionByType() {
        UserService.logs.stream()
                    .collect(Collectors.groupingBy(Log::getType , Collectors.counting()))
                    .entrySet()
                    .stream()
                    .forEach(log -> System.out.println("   - " + log.getKey() + " : " + log.getValue()));
    }
}