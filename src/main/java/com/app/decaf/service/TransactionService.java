package com.app.decaf.service;

import com.app.decaf.model.Entry;
import com.app.decaf.model.Transaction;
import com.app.decaf.model.User;
import com.app.decaf.repository.EntryRepository;
import com.app.decaf.repository.TransactionRepository;
import com.app.decaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final EntryRepository entryRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public List<Transaction> getAllTransactionsForCurrentUser() {
        User user = getCurrentUser();
        return transactionRepository.findAllByUser(user);
    }

    public Transaction getTransactionById(Long id) {
        User user = getCurrentUser();
        return transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Transaction not found or not authorized"));
    }

    public Transaction createTransaction(Transaction transaction) {
        User user = getCurrentUser();
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }
    public Transaction createTransaction(Transaction transaction, List<Entry> entries) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        for (Entry entry : entries) {
            entry.setTransaction(savedTransaction);
            entryRepository.save(entry);
        }
        return savedTransaction;
    }

    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        User user = getCurrentUser();
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Transaction not found or not authorized"));

        transaction.setDate(updatedTransaction.getDate());
        transaction.setAmount(updatedTransaction.getAmount());
        transaction.setDescription(updatedTransaction.getDescription());
        transaction.setCategory(updatedTransaction.getCategory());
        transaction.setAccount(updatedTransaction.getAccount());
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        User user = getCurrentUser();
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Transaction not found or not authorized"));
        transactionRepository.delete(transaction);
    }
}
