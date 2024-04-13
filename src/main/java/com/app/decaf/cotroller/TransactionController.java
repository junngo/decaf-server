package com.app.decaf.cotroller;

import com.app.decaf.dto.TransactionRequest;
import com.app.decaf.model.Entry;
import com.app.decaf.model.Transaction;
import com.app.decaf.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.createTransaction(request.getTransaction(), request.getEntries()));
    }
}
