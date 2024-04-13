package com.app.decaf.service;

import com.app.decaf.model.Account;
import com.app.decaf.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }
}
