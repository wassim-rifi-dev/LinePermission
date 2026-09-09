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

                    switch (ConsoleApp.choix) {
                        case "signup":
                            try {
                                String[] singupInformations = consoleApp.singUpChoix();

                                String signUpUsername = singupInformations[0];
                                String signUpPassword = singupInformations[1];

                                authService.singUp(signUpUsername, signUpPassword);
                            } catch (UserAlreadyExisteException e) {
                                System.out.println(e.getMessage());
                            }
                            break;

                        case "login":
                            try {
                                String[] loginInformations = consoleApp.loginChoix();

                                String loginUsername = loginInformations[0];
                                String loginPassword = loginInformations[1];

                                authService.login(loginUsername, loginPassword);
                            } catch (UserAlreadyExisteException e) {
                                System.out.println(e.getMessage());
                            }
                            break;

                        case "exit":
                            System.out.println("Au revoir.");
                            running = false;
                            break;

                        
                        default:
                            System.out.println("Choix don't existe.");
                            break;
                    }
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