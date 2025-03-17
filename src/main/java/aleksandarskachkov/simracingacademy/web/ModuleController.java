package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.module.service.ModuleService;
import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/modules")
public class ModuleController {

    private final UserService userService;
    private final ModuleService moduleService;

    @Autowired
    public ModuleController(UserService userService, ModuleService moduleService) {
        this.userService = userService;
        this.moduleService = moduleService;
    }

    @GetMapping
    public ModelAndView getModulesPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        List<Module> modules = moduleService.getAllModules(user.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("modules");
        modelAndView.addObject("user", user);
        modelAndView.addObject("modules", modules);

        return modelAndView;
    }
}
