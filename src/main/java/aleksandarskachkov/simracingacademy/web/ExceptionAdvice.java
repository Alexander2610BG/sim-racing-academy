package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.exception.*;
import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            DomainException.class,
            AccessDeniedException.class,
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            UserDoesntOwnTrack.class,
            UserDoesntOwnModule.class,
            ModuleNotFound.class,
            TrackNotFound.class,
            RetryableException.class
    })
    public ModelAndView handleNotFoundExceptions() {

        return new ModelAndView("not-found");
    }



    @ExceptionHandler(UsernameAlreadyExist.class)
    public String handleUsernameAlreadyExist(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        String username = request.getParameter("username");
        String message = "%s is already in use!".formatted(username);

        redirectAttributes.addFlashAttribute("usernameAlreadyExistMessage", message);
        return "redirect:/register";
    }

    @ExceptionHandler(WalletDoestNotExist.class)
    public String handleWalletDoesNotExist(RedirectAttributes redirectAttributes, WalletDoestNotExist exception) {

        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("walletDoesntExistErrorMessage", message);
        return "redirect:/home";
    }

    @ExceptionHandler(WalletDoesntBelong.class)
    public String handleWalletDoesntBelong(RedirectAttributes redirectAttributes, WalletDoestNotExist exception) {

        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("walletDoesntBelongErrorMessage", message);
        return "redirect:/home";
    }

    @ExceptionHandler(TransactionDoesntExist.class)
    public String handleTransactionDoesntExist(RedirectAttributes redirectAttributes, TransactionDoesntExist exception) {

        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("transactionDoesntExistErrorMessage", message);
        return "redirect:/home";
    }

    @ExceptionHandler(NotificationPreferenceDoesntExist.class)
    public String handleNotificationPreferenceDoesntExist(RedirectAttributes redirectAttributes, NotificationPreferenceDoesntExist exception) {

        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("notificationPreferenceDoesntExistErrorMessage", message);
        return "redirect:/home";
    }

    @ExceptionHandler(NoActiveSubscriptionFound.class)
    public String handleNoActiveSubscriptionFound(RedirectAttributes redirectAttributes, NoActiveSubscriptionFound exception) {

        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("noActiveSubscriptionFoundErrorMessage", message);
        return "redirect:/home";
    }



    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("internal-server-error");
        modelAndView.addObject("errorMessage", exception.getClass().getSimpleName());

        return modelAndView;

    }
}
