package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.notification.client.dto.Notification;
import aleksandarskachkov.simracingacademy.notification.client.dto.NotificationPreference;
import aleksandarskachkov.simracingacademy.notification.service.NotificationService;
import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ModelAndView getNotificationsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        NotificationPreference notificationPreference = notificationService.getNotificationPreference(user.getId());
        List<Notification> notificationHistory = notificationService.getNotificationHistory(user.getId());
        long succeededNotificationsNumber = notificationHistory.stream().filter(n -> n.getStatus().equals("SUCCEEDED")).count();
        long failedNotificationNumber = notificationHistory.stream().filter(n -> n.getStatus().equals("FAILED")).count();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("notifications");


        modelAndView.addObject("notificationPreference", notificationPreference);
        modelAndView.addObject("notificationHistory", notificationHistory);
        modelAndView.addObject("succeededNotificationsNumber", succeededNotificationsNumber);
        modelAndView.addObject("failedNotificationsNumber", failedNotificationNumber);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @PutMapping("/user-preference")
    public String updateUserPreference(@RequestParam("enabled") boolean enabled, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        notificationService.updateNotificationPreference(authenticationMetadata.getUserId(), enabled);

        return "redirect:/notifications";
    }

    @DeleteMapping
    public String deleteNotificationHistory(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        UUID userId = authenticationMetadata.getUserId();

        notificationService.clearHistory(userId);

        return "redirect:/notifications";
    }

    @PutMapping
    public String retryFailedNotifications(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        UUID userId = authenticationMetadata.getUserId();

        notificationService.retryFailed(userId);

        return "redirect:/notifications";
    }
}
