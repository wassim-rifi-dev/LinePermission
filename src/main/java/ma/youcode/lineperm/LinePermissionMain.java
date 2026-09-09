    package ma.youcode.lineperm;

    import ma.youcode.lineperm.services.AuthService;
import ma.youcode.lineperm.services.FileService;
import ma.youcode.lineperm.services.UserService;
    import ma.youcode.lineperm.ui.ConsoleApp;

    public class LinePermissionMain {
        private final ConsoleApp consoleApp;
        private final AuthService authService;
        private final UserService userService;

        LinePermissionMain(ConsoleApp consoleApp , AuthService authService , UserService userService) {
            this.consoleApp = consoleApp;
            this.authService = authService;
            this.userService = userService;
        }

        public static void main(String[] args) {
            AuthService authService = new AuthService();
            UserService userService = new UserService();
            FileService fileService = new FileService();

            ConsoleApp consoleApp = new ConsoleApp(authService , userService , fileService);

            boolean running = true;

            while (running) {

                if (!AuthService.isAuth) {
                    running = consoleApp.notAuthDesign();
                } else {
                    consoleApp.isAuthDesign();
                }
            }
        }
    }