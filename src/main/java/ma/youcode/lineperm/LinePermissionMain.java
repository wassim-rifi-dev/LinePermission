package ma.youcode.lineperm;

import ma.youcode.lineperm.ui.ConsoleApp;

public class LinePermissionMain {
    private final ConsoleApp consoleApp;

    LinePermissionMain(ConsoleApp consoleApp) {
        this.consoleApp = consoleApp;
    }

    public static void main(String[] args) {
        ConsoleApp consoleApp = new ConsoleApp();
        new LinePermissionMain(consoleApp);
    }
}