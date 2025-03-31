package aleksandarskachkov.simracingacademy.video;

import aleksandarskachkov.simracingacademy.exception.DomainException;
import aleksandarskachkov.simracingacademy.exception.UserDoesntOwnModule;
import aleksandarskachkov.simracingacademy.exception.UserDoesntOwnTrack;
import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.module.model.ModuleName;
import aleksandarskachkov.simracingacademy.module.model.ModuleType;
import aleksandarskachkov.simracingacademy.module.repository.ModuleRepository;
import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionStatus;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionType;
import aleksandarskachkov.simracingacademy.subscription.repository.SubscriptionRepository;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import aleksandarskachkov.simracingacademy.video.model.Video;
import aleksandarskachkov.simracingacademy.video.repository.VideoRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoServiceUTest {

    @Mock
    private VideoRepository videoRepository;
    @Mock
    private TrackRepository trackRepository;
    @Mock
    private ModuleRepository moduleRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private VideoService videoService;

    @Test
    void givenVideoDetailsForTrack_whenCreateVideoForTrack_thenReturnNewVideoForTrack() {

        // Given
        String title = "Title";
        String videoUrl = "asdf";
        String description = "asdf";
        TrackName trackName = TrackName.SPA_FRANCORCHAMPS;
        Track track = Track.builder().name(trackName).build();
        when(trackRepository.findByName(trackName)).thenReturn(track);
        when(videoRepository.save(any(Video.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Video result = videoService.createVideoForTrack(title, videoUrl, description, trackName);

        // Then
        assertEquals(title, result.getTitle());
        assertEquals(videoUrl, result.getVideoUrl());
        assertEquals(description, result.getDescription());
        assertEquals(track, result.getTrack());
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    void givenInvalidTrackName_whenCreateVideoForTrack_thenThrowException() {

        // Given
        String title = "Title";
        String videoUrl = "asdf";
        String description = "asdf";
        TrackName trackName = TrackName.SPA_FRANCORCHAMPS;
        Track track = Track.builder().name(trackName).build();
        when(trackRepository.findByName(trackName)).thenReturn(null);

        // When & Then
        assertThrows(DomainException.class, () -> videoService.createVideoForTrack(title, videoUrl, description, trackName));
        verify(videoRepository, never()).save(any(Video.class));
    }

    @Test
    void givenVideoDetailsForModule_whenCreateVideoForModule_thenReturnNewVideoForModule() {

        // Given
        String title = "Title";
        String videoUrl = "asdf";
        String description = "asdf";
        ModuleName moduleName = ModuleName.VEHICLE_SETUP_OPTIMIZATION;
        Module module = Module.builder().name(moduleName).build();
        when(moduleRepository.findByName(moduleName)).thenReturn(module);
        when(videoRepository.save(any(Video.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Video result = videoService.createVideoForModule(title, videoUrl, description, moduleName);

        // Then
        assertEquals(title, result.getTitle());
        assertEquals(videoUrl, result.getVideoUrl());
        assertEquals(description, result.getDescription());
        assertEquals(module, result.getModule());
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    void givenInvalidModuleName_whenCreateVideoForModule_thenThrowException() {

        // Given
        String title = "Title";
        String videoUrl = "asdf";
        String description = "asdf";
        ModuleName moduleName = ModuleName.VEHICLE_SETUP_OPTIMIZATION;
        when(moduleRepository.findByName(moduleName)).thenReturn(null);

        // When & Then
        assertThrows(DomainException.class, () -> videoService.createVideoForModule(title, videoUrl, description, moduleName));
        verify(videoRepository, never()).save(any(Video.class));
    }

    @Test
    void givenValidTrackAndActiveSubscription_whenGetVideosForTrack_thenReturnTrackById() {

        // Given
        UUID trackId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Track track = Track.builder()
                .id(trackId)
                .type(TrackType.DEFAULT)
                .build();

        Subscription subscription = Subscription.builder()
                .type(SubscriptionType.DEFAULT)
                .build();
        List<Video> videos = List.of(new Video(), new Video());

        when(trackRepository.getTrackById(trackId)).thenReturn(track);
        when(subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, userId))
                .thenReturn(Optional.of(subscription));
        when(videoRepository.findAllByTrackId(trackId)).thenReturn(videos);

        // When
        List<Video> result = videoService.getVideosForTrack(trackId, userId);

        // Then
        assertEquals(videos, result);
        verify(videoRepository, times(1)).findAllByTrackId(trackId);
    }

    @Test
    void givenNoActiveSubscription_whenGetVideosForTrack_thenThrowException() {

        // Given
        UUID trackId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Track track = Track.builder()
                .id(trackId)
                .type(TrackType.DEFAULT)
                .build();

        when(trackRepository.getTrackById(trackId)).thenReturn(track);
        when(subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DomainException.class, () -> videoService.getVideosForTrack(trackId, userId));
        verify(videoRepository, never()).findAllByTrackId(any(UUID.class));
    }

    @Test
    void givenPremiumSubscription_whenGetVideosForTrack_thenReturnTrackVideos() {

        // Given
        UUID trackId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Track track = Track.builder()
                .id(trackId)
                .type(TrackType.SUBSCRIPTION)
                .build();

        Subscription subscription = Subscription.builder()
                .type(SubscriptionType.PREMIUM)
                .build();

        List<Video> videos = List.of(new Video(), new Video());

        when(trackRepository.getTrackById(trackId)).thenReturn(track);
        when(subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, userId))
                .thenReturn(Optional.of(subscription));
        when(videoRepository.findAllByTrackId(trackId)).thenReturn(videos);

        // When
        List<Video> result = videoService.getVideosForTrack(trackId, userId);

        // Then
        assertEquals(videos, result);
        verify(videoRepository, times(1)).findAllByTrackId(any(UUID.class));
    }

    @Test
    void givenUltimateSubscription_whenGetVideosForTrack_thenReturnTrackVideos() {

        // Given
        UUID trackId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Track track = Track.builder()
                .id(trackId)
                .type(TrackType.SUBSCRIPTION)
                .build();

        Subscription subscription = Subscription.builder()
                .type(SubscriptionType.ULTIMATE)
                .build();

        List<Video> videos = List.of(new Video(), new Video());

        when(trackRepository.getTrackById(trackId)).thenReturn(track);
        when(subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, userId))
                .thenReturn(Optional.of(subscription));
        when(videoRepository.findAllByTrackId(trackId)).thenReturn(videos);

        // When
        List<Video> result = videoService.getVideosForTrack(trackId, userId);

        // Then
        assertEquals(videos, result);
        verify(videoRepository, times(1)).findAllByTrackId(any(UUID.class));
    }

    @Test
    void givenUserWithoutPremiumOrUltimateSubscription_whenGetVideosForTrack_thenThrowsException() {

        // Given

        UUID trackId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Track track = Track.builder()
                .id(trackId)
                .type(TrackType.SUBSCRIPTION)
                .build();

        Subscription subscription = Subscription.builder()
                .type(SubscriptionType.DEFAULT)
                .build();

        when(trackRepository.getTrackById(trackId)).thenReturn(track);
        when(subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, userId)).thenReturn(Optional.of(subscription));

        // When & Then
        assertThrows(UserDoesntOwnTrack.class, () -> videoService.getVideosForTrack(trackId, userId));
        verify(videoRepository, never()).findAllByTrackId(any(UUID.class));

    }

    @Test
    void givenValidModuleAndActiveSubscription_whenGetVideosForModule_thenReturnModuleById() {

        // Given
        UUID moduleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Module module = Module.builder()
                .id(moduleId)
                .type(ModuleType.DEFAULT)
                .build();

        Subscription subscription = Subscription.builder()
                .type(SubscriptionType.DEFAULT)
                .build();
        List<Video> videos = List.of(new Video(), new Video());

        when(moduleRepository.getModuleById(moduleId)).thenReturn(module);
        when(subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, userId))
                .thenReturn(Optional.of(subscription));
        when(videoRepository.findAllByModuleId(moduleId)).thenReturn(videos);

        // When
        List<Video> result = videoService.getVideosForModule(moduleId, userId);

        // Then
        assertEquals(videos, result);
        verify(videoRepository, times(1)).findAllByModuleId(moduleId);
    }

    @Test
    void givenUserWithoutPremiumSubscription_whenGetVideosForModule_thenReturnModuleVideos() {

        // Given
        UUID moduleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Module module = Module.builder()
                .id(moduleId)
                .type(ModuleType.SUBSCRIPTION)
                .build();

        Subscription subscription = Subscription.builder()
                .type(SubscriptionType.PREMIUM)
                .build();

        when(moduleRepository.getModuleById(moduleId)).thenReturn(module);
        when(subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, userId))
                .thenReturn(Optional.of(subscription));

        // When * Then
        assertThrows(UserDoesntOwnModule.class, () -> videoService.getVideosForModule(moduleId, userId));
        verify(videoRepository, never()).findAllByModuleId(any(UUID.class));
    }

    @Test
    void givenUltimateSubscription_whenGetVideosForModule_thenReturnModuleById() {

        // Given
        UUID moduleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Module module = Module.builder()
                .id(moduleId)
                .type(ModuleType.SUBSCRIPTION)
                .build();

        Subscription subscription = Subscription.builder()
                .type(SubscriptionType.ULTIMATE)
                .build();
        List<Video> videos = List.of(new Video(), new Video());

        when(moduleRepository.getModuleById(moduleId)).thenReturn(module);
        when(subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, userId))
                .thenReturn(Optional.of(subscription));
        when(videoRepository.findAllByModuleId(moduleId)).thenReturn(videos);

        // When
        List<Video> result = videoService.getVideosForModule(moduleId, userId);

        // Then
        assertEquals(videos, result);
        verify(videoRepository, times(1)).findAllByModuleId(moduleId);
    }

    @Test
    void givenNoActiveSubscription_whenGetVideosForModule_thenThrowException() {

        // Given
        UUID moduleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Module module = Module.builder()
                .id(moduleId)
                .type(ModuleType.SUBSCRIPTION)
                .build();

        when(moduleRepository.getModuleById(moduleId)).thenReturn(module);
        when(subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DomainException.class, () -> videoService.getVideosForModule(moduleId, userId));
        verify(videoRepository, never()).findAllByTrackId(any(UUID.class));
    }

    @Test
    void givenVideoDoesNotExist_whenAddVideoIfNotExistsForTrack_thenCreateVideo() {

        // Given
        String title = "Title";
        String url = "asdf";
        String description = "asdf";
        TrackName trackName = TrackName.MONZA;

        Track track = Track.builder()
                .name(trackName)
                .build();

        when(videoRepository.existsByTitleAndTrackName(title, trackName)).thenReturn(false);
        when(trackRepository.findByName(trackName)).thenReturn(track);
        when(videoRepository.save(any(Video.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        videoService.addVideoIfNotExistsForTrack(title, url, description, trackName);

        // Then
        verify(videoRepository, times(1)).existsByTitleAndTrackName(title, trackName);
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    void givenVideoExists_whenAddVideoIfExistsForTrack_thenDoNotCreateVideo() {

        // Given
        String title = "Title";
        String url = "asdf";
        String description = "asdf";
        TrackName trackName = TrackName.MONZA;

        when(videoRepository.existsByTitleAndTrackName(title, trackName)).thenReturn(true);

        // When
        videoService.addVideoIfNotExistsForTrack(title, url, description, trackName);

        // Then
        verify(videoRepository, times(1)).existsByTitleAndTrackName(title, trackName);
        verify(videoRepository, never()).save(any(Video.class));
    }

    @Test
    void givenVideoDoesNotExist_whenAddVideoIfNotExistsForModule_thenCreateVideo() {

        // Given
        String title = "Title";
        String url = "asdf";
        String description = "asdf";
        ModuleName moduleName = ModuleName.VEHICLE_SETUP_OPTIMIZATION;

        Module module = Module.builder()
                .name(moduleName)
                .build();

        when(videoRepository.existsByTitleAndModuleName(title, moduleName)).thenReturn(false);
        when(moduleRepository.findByName(moduleName)).thenReturn(module);
        when(videoRepository.save(any(Video.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        videoService.addVideoIfNotExistsForModule(title, url, description, moduleName);

        // Then
        verify(videoRepository, times(1)).existsByTitleAndModuleName(title, moduleName);
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    void givenVideoExists_whenAddVideoIfExistsForModule_thenDoNotCreateVideo() {

        // Given
        String title = "Title";
        String url = "asdf";
        String description = "asdf";
        ModuleName moduleName = ModuleName.VEHICLE_SETUP_OPTIMIZATION;

        when(videoRepository.existsByTitleAndModuleName(title, moduleName)).thenReturn(true);

        // When
        videoService.addVideoIfNotExistsForModule(title, url, description, moduleName);

        // Then
        verify(videoRepository, times(1)).existsByTitleAndModuleName(title, moduleName);
        verify(videoRepository, never()).save(any(Video.class));
    }
}
