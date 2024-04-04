package com.app.decaf.repository;

import com.app.decaf.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByAccountId(Long accountId);
    List<Entry> findByTransactionId(Long transactionId);
}
