    package ma.youcode.lineperm;

    import ma.youcode.lineperm.exceptions.UserAlreadyExisteException;
    import ma.youcode.lineperm.services.AuthService;
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
            ConsoleApp consoleApp = new ConsoleApp();
            AuthService authService = new AuthService();
            UserService userService = new UserService();

            boolean running = true;

            while (running) {

                if (!AuthService.isAuth) {
                    userService.loadUsers();
                    consoleApp.start();

                    
                } else {
                    consoleApp.isLoginDesign();

                    switch (ConsoleApp.prompt) {
                        case "logout":
                            authService.logout();
                            break;
                    
                        default:
                            System.out.println("Commande note existe");
                            break;
                    }
                }
            }
        }
    }