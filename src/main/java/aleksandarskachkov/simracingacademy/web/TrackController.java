package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.service.TrackService;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import aleksandarskachkov.simracingacademy.video.model.Video;
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

    @Autowired
    public TrackController(UserService userService, TrackService trackService) {
        this.userService = userService;
        this.trackService = trackService;
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

    @GetMapping("/{id}/videos")
    public ModelAndView getVideosPageOfTrack(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

//        List<Video> videos = trackService.getAllVideosOfTrack(id);

        Track track = trackService.getTrackById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("videos");
        modelAndView.addObject("user", user);
        modelAndView.addObject("track", track);
//        modelAndView.addObject("videos", videos);

        return modelAndView;
    }
}
