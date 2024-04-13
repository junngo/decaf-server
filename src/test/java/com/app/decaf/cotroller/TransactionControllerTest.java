package com.app.decaf.cotroller;

import com.app.decaf.config.TestSecurityConfig;
import com.app.decaf.dto.TransactionRequest;
import com.app.decaf.model.Entry;
import com.app.decaf.model.Transaction;
import com.app.decaf.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@WebMvcTest(TransactionController.class)
@Import(TestSecurityConfig.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;  // Mock the TransactionService since it's outside the Web Layer

    @Autowired
    private ObjectMapper objectMapper;  // For converting objects to JSON strings

    @Test
    public void whenPostTransaction_thenCreateTransaction() throws Exception {
        Transaction transaction = new Transaction();  // Ensure this is correctly instantiated
        List<Entry> entries = List.of(new Entry(), new Entry());  // Ensure these entries are correctly instantiated
        TransactionRequest request = new TransactionRequest(transaction, entries);  // Assuming you've created a combined request class

        when(transactionService.createTransaction(any(Transaction.class), anyList())).thenReturn(transaction);  // Ensure this mock is correct

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))  // Ensure serialization here is correct
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transaction)));  // Ensure this expectation is valid
    }
}