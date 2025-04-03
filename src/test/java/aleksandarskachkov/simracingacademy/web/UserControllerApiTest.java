package aleksandarskachkov.simracingacademy.web;


import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.model.UserRole;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void putUnauthorizedRequestToSwitchRole_shouldReturn404AndNotFoundView() throws Exception {

        // 1. Build Request
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "User123", "123123", UserRole.USER, true);
        MockHttpServletRequestBuilder request = put("/users/{id}/role", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(view().name("not-found"));
    }

    @Test
    void putAuthorizedRequestToSwitchRole_shouldRedirectToUsers() throws Exception {

        // 1. Build Request
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "User123", "123123", UserRole.ADMIN, true);
        MockHttpServletRequestBuilder request = put("/users/{id}/role", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void putAuthorizedRequestToSwitchStatus_shouldRedirectToUsers() throws Exception {

        // 1. Build Request
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "User123", "123123", UserRole.ADMIN, true);
        MockHttpServletRequestBuilder request = put("/users/{id}/status", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }


@Test
void getAllUsers_shouldReturnUsersView() throws Exception {

    // 1. Build Request
    AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "User123", "123123", UserRole.ADMIN, true);
    MockHttpServletRequestBuilder request = get("/users")
            .with(user(principal));
    // 2. Send Request
    mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("users"))
            .andExpect(model().attributeExists("users"));
}

    @Test
    void getProfileMenu_shouldReturnProfileMenuView() throws Exception {

        // 1. Build Request
        UUID userId = UUID.randomUUID();
        when(userService.getById(userId)).thenReturn(User.builder().build());
        MockHttpServletRequestBuilder request = get("/users/{id}/profile", userId);
        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("profile-menu"))
                .andExpect(model().attributeExists("user", "userEditRequest"));
    }

    @Test
    void putInvalidUpdateProfileMenu_shouldReturnProfileMenuView() throws Exception {
        UUID userId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = get("/users/{id}/profile", userId)
                .param("invalidField", "")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("profile-menu"))
                .andExpect(model().attributeExists("user", "userEditRequest"));


    }

}
