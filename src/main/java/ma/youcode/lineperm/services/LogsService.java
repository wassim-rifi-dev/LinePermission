package ma.youcode.lineperm.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ma.youcode.lineperm.models.AccessLog;
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
        users.stream().forEach(System.out::println);
    }

    public void logsNumberByUser() {
        Map<String, Long> logsNumberByUser = UserService.logs.stream()
                                                            .collect(Collectors.groupingBy(
                                                                AccessLog::getUser,
                                                                Collectors.counting()
                                                            ));

        if (logsNumberByUser.isEmpty()) {
            System.out.println("Aucune utilisateurs a des activites.");
            return;
        }

        System.out.println("Numero de logs a chaque utilisateur : ");
        logsNumberByUser.entrySet().stream()
                        .forEach(user -> System.out.print(user.getKey() + " : " + user.getValue()));
        System.out.println("");
    }

    public void topThreeFile() {
        List<String> topThreeFiles = UserService.logs.stream()
                                                .collect(Collectors.groupingBy(
                                                    AccessLog::getFile,
                                                    Collectors.counting()
                                                ))
                                                .entrySet()
                                                .stream()
                                                .sorted(Map.Entry.comparingByValue())
                                                .limit(3)
                                                .map(Map.Entry::getKey)
                                                .toList();

        topThreeFiles.stream().forEach(System.out::print);
    }
}
