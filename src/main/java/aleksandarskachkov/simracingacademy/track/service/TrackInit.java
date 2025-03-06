package aleksandarskachkov.simracingacademy.track.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TrackInit implements CommandLineRunner {

    private final TrackService trackService;

    @Autowired
    public TrackInit(TrackService trackService) {
        this.trackService = trackService;
    }

    @Override
    public void run(String... args) throws Exception {
        trackService.createDefaultTracks();
    }
}
