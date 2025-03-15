package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import aleksandarskachkov.simracingacademy.wallet.service.WalletService;
import aleksandarskachkov.simracingacademy.web.dto.DonationRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/donations")
public class DonationController {

    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public DonationController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @GetMapping
    public ModelAndView getDonationPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("donations");
        modelAndView.addObject("user", user);
        modelAndView.addObject("donationRequest", DonationRequest.builder().build());

        return modelAndView;

    }

    @PostMapping
    public ModelAndView initiateDonation(@Valid DonationRequest donationRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("donations");
            modelAndView.addObject("user", user);
            modelAndView.addObject("donationRequest", donationRequest);
            return modelAndView;
        }

        Transaction transaction = walletService.donate(user, donationRequest);

        return new ModelAndView("redirect:/transactions/" + transaction.getId());
    }
}
