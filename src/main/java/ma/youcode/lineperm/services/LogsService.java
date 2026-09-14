package ma.youcode.lineperm.services;

public class LogsService {

    public void logsNumberTotal() {
        long number = UserService.logs.stream().count();

        System.out.println("Nombre total d'actions : " + number);
    }
}
