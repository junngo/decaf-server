package com.app.decaf.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.app.decaf.model.Account;
import com.app.decaf.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void whenAddAccount_thenAccountIsSaved() {
        // Given
        Account account = new Account();
        account.setName("Test Account");

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // When
        Account savedAccount = accountService.addAccount(account);

        // Then
        assertEquals(account.getName(), savedAccount.getName());
        verify(accountRepository).save(account);  // Ensure save was called on repository
    }
}