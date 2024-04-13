package com.app.decaf.cotroller;

import com.app.decaf.config.TestSecurityConfig;
import com.app.decaf.model.Account;
import com.app.decaf.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.BDDMockito.given;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
@Import(TestSecurityConfig.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;  // Mock the AccountService since it's outside the Web Layer

    @Autowired
    private ObjectMapper objectMapper;  // For converting objects to JSON strings

    @Test
    public void whenPostAccount_thenCreateAccount() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setName("Sample Account");
        // Ensure the mock returns a valid account object
        given(accountService.addAccount(any(Account.class))).willReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(account)));
    }
}
