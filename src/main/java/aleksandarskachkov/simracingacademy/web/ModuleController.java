package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.module.service.ModuleService;
import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import aleksandarskachkov.simracingacademy.video.model.Video;
import aleksandarskachkov.simracingacademy.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/modules")
public class ModuleController {

    private final UserService userService;
    private final ModuleService moduleService;
    private final VideoService videoService;

    @Autowired
    public ModuleController(UserService userService, ModuleService moduleService, VideoService videoService) {
        this.userService = userService;
        this.moduleService = moduleService;
        this.videoService = videoService;
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

    @GetMapping("/{moduleId}/videos")
    public ModelAndView getVideosForTrack(@PathVariable UUID moduleId, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        List<Video> videos = videoService.getVideosForModule(moduleId, user.getId());

        Module module = moduleService.getModuleById(moduleId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("videos");
        modelAndView.addObject("user", user);
        modelAndView.addObject("videos", videos);
        modelAndView.addObject("module", module);

        return modelAndView;
    }
}
