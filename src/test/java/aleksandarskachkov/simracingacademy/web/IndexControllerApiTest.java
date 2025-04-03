package aleksandarskachkov.simracingacademy.web;


import aleksandarskachkov.simracingacademy.exception.UsernameAlreadyExist;
import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.user.model.UserRole;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.UUID;

import static aleksandarskachkov.simracingacademy.TestBuilder.aRandomUser;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {


    // Важно когато тествам контролери трябва да мокна всички депендънсинта на контролера
    @MockitoBean
    private UserService userService;

    // Използвам MockMvc, за да изпращам заявки
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToIndexEndpoint_shouldReturnIndexView() throws Exception {

        // 1. Build Request
        MockHttpServletRequestBuilder request = get("/");

        // 2. Send Request
        //.andExpect() - проверявам резултата
        // MockMvcResultMatchers.status() - проверка на статуса
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void getRequestToRegisterEndpoint_shouldReturnRegisterView() throws Exception {

        // 1. Build Request
        MockHttpServletRequestBuilder request = get("/register");

        // 2. Send Request
        //.andExpect() - проверявам резултата
        // MockMvcResultMatchers.status() - проверка на статуса
        // model().attributeExists("registerRequest") - проверява дали има конкретен NOT NULL атрибут
        // model().attribute("registerRequest", instanceOf(RegisterRequest.class)) - ако има NULL
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"));

    }

    @Test
    void getRequestToLoginEndpoint_shouldReturnLoginView() throws Exception {

        // 1. Build Request
        MockHttpServletRequestBuilder request = get("/login");


        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"));

    }


    @Test
    void getRequestToLoginEndpointWithErrorParameter_shouldReturnLoginViewAndErrorMessageAttribute() throws Exception {

        // 1. Build Request
        MockHttpServletRequestBuilder request = get("/login").param("error", "");

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest", "errorMessage"));
//                .andExpect(model().attributeExists("errorRequest"));

    }


    // POST with correct form data
    // Expect:
    // status - 3xx Redirect Status
    // called .register method of userService
    // redirect to /login\

    @Test
    void postRequestToRegisterEndpoint_happyPath() throws Exception {

        // 1. Build Request
        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "Alex123")
                .formField("password", "Test123#")
                .formField("country", "BULGARIA")
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestToRegisterEndpointWhenUsernameAlreadyExist_thenRedirectToRegisterWithFlashParameter() throws Exception {

        // 1. Build Request
        when(userService.register(any())).thenThrow(new UsernameAlreadyExist("Username already exist!"));
        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "Alex123")
                .formField("password", "Test123#")
                .formField("country", "BULGARIA")
                .with(csrf());


        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("usernameAlreadyExistMessage"));
        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestToRegisterEndpointWithInvalidData_returnRegisterView() throws Exception {

        // 1. Build Request
        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "")
                .formField("password", "")
                .formField("country", "BULGARIA")
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
        verify(userService, never()).register(any());

    }

    @Test
    void getAuthenticatedRequestToHome_returnsHomeView() throws Exception {

        when(userService.getById(any())).thenReturn(aRandomUser());

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "Alex123", "Test123#", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/home")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("user"));
        verify(userService, times(1)).getById(userId);
    }

    @Test
    void getUnauthenticatedRequestToHome_redirectToLogin() throws Exception {

        MockHttpServletRequestBuilder request = get("/home");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());
        verify(userService, never()).getById(any());
    }

}
