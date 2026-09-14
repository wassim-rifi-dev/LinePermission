package ma.youcode.lineperm.services;

import java.util.List;

import ma.youcode.lineperm.models.enums.LogResult;

public class LogsService {

    public void logsNumberTotal() {
        long number = UserService.logs.stream().count();

        System.out.println("Nombre total d'actions : " + number);
    }

    public void refusedLogsNumber() {
        long number = UserService.logs.stream()
                                .filter(log -> log.getResult() == LogResult.REFUSE)
                                .count();

        System.out.println("Nombre refuse d'actions : " + number);
    }

    public void distinctUsers() {
        List<String> users = UserService.logs.stream()
                                        .map(log -> log.getUser())
                                        .distinct()
                                        .toList();

        if (users.isEmpty()) {
            System.out.println("Aucune utilisateurs a des activites.");
            return;
        }

        System.out.print("Utilisateurs distincts : ");
        users.stream().forEach(System.out::print);
    }
}
