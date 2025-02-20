package aleksandarskachkov.simracingacademy.user.service;

import aleksandarskachkov.simracingacademy.exception.DomainException;
import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.subscription.service.SubscriptionService;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import aleksandarskachkov.simracingacademy.track.service.TrackService;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.model.UserRole;
import aleksandarskachkov.simracingacademy.user.repository.UserRepository;
import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import aleksandarskachkov.simracingacademy.wallet.service.WalletService;
import aleksandarskachkov.simracingacademy.web.dto.LoginRequest;
import aleksandarskachkov.simracingacademy.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final WalletService walletService;
    private final PasswordEncoder passwordEncoder;
    private final TrackRepository trackRepository;
    private final TrackService trackService;

    @Autowired
    public UserService(UserRepository userRepository, SubscriptionService subscriptionService, WalletService walletService, PasswordEncoder passwordEncoder, TrackRepository trackRepository, TrackService trackService) {
        this.userRepository = userRepository;
        this.subscriptionService = subscriptionService;
        this.walletService = walletService;
        this.passwordEncoder = passwordEncoder;
        this.trackRepository = trackRepository;
        this.trackService = trackService;
    }

    @Transactional
    public User registerUser(RegisterRequest registerRequest) {

        Optional<User> userOptional = userRepository.findByUsername(registerRequest.getUsername());

        if (userOptional.isPresent()) {
            throw new DomainException("User with this username already exists.");
        }

        User user = userRepository.save(initializeUser(registerRequest));

        Subscription defaultSubscription = subscriptionService.createDefaultSubscription(user);
        user.setSubscriptions(List.of(defaultSubscription));

        Wallet wallet = walletService.createNewWallet(user);
        user.setWallet(wallet);

        // potentially to be removed if user cant buy tracks
        List<Track> defaultTracks = trackRepository.getAllTracksByType(TrackType.DEFAULT);
        user.setTracks(defaultTracks);

        log.info("Successfully created new user account for username [%s] and id [%s]"
                .formatted(user.getUsername(), user.getId()));

        return user;
    }

    public User login(LoginRequest loginRequest) {

        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());
        if (optionalUser.isEmpty()) {
            throw new DomainException("Username or password are invalid.");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new DomainException("Username or password are incorrect.");
        }

        return user;
    }

    private User initializeUser(RegisterRequest registerRequest) {

        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .country(registerRequest.getCountry())
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .tracks(trackService.getDefaultTracks())
                .build();
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }
}
