package com.app.decaf.service;

import com.app.decaf.model.Entry;
import com.app.decaf.model.Transaction;
import com.app.decaf.repository.EntryRepository;
import com.app.decaf.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final EntryRepository entryRepository;

    public Transaction createTransaction(Transaction transaction, List<Entry> entries) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        for (Entry entry : entries) {
            entry.setTransaction(savedTransaction);
            entryRepository.save(entry);
        }
        return savedTransaction;
    }
}
