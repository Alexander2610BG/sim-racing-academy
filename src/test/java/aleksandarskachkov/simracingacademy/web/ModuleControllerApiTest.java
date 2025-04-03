package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.module.service.ModuleService;
import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.user.model.UserRole;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import aleksandarskachkov.simracingacademy.video.model.Video;
import aleksandarskachkov.simracingacademy.video.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static aleksandarskachkov.simracingacademy.TestBuilder.aRandomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ModuleController.class)
public class ModuleControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private ModuleService moduleService;
    @MockitoBean
    private VideoService videoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getModulePage_thenReturnModulePage() throws Exception {
        when(userService.getById(any())).thenReturn(aRandomUser());
        when(moduleService.getAllModules(aRandomUser().getId())).thenReturn(List.of(new Module(), new Module()));

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = get("/modules")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("modules"))
                .andExpect(model().attributeExists("modules", "user"));
    }

    @Test
    void getVideosForModule_thenReturnModuleWithVideosPage() throws Exception {
        UUID trackId = UUID.randomUUID();
        when(userService.getById(any())).thenReturn(aRandomUser());
        when(moduleService.getModuleById(trackId)).thenReturn(new Module());
        when(videoService.getVideosForTrack(trackId, aRandomUser().getId())).thenReturn(List.of(new Video()));

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = get("/modules/{id}/videos", trackId)
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("videos"))
                .andExpect(model().attributeExists("module", "user", "videos"));
    }
}
