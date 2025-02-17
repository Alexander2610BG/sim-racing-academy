//package aleksandarskachkov.simracingacademy.user.service;
//
//import aleksandarskachkov.simracingacademy.user.model.Country;
//import aleksandarskachkov.simracingacademy.web.dto.RegisterRequest;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserInit implements CommandLineRunner {
//
//    private final UserService userService;
//
//    public UserInit(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (!userService.getAllUsers().isEmpty()) {
//            return;
//        }
//
//        RegisterRequest registerRequest = RegisterRequest.builder()
//                .username("Alex123")
//                .password("Alex123#")
//                .country(Country.BULGARIA)
//                .build();
//
//        userService.register(registerRequest);
//    }
//}
