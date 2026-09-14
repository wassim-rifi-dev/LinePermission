package ma.youcode.lineperm.services;

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
}
