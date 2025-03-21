package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.service.TrackService;
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
@RequestMapping("/tracks")
public class TrackController {

    private final UserService userService;
    private final TrackService trackService;
    private final VideoService videoService;

    @Autowired
    public TrackController(UserService userService, TrackService trackService, VideoService videoService) {
        this.userService = userService;
        this.trackService = trackService;
        this.videoService = videoService;
    }

    @GetMapping
    public ModelAndView getTracksPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        List<Track> tracks = trackService.getAllTracks(user.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("tracks");
        modelAndView.addObject("user", user);
        modelAndView.addObject("tracks", tracks);

        return modelAndView;
    }
}
