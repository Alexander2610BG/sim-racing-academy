package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionStatus;
import aleksandarskachkov.simracingacademy.transaction.service.TransactionService;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.model.UserRole;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static aleksandarskachkov.simracingacademy.TestBuilder.aRandomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;


}
