package aleksandarskachkov.simracingacademy.track;

import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import aleksandarskachkov.simracingacademy.track.service.TrackService;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.video.model.Video;
import aleksandarskachkov.simracingacademy.video.service.VideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackServiceUTest {

    @Mock
    private TrackRepository trackRepository;
    @Mock
    private VideoService videoService;

    @InjectMocks
    private TrackService trackService;

    @Test
    void givenTrackDetails_whenCreateTrack_thenCreateTrack() {

        // Given
        Video video = Video.builder()
                .id(UUID.randomUUID())
                .title("Title")
                .description("Description")
                .build();

        User owner = new User();

        Track track = Track.builder()
                .id(UUID.randomUUID())
                .name(TrackName.MONACO)
                .description("Monaco")
                .imageUrl("www.image.com")
                .type(TrackType.SUBSCRIPTION)
                .videos(List.of(video))
                .owner(owner)
                .build();

        when(trackRepository.save(any(Track.class))).thenReturn(track);

        // When
        Track result = trackService.createTrack(track.getName(), track.getDescription(), track.getImageUrl(), track.getType(), track.getVideos());

        // Then
        assertEquals(track.getName(), result.getName());
        assertEquals(track.getDescription(), result.getDescription());
        assertEquals(track.getImageUrl(), result.getImageUrl());
        assertEquals(track.getType(), result.getType());
        assertEquals(track.getVideos().size(), result.getVideos().size());
        assertEquals(track.getOwner(), result.getOwner());
        verify(trackRepository, times(1)).save(any(Track.class));
    }

    @Test
    void givenNonExistingTrack_whenAddTrackIfNotExists_thenReturnNewTrack() {

        // Given
        TrackName trackName = TrackName.MONACO;
        String description = "Monaco";
        String imageUrl = "www.image.com";
        TrackType type = TrackType.SUBSCRIPTION;
        when(trackRepository.findByName(trackName)).thenReturn(null);

        // When
        trackService.addTrackIfNotExists(trackName, description, imageUrl, type);

        // Then
        verify(trackRepository, times(1)).save(any(Track.class));
    }

    @Test
    void givenExistingTrack_whenAddTrackIfNotExists_thenDoNotSaveTrack() {

        // Given
        when(trackRepository.findByName(TrackName.MONACO)).thenReturn(new Track());

        // When
        trackService.addTrackIfNotExists(TrackName.MONACO, "", "", TrackType.SUBSCRIPTION);

        // Then
        verify(trackRepository, never()).save(any(Track.class));
    }

    @Test
    void givenTrackType_whenGetAllTracksByType_thenReturnListOfTracks() {

        // Given

        TrackType type = TrackType.SUBSCRIPTION;
        List<Track> tracks = List.of(new Track(), new Track());
        when(trackRepository.findAllByType(type)).thenReturn(tracks);

        // When
        List<Track> result = trackService.getAllTracksByType(type);

        // Then
        assertEquals(tracks.size(), result.size());
        verify(trackRepository, times(1)).findAllByType(type);
    }

    @Test
    void givenTrackId_whenGetTrackByID_thenReturnTrack() {

        // Given

        UUID trackId = UUID.randomUUID();
        Track track = Track.builder().build();
        when(trackRepository.getById(track.getId())).thenReturn(track);

        // When
        Track result = trackService.getTrackById(track.getId());

        // Then
        assertNotNull(result);
        assertEquals(track, result);
        verify(trackRepository, times(1)).getById(track.getId());
    }

    @Test
    void givenOwnerId_whenGetAllTracksByOwner_thenReturnListOfTracks() {

        // Given

        UUID ownerId = UUID.randomUUID();
        List<Track> tracks = List.of(new Track(), new Track());
        when(trackRepository.findAllTracksByUserId(ownerId)).thenReturn(tracks);

        // When
        List<Track> result = trackService.getAllTracks(ownerId);

        // Then
        assertEquals(tracks.size(),result.size());
        verify(trackRepository, times(1)).findAllTracksByUserId(ownerId);
    }
}
