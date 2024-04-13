package com.app.decaf.service;

import com.app.decaf.config.TestSecurityConfig;
import com.app.decaf.model.Entry;
import com.app.decaf.model.Transaction;
import com.app.decaf.repository.EntryRepository;
import com.app.decaf.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private EntryRepository entryRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void createTransaction_savesTransactionAndEntries() {
        // Arrange
        Transaction transaction = new Transaction();
        Entry entry1 = new Entry();
        Entry entry2 = new Entry();
        List<Entry> entries = Arrays.asList(entry1, entry2);

        // Mock the behavior of repositories
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(entryRepository.save(any(Entry.class))).thenAnswer(i -> i.getArgument(0)); // return passed entry

        // Act
        Transaction savedTransaction = transactionService.createTransaction(transaction, entries);

        // Assert
        // Verify transaction save
        verify(transactionRepository, times(1)).save(transaction);
        // Verify each entry is saved and linked to the saved transaction
        assertEquals(savedTransaction, entry1.getTransaction());
        assertEquals(savedTransaction, entry2.getTransaction());
        verify(entryRepository, times(entries.size())).save(any(Entry.class));
        // Ensure the service returns the saved transaction
        assertEquals(transaction, savedTransaction);
    }
}