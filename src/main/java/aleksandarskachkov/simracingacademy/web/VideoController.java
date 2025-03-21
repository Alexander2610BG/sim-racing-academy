package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.module.service.ModuleService;
import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.service.TrackService;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import aleksandarskachkov.simracingacademy.video.model.Video;
import aleksandarskachkov.simracingacademy.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/videos")
public class VideoController {

    private final UserService userService;
    private final VideoService videoService;
    private final TrackService trackService;
    private final ModuleService moduleService;

    @Autowired
    public VideoController(UserService userService, VideoService videoService, TrackService trackService, ModuleService moduleService) {
        this.userService = userService;
        this.videoService = videoService;
        this.trackService = trackService;
        this.moduleService = moduleService;
    }

//    @PreAuthorize("hasAuthority('PREMIUM') or hasAuthority('ULTIMATE')")
    @GetMapping("/track/{trackId}")
    public ModelAndView getVideosForTrack(@PathVariable UUID trackId, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        List<Video> videos = videoService.getVideosForTrack(trackId);

        Track track = trackService.getTrackById(trackId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("videos");
        modelAndView.addObject("user", user);
        modelAndView.addObject("videos", videos);
        modelAndView.addObject("track", track);

        return modelAndView;
    }

    @GetMapping("/module/{moduleId}")
    public ModelAndView getVideosForModule(@PathVariable UUID moduleId, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        List<Video> videos = videoService.getVideosForModule(moduleId);

        Module module = moduleService.getModuleById(moduleId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("videos");
        modelAndView.addObject("user", user);
        modelAndView.addObject("module", module);
        modelAndView.addObject("videos", videos);

        return modelAndView;
    }
}
